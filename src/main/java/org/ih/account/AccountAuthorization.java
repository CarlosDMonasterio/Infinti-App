package org.ih.account;

import org.ih.common.access.Authorization;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;

/**
 * @author Hector Plahar
 */
public class AccountAuthorization extends Authorization<AccountModel> {

    public AccountAuthorization() {
        super(DAOFactory.getAccountDAO());
    }

    public boolean isUserHasSpecifiedRole(String userId, AccountRole role) {
        AccountDAO dao = (AccountDAO) this.repository;
        AccountModel accountModel = dao.getByEmail(userId);
        if (accountModel == null)
            throw new NullPointerException("No account for specified id " + userId);

        if (role == null)
            throw new NullPointerException("Cannot check against null role");

        return accountModel.getRoles().contains(role);
    }
}
