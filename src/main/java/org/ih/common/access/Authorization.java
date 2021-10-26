package org.ih.common.access;

import org.ih.account.AccountRole;
import org.ih.dao.DAOFactory;
import org.ih.dao.DatabaseModel;
import org.ih.dao.Repository;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dto.Account;

/**
 * Used in instances where access permissions are to be enforced.
 * <p>
 * Currently, the only rule is that a user must belong to the same
 * group as the owner of the object being accessed, in order to be able to
 * read it.
 *
 * @author Hector Plahar
 */
public abstract class Authorization<T extends DatabaseModel> {

    protected final Repository<T> repository;

    public Authorization(Repository<T> repository) {
        this.repository = repository;
    }

    protected AccountModel getAccount(String userId) {
        AccountModel accountModel = DAOFactory.getAccountDAO().getByEmail(userId.toLowerCase());
        if (accountModel == null)
            throw new IllegalArgumentException("Could not retrieve account information for user " + userId);
        return accountModel;
    }

    protected Account getOwner(T object) {
        return null;
    }

    public boolean isAdmin(String userId) {
        return this.isUserHasSpecifiedRole(userId, AccountRole.ADMINISTRATOR);
    }

    public boolean canRead(String userId, T object) {
        return (isOwner(userId, object) || isAdmin(userId));
    }

    protected boolean isOwner(String userId, T object) {
        Account owner = getOwner(object);
        return owner == null || userId.equalsIgnoreCase(owner.getEmail());
    }

    protected boolean isUserHasSpecifiedRole(String userId, AccountRole role) {
        AccountDAO dao = (AccountDAO) this.repository;
        AccountModel accountModel = dao.getByEmail(userId);
        if (accountModel == null)
            throw new NullPointerException("No account for specified id " + userId);

        if (role == null)
            throw new NullPointerException("Cannot check against null role");

        return accountModel.getRoles().contains(role);
    }

    public void expectRead(String userId, T object) throws AuthorizationException {
        if (!canRead(userId, object))
            throw new AuthorizationException(userId + " does not have the required read privileges");
    }

    /**
     * Should either be an administrator or the owner of the object to be able to write
     *
     * @param userId unique user identifier
     * @param object object write ownership is being checked against
     * @return true is user is an admin or the owner of the object
     */
    public boolean canWrite(String userId, T object) {
        if (isAdmin(userId))
            return true;

        Account owner = getOwner(object);
        return owner == null || userId.equals(owner.getEmail());
    }

    public void expectWrite(String userId, T object) throws AuthorizationException {
        if (!canWrite(userId, object))
            throw new AuthorizationException(userId);
    }

    public void expectAdmin(String userId) throws AuthorizationException {
        if (!isAdmin(userId))
            throw new AuthorizationException(userId + " attempting to access admin restricted action");
    }
}
