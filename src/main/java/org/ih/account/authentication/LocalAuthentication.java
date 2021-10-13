package org.ih.account.authentication;

import org.ih.account.Accounts;
import org.ih.account.PasswordUtil;
import org.ih.common.exception.UtilityException;
import org.ih.dao.DAOFactory;
import org.ih.dao.DataAccessException;
import org.ih.dao.model.AccountModel;
import org.ih.util.StringUtil;

/**
 * Backend for authentication using the database. This is the default backend.
 *
 * @author Hector Plahar
 */
public class LocalAuthentication implements Authentication {

    public LocalAuthentication() {
    }

    @Override
    public boolean authenticates(String userId, String password) throws AuthenticationException {
        Accounts accounts = new Accounts();
        boolean accountExists = accounts.accountExists(userId);

        if (!accountExists)
            return false;

        boolean isValid = isValidPassword(userId, password);
        if (!isValid) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                // ?
            }
        }
        return isValid;
    }

    private boolean isValidPassword(String username, String password) throws AuthenticationException {
        AccountModel accountModel;
        try {
            accountModel = DAOFactory.getAccountDAO().getByEmail(username);
        } catch (DataAccessException e) {
            throw new AuthenticationException("Exception retrieving account by id " + username, e);
        }

        if (accountModel == null)
            throw new AuthenticationException("Cannot authenticate password for non-existent account " + username);

        String existingPassword = accountModel.getPassword().trim();
        if (StringUtil.isEmpty(accountModel.getSalt()) || StringUtil.isEmpty(existingPassword))
            return false;

        try {
            return existingPassword.equals(PasswordUtil.encryptPassword(password, accountModel.getSalt()));
        } catch (UtilityException e) {
            throw new AuthenticationException("Exception encrypting user password", e);
        }
    }
}
