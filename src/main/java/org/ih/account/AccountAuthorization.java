package org.ih.account;

import org.ih.common.access.Authorization;
import org.ih.dao.DAOFactory;
import org.ih.dao.model.AccountModel;

/**
 * @author Hector Plahar
 */
public class AccountAuthorization extends Authorization<AccountModel> {

    public AccountAuthorization() {
        super(DAOFactory.getAccountDAO());
    }

}
