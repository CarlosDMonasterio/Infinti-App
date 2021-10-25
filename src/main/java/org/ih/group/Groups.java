package org.ih.group;

import org.ih.account.Accounts;
import org.ih.common.Results;
import org.ih.common.access.AuthorizationException;
import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.hibernate.GroupDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.GroupModel;
import org.ih.dto.Account;
import org.ih.dto.GroupTransfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Hector Plahar
 */
public class Groups {

    private final AccountDAO accountDAO;
    private final GroupDAO dao;
    private final String userId;
    private boolean isAdmin;

    public Groups(String userId) {
        this.accountDAO = DAOFactory.getAccountDAO();
        this.dao = DAOFactory.getGroupDAO();
        this.userId = userId;
        this.isAdmin = new Accounts().isAdministrator(userId);
    }

    public boolean addUserToGroup(long groupId, String userId) {
        AccountModel accountModel = accountDAO.getByEmail(userId);
        if (accountModel == null)
            throw new IllegalArgumentException("User with id '" + userId + "' does not exist");

        GroupModel group = dao.get(groupId);
        if (group == null)
            throw new IllegalArgumentException("Group with id '" + groupId + "' does not exist");

        if (accountModel.getGroups().contains(group))
            return false;

        accountModel.getGroups().add(group);
        accountDAO.update(accountModel);
        return true;
    }

    public Results<GroupTransfer> get(GroupType type, int start, int limit, String filter) {
        AccountModel accountModel = accountDAO.getByEmail(userId);
        List<GroupModel> groups = dao.availableGroups(type, accountModel, start, limit, filter);
        Results<GroupTransfer> result = new Results<>();
        for (GroupModel group : groups) {
            GroupTransfer transfer = group.toDataObject();
            if (group.getOwner() != null) {
                AccountModel owner = group.getOwner();
                Account account = new Account();
                account.setId(owner.getId());
                account.setEmail(owner.getEmail());
                transfer.setOwner(account);
            }
            long memberCount = dao.getMemberCount(group.getId());
            transfer.setMemberCount(memberCount);
            result.getRequested().add(transfer);
        }
        result.setAvailable(dao.availableCount(type, accountModel, filter));
        return result;
    }

    public GroupTransfer add(GroupTransfer transfer) {
        if (transfer.getType() == null)
            transfer.setType(GroupType.PRIVATE);

        GroupModel group = new GroupModel();
        group.setType(transfer.getType());
        group.setDescription(transfer.getDescription());
        group.setLabel(transfer.getDisplay());
        group.setCreationTime(new Date());
        AccountModel accountModel = this.accountDAO.getByEmail(this.userId);
        group.setOwner(accountModel);
        group = dao.create(group);
        setGroupMembers(group, transfer.getMembers());
        return group.toDataObject();
    }

    public GroupTransfer update(long id, GroupTransfer transfer) {
        GroupModel group = this.dao.get(id);
        if (group == null)
            throw new IllegalArgumentException("No group available with id " + id);

        group.setDescription(transfer.getDescription());
        group.setLabel(transfer.getDisplay());
        return dao.create(group).toDataObject();
    }

    public List<Account> getMembers(long groupId) {
        GroupModel group = expectWrite(groupId, false);
        List<AccountModel> accountModels = accountDAO.getGroupMembers(group);
        List<Account> results = new ArrayList<>(accountModels.size());
        for (AccountModel accountModel : accountModels)
            results.add(accountModel.toDataObject());
        return results;
    }

    public List<GroupTransfer> getMatchingGroups(String token, int limit) {
        AccountModel accountModel = this.accountDAO.getByEmail(this.userId);
        List<GroupModel> groups = dao.availableGroups(GroupType.PRIVATE, accountModel, 0, limit, token);
        List<GroupTransfer> result = new ArrayList<>();
        for (GroupModel group : groups) {
            GroupTransfer groupTransfer = group.toDataObject();
            groupTransfer.setMemberCount(dao.getMemberCount(group.getId()));
            result.add(groupTransfer);
        }
        return result;
    }

    public boolean removeMemberFromGroup(long groupId, long memberId) {
        GroupModel group = expectWrite(groupId, true);
        AccountModel accountModel = accountDAO.get(memberId);
        if (accountModel == null)
            throw new IllegalArgumentException("Cannot find member with id " + memberId);
        accountDAO.removeGroup(group, accountModel);
        return true;
    }

    public boolean remove(long groupId) {
        GroupModel group = expectWrite(groupId, false);
        for (AccountModel accountModel : accountDAO.getGroupMembers(group))
            accountDAO.removeGroup(group, accountModel);
        dao.delete(group);
        return true;
    }

    /**
     * Verify that the user has appropriate write privileges on the group
     *
     * @param groupId     unique identifier for group being checked
     * @param canBeMember if true, then it is ok (permissions-wise) for the user to be just a member of the group.
     * @return group object if verification is successful
     * @throws AuthorizationException if the appropriate privileges do not exist
     */
    protected GroupModel expectWrite(long groupId, boolean canBeMember) {
        GroupModel group = this.dao.get(groupId);
        if (group == null)
            throw new IllegalArgumentException("No group available with id " + groupId);

        if (!group.getOwner().getEmail().equalsIgnoreCase(this.userId) && !this.isAdmin) {
            if (canBeMember) {
                AccountModel accountModel = this.accountDAO.getByEmail(this.userId);
                if (dao.isGroupMember(group, accountModel))
                    return group;
            }
            throw new AuthorizationException(userId + " has no access privileges for group " + groupId);
        }

        return group;
    }

    private void setGroupMembers(GroupModel group, List<Account> members) {
        if (members == null || members.isEmpty())
            return;

        Set<String> memberHash = group.getMembers().stream().map(AccountModel::getEmail).collect(Collectors.toSet());

        for (Account account : members) {
            String userId = account.getEmail();

            // do not add if found in set
            if (memberHash.remove(userId))
                continue;

            // not found so add
            AccountModel memberToAdd = accountDAO.getByEmail(userId);
            if (memberToAdd == null) {
                Logger.error("Could not find account " + userId + " to add to group");
                continue;
            }

            group.getMembers().add(memberToAdd);
            memberToAdd.getGroups().add(group);
            accountDAO.update(memberToAdd);
        }

        // all emails remaining should be removed
        for (String memberEmail : memberHash) {
            AccountModel memberAccountModel = accountDAO.getByEmail(memberEmail);
            if (memberAccountModel == null)
                continue;
            memberAccountModel.getGroups().remove(group);
            group.getMembers().remove(memberAccountModel);
            accountDAO.update(memberAccountModel);
        }
    }
}
