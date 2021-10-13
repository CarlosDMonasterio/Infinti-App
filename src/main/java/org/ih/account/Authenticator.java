package org.ih.account;

import org.ih.account.authentication.*;
import org.ih.common.logging.Logger;
import org.ih.config.ConfigurationValue;
import org.ih.config.Settings;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dto.Account;
import org.ih.util.StringUtil;

import java.util.Date;

/**
 * Responsible for handling all authentication related actions
 *
 * @author Hector Plahar
 */
public class Authenticator {

    private final AccountDAO dao;

    public Authenticator() {
        dao = DAOFactory.getAccountDAO();
    }

    public Account authenticate(String userName, String password) throws AuthenticationException {
        if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password))
            throw new IllegalArgumentException("Invalid credentials");

        userName = userName.toLowerCase().trim();
        Authentication authentication = getAuthentication();

        if (!authentication.authenticates(userName, password))
            return null;

        String sid = SessionHandler.createNewSessionForUser(userName);
        AccountModel accountModel = dao.getByEmail(userName);
        accountModel.setLastLoginTime(new Date());
        dao.update(accountModel);

        Account transfer = accountModel.toDataObject();
        transfer.setSessionId(sid);
        boolean isAdmin = ((accountModel.getRoles() != null && accountModel.getRoles().contains(AccountRole.ADMINISTRATOR))
                || (Accounts.DEFAULT_ADMIN_USERID.equalsIgnoreCase(userName)));
        transfer.setAdministrator(isAdmin);
        return transfer;
    }

    private Authentication getAuthentication() {
        try {
            String clazz = new Settings().getValue(ConfigurationValue.AUTHENTICATION_METHOD);
            if (StringUtil.isEmpty(clazz))
                return new LocalAuthentication();

            switch (AuthType.valueOf(clazz.toUpperCase())) {
                case OPEN:
                    return new UserIdOnlyAuthentication();
                case DEFAULT:
                default:
                    return new LocalAuthentication();
            }
        } catch (Exception e) {
            Logger.error("Exception loading authentication class: ", e);
            Logger.error("Using default authentication");
            return new LocalAuthentication();
        }
    }
}
