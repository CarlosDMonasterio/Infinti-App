package org.ih.account;

import org.apache.commons.lang3.StringUtils;
import org.ih.account.authentication.AuthenticationException;
import org.ih.common.Results;
import org.ih.common.exception.ServiceException;
import org.ih.common.exception.UtilityException;
import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dto.Account;
import org.ih.notification.NotificationTask;
import org.ih.task.TaskRunner;
import org.ih.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Hector Plahar
 */
public class Accounts {

    private final AccountAuthorization authorization = new AccountAuthorization();
    private String userId;
    private final AccountDAO dao;
    public static final String DEFAULT_ADMIN_USERID = "Administrator";
    private static final long MIN_PASSWORD_RESET_PERIOD = 1000 * 60 * 60 * 3;  // 3 hour in milliseconds

    public Accounts() {
        dao = DAOFactory.getAccountDAO();
    }

    public Accounts(String userId) {
        dao = DAOFactory.getAccountDAO();
        this.userId = userId;
    }

    public boolean addRole(long id, AccountRole role) {
        authorization.expectAdmin(userId);
        AccountModel accountModel = DAOFactory.getAccountDAO().get(id);
        if (!accountModel.getRoles().add(role))
            return true; // todo : already here but if the request is being made then requester does not know?

        accountModel.setLastUpdateTime(new Date());
        return dao.update(accountModel) != null;
    }

    public boolean removeRole(long id, AccountRole role) {
        authorization.expectAdmin(userId);
        AccountModel accountModel = DAOFactory.getAccountDAO().get(id);

        if (!accountModel.getRoles().remove(role))
            return true; // todo : ditto see add role
        accountModel.setLastUpdateTime(new Date());
        return dao.update(accountModel) != null;
    }

    public void createDefaultAdminAccount() throws ServiceException {
        AccountModel accountModel = dao.getByEmail(DEFAULT_ADMIN_USERID);
        String newPassword = PasswordUtil.generateRandomToken(48);

        if (accountModel != null) {
            Logger.info("Resetting Administrator account password");
            try {
                accountModel.setPassword(PasswordUtil.encryptPassword(newPassword, accountModel.getSalt()));
            } catch (UtilityException e) {
                throw new ServiceException("Exception encrypting password", e);
            }
            accountModel.setLastUpdateTime(accountModel.getCreationTime());
            dao.update(accountModel);

        } else {
            Logger.info("Creating Administrator Account");
            accountModel = new AccountModel();
            accountModel.getRoles().add(AccountRole.ADMINISTRATOR);
            accountModel.setCreationTime(new Date(System.currentTimeMillis()));
            accountModel.setLastUpdateTime(accountModel.getCreationTime());
            accountModel.setFirstName("Administrator");
            accountModel.setLastName("");
            accountModel.setDisabled(false);
            accountModel.setEmail(DEFAULT_ADMIN_USERID.toLowerCase());
            accountModel.setSalt(PasswordUtil.generateSalt());

            try {
                accountModel.setPassword(PasswordUtil.encryptPassword(newPassword, accountModel.getSalt()));
            } catch (UtilityException ue) {
                throw new ServiceException("Exception encrypting password", ue);
            }

            dao.create(accountModel);
        }

        // add log information for admin password
        Logger.info("NEW ADMIN PASSWORD");
        Logger.info("************************");
        Logger.info(newPassword);
        Logger.info("************************");
    }

    public Account createAccount(String userId, Account account, boolean sendEmailNotification) {
        if (account == null || account.getEmail() == null || account.getEmail().trim().isEmpty())
            throw new ServiceException("User id is required to create an account");

        if (StringUtil.isEmpty(account.getFirstName()) || StringUtil.isEmpty(account.getLastName()))
            throw new ServiceException("First and Last names are required to create an account");

        if (StringUtil.isEmpty(account.getEmail()))
            throw new ServiceException("A valid email address is required to create an account");

        AccountModel accountModel = dao.getByEmail(account.getEmail().trim().toLowerCase(Locale.ROOT));
        if (accountModel != null)
            throw new IllegalArgumentException("User with id " + account.getEmail() + " already exists");

        // if an email notification is to be sent, this implies an administrator is creating the password
        if (sendEmailNotification)
            authorization.expectAdmin(userId);

        // create new account
        accountModel = new AccountModel();
        accountModel.setCreationTime(new Date());
        accountModel.setLastUpdateTime(accountModel.getCreationTime());
        accountModel.setFirstName(account.getFirstName());
        accountModel.setLastName(account.getLastName());
        accountModel.setEmail(account.getEmail().trim().toLowerCase(Locale.ROOT));
        accountModel.setDescription(account.getDescription());
        accountModel.setDisabled(!sendEmailNotification);
        account.setPhone(account.getPhone());
        String password = "";

        // check whether to generate password information if sending email notification
        if (!sendEmailNotification) {
            accountModel.setSalt(PasswordUtil.generateSalt());
            password = account.getPassword();
            if (StringUtil.isEmpty(password)) {
                // generate a temporary password if user doesn't specify password
                password = PasswordUtil.generateTemporaryPassword();
                accountModel.setUsingTempPassword(true);
            }
            try {
                accountModel.setPassword(PasswordUtil.encryptPassword(password, accountModel.getSalt()));
            } catch (UtilityException ue) {
                throw new ServiceException("Exception encrypting password", ue);
            }
        }

        Account newAccount = dao.create(accountModel).toDataObject();

        // send email to new user
        if (sendEmailNotification && !StringUtils.isBlank(password))
            sendAccountEmail(newAccount, password);

        return newAccount;
    }

    private void sendAccountEmail(Account newAccount, String password) {
        String subject = "Infiniti Health System Account Created";

        String stringBuilder = "Dear " +
                newAccount.getFirstName() + " " + newAccount.getLastName() +
                ", " +
                "\n\nA new account has been created for you on the Infiniti Health Technology Applications site." +
                "\nPlease use the following username and password to access the site." +
                "\n\nUser name: " +
                newAccount.getEmail() +
                "\nPassword: " +
                password +
                "\n\nPlease use the link below to login and finish setting up your account." +
                "\n\nLink: https://infinitihealth.tech\n\n\nThank you" +
                "\n\n\n----------------------------------------------------";

        NotificationTask notificationTask = new NotificationTask();
        notificationTask.addInformation(newAccount.getEmail(), subject, stringBuilder);
        TaskRunner.getInstance().runTask(notificationTask);
    }

    public boolean update(String userId, long id, Account transfer) {
        AccountModel accountModel = dao.get(id);
        if (!accountModel.getEmail().equalsIgnoreCase(userId))
            authorization.expectAdmin(userId);

        accountModel.setFirstName(transfer.getFirstName());
        accountModel.setLastName(transfer.getLastName());
        accountModel.setEmail(transfer.getEmail());
        accountModel.setLastUpdateTime(new Date());
        return dao.update(accountModel) != null;
    }

    /**
     * Updates the password for the specified user
     *
     * @param userId      unique identifier for user whose password is being changed
     * @param password    existing user password
     * @param newPassword new user password
     * @throws ServiceException        on exception updating the password
     * @throws AuthenticationException on exception authenticating with the existing password
     */
    public Account updatePassword(String userId, String password, String newPassword) throws ServiceException,
            AuthenticationException {
        Authenticator authenticator = new Authenticator();
        Account account = authenticator.authenticate(userId, password);
        if (account == null)
            throw new AuthenticationException("Could not validate existing password for user " + userId);

        try {
            AccountModel accountModel = dao.getByEmail(userId);
            if (StringUtil.isEmpty(accountModel.getSalt())) {
                accountModel.setSalt(PasswordUtil.generateSalt());
            }
            accountModel.setPassword(PasswordUtil.encryptPassword(newPassword, accountModel.getSalt()));
            accountModel.setLastUpdateTime(new Date());
            accountModel.setPasswordUpdatedTime(new Date());
            accountModel.setUsingTempPassword(false);
            dao.update(accountModel);
            account.setUsingTemporaryPassword(false);
            account.setLastUpdateTime(accountModel.getLastUpdateTime().getTime());
            return account;
        } catch (UtilityException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Resets the password for the user specified in the parameter. `
     * <p>
     * <p>
     * If a new password is specified, the system authenticates (using existing password information)
     * and does not send an email, otherwise, it generates a new password and sends it via email.
     *
     * @param userId   unique identifier for the user
     * @param transfer whether to send notification to the user after the password reset
     * @throws IllegalArgumentException if the user id is not associated with any registered user
     * @throws ServiceException         on exception resetting the user password
     */
    public Account resetPassword(String userId, Account transfer) throws ServiceException {
        AccountModel accountModel = dao.getByEmail(userId);

        if (accountModel == null) { /* Wrapping entire operation to bypass IF User ID is not valid */
            return null; /* Returns 404 */
        } else {
            //execute only if accountModel !null
            if (accountModel.getDisabled() == null || accountModel.getDisabled() || userId.equalsIgnoreCase(DEFAULT_ADMIN_USERID)) {
                Logger.error("Cannot reset password for account " + accountModel.getEmail());
                String errorMsg = "Cannot reset the password for this account";

                /* throw new ServiceException(errorMsg); -- Returns a 500 error */
                return null; /* Returns 404 - Protects administrator username*/
            }

            // check how long since last update request; it must be greater than the min password reset period
            if (accountModel.getPasswordUpdatedTime() != null && !accountModel.getUsingTempPassword()) {
                // now - updatedTime >= MIN_PASSWORD_RESET_PERIOD;
                long updatedTime = accountModel.getPasswordUpdatedTime().getTime();
                if (new Date(updatedTime + MIN_PASSWORD_RESET_PERIOD).after(new Date())) {
                    String errMsg = "Too many password reset attempts in the past few hours";
                    Logger.error(errMsg);
                    throw new ServiceException(errMsg);
                }
            }

            if (transfer != null && !StringUtil.isEmpty(transfer.getNewPassword())) {
                try {
                    return updatePassword(userId, transfer.getPassword(), transfer.getNewPassword());
                } catch (AuthenticationException e) {
                    throw new ServiceException(e);
                }
            }

            // generate a new random password and send
            String tempPassword = PasswordUtil.generateTemporaryPassword();
            boolean isNewReset = StringUtil.isEmpty(accountModel.getPassword());    // if this is a new password generation

            try {
                accountModel.setPassword(PasswordUtil.encryptPassword(tempPassword, accountModel.getSalt()));
            } catch (UtilityException ue) {
                throw new ServiceException("Exception encrypting password", ue);
            }

            accountModel.setUsingTempPassword(true);
            accountModel.setLastUpdateTime(new Date());
            accountModel.setPasswordUpdatedTime(new Date());
            accountModel = dao.update(accountModel);

            if (isNewReset) {
                sendAccountEmail(accountModel.toDataObject(), tempPassword);
                return accountModel.toDataObject();
            }

            String subject = "Your Infiniti Health password was reset";
            String name = accountModel.getFirstName();
            if (StringUtil.isEmpty(name)) {
                name = accountModel.getLastName();
                if (StringUtil.isEmpty(name))
                    name = accountModel.getEmail();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy 'at' HH:mm aaa, z");

            String builder = "Dear " + name + ",\n\n" +
                    "The password for your Infiniti Health" +
                    " account (" + userId + ") was reset on " +
                    dateFormat.format(new Date()) + ". Your new temporary password is\n\n" +
                    tempPassword + "\n\n" +
                    "Please use the link below to login" +
                    "\n\nLink: https:\\infinitihealth.tech\n\n\nThank you" +
                    "\n\n\n----------------------------------------------------";

            NotificationTask notificationTask = new NotificationTask();
            notificationTask.addInformation(accountModel.getEmail(), subject, builder);
            TaskRunner.getInstance().runTask(notificationTask);
            return accountModel.toDataObject();
        }
    }

    public boolean isAdministrator(String userId) {
        AccountModel accountModel = dao.getByEmail(userId);
        return accountModel != null && accountModel.getRoles() != null && accountModel.getRoles().contains(AccountRole.ADMINISTRATOR);
    }

    public Account get(String userId, String uid) {
        AccountModel accountModel;

        try {
            accountModel = dao.get(Long.decode(uid));
        } catch (NumberFormatException e) {
            // actual user id
            accountModel = dao.getByEmail(uid);
        }

        if (accountModel == null)
            return null;

        if (!accountModel.getEmail().equalsIgnoreCase(userId))
            authorization.expectAdmin(userId);

        return accountModel.toDataObject();
    }

    public boolean accountExists(String userId) {
        if (StringUtil.isEmpty(userId))
            return false;

        return dao.getByEmail(userId.toLowerCase()) != null;
    }

    public Results<Account> retrieve(String userId, int start, int limit, boolean asc, String prop, String filter) {
        new AccountAuthorization().expectAdmin(userId);
        List<AccountModel> accountModels = dao.retrieveAccounts(start, limit, asc, prop, filter);
        if (accountModels == null)
            return new Results<>();

        Results<Account> data = new Results<>();

        for (AccountModel accountModel : accountModels) {
            Account transfer = accountModel.toDataObject();
            data.getRequested().add(transfer);
        }
        data.setAvailable(dao.getAccountTotal(filter));
        return data;
    }

    public List<Account> getAccountsByRole(String userId, AccountRole role) {
        authorization.expectAdmin(userId);

        List<AccountModel> accountModels = dao.getByRole(role);
        return accountModels.stream().map(AccountModel::toDataObject).collect(Collectors.toList());
    }

    public boolean setDisabled(long id, boolean disable) {
        authorization.expectAdmin(this.userId);
        AccountModel accountModel = DAOFactory.getAccountDAO().get(id);
        if (accountModel == null)
            return false;

        // if enabling and account is using a temporary password
        // then it is approved after vetting
        if (!disable && accountModel.getUsingTempPassword() != null && accountModel.getUsingTempPassword()) {
            String tempPassword = PasswordUtil.generateTemporaryPassword();

            try {
                accountModel.setPassword(PasswordUtil.encryptPassword(tempPassword, accountModel.getSalt()));
            } catch (UtilityException ue) {
                throw new ServiceException("Exception encrypting password", ue);
            }

            sendAccountEmail(accountModel.toDataObject(), tempPassword);
        }

        accountModel.setDisabled(disable);
        return dao.update(accountModel) != null;
    }
}
