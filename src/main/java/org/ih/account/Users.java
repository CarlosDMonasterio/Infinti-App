package org.ih.account;

import com.opencsv.CSVReader;
import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.GroupModel;
import org.ih.dto.Account;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Hector Plahar
 */
public class Users {

    private final AccountDAO dao;
    private final String userId;
    private final AccountAuthorization authorization;

    public Users(String userId) {
        this.dao = DAOFactory.getAccountDAO();
        this.userId = userId;
        this.authorization = new AccountAuthorization();
    }

    public List<Account> filter(String token, int limit) {
        if (authorization.isAdmin(this.userId)) {
            List<AccountModel> results = dao.getMatchingAccounts(token, limit);
            List<Account> accounts = new ArrayList<>();
            for (AccountModel match : results) {
                Account info = new Account();
                info.setId(match.getId());
                info.setEmail(match.getEmail());
                info.setFirstName(match.getFirstName());
                info.setLastName(match.getLastName());
                info.setPhone(match.getPhone());
                info.setDescription(match.getDescription());
                accounts.add(info);
            }
            return accounts;
        }

        List<Account> result = new ArrayList<>();
        AccountModel tokenAccountModel = dao.getByEmail(token);
        if (tokenAccountModel != null) {
            result.add(tokenAccountModel.toDataObject());
            return result;
        }

        // else non admin; filter by users that account is allowed to see
        // this is typically based on group membership
        AccountModel accountModel = dao.getByEmail(userId);
        Set<GroupModel> groups = accountModel.getGroups();
        if (groups.isEmpty())
            return result;

        List<AccountModel> matches = dao.getMatchingGroupMembers(groups, token, limit);
        result.addAll(matches.stream().map(AccountModel::toDataObject).toList());
        return result;
    }

    public void importFile(InputStream inputStream, boolean notifyUsers) {
        // expected file format is
        // "First Name", "Last Name", "Email", "Phone", "Role/Description" (optional)
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

        int i = 0;
        Accounts accounts = new Accounts();

        for (String[] next : reader) {
            if (next.length != 5) {
                throw new IllegalArgumentException("Cannot import file. Improper format");
            }

            if (++i == 1)
                continue;

            String firstName = next[0];
            String lastName = next[1];
            String email = next[2];
            String phone = next[3];
            String role = next[4];

            AccountModel model = DAOFactory.getAccountDAO().getByEmail(next[1]);
            if (model != null) {
                Logger.error("User with email: " + email + " already exists. Skipping account creation");
                continue;
            }

            Logger.info("Creating account for user: " + email);
            Account account = new Account();
            account.setEmail(email);
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.setPhone(phone);
            account.setDescription(role);
            accounts.createAccount(this.userId, account, notifyUsers);
        }
    }
}
