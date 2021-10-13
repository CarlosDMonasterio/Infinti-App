package org.ih.account;

import com.opencsv.CSVReader;
import org.ih.common.exception.ServiceException;
import org.ih.common.exception.UtilityException;
import org.ih.common.logging.Logger;
import org.ih.dao.DAOFactory;
import org.ih.dao.hibernate.AccountDAO;
import org.ih.dao.model.AccountModel;
import org.ih.dao.model.Group;
import org.ih.dto.Account;
import org.ih.notification.NotificationTask;
import org.ih.task.TaskRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Group> groups = accountModel.getGroups();
        if (groups.isEmpty())
            return result;

        List<AccountModel> matches = dao.getMatchingGroupMembers(groups, token, limit);
        result.addAll(matches.stream().map(AccountModel::toDataObject).collect(Collectors.toList()));
        return result;
    }

    public void importFile(InputStream inputStream, boolean notifyUsers) {
        // expected file format is
        // "Last Name", "First Name", "Email", "Phone Number", "Street", "City", "State", "Zip"
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));

        int i = 0;
        AccountDAO accountDAO = DAOFactory.getAccountDAO();
        NotificationTask notificationTask = new NotificationTask();

        for (String[] next : reader) {
            if (next.length != 8) {
                throw new IllegalArgumentException("Cannot import file. Improper format");
            }

            if (++i == 1)
                continue;

            String lastName = next[0];
            String firstName = next[1];
            String email = next[2];
            String phone = next[3];
            String address = next[4] + ", " + next[5] + ", " + next[6] + ", " + next[7];

            AccountModel model = DAOFactory.getAccountDAO().getByEmail(next[1]);
            if (model != null) {
                Logger.error("User with email: " + email + " already exists");
                continue;
            }

            Logger.info("Creating account for user: " + email);
            model = new AccountModel();
            model.setEmail(email);
            model.setFirstName(firstName);
            model.setLastName(lastName);
            model.setPhone(phone);
            model.setAddress(address);
            model.setDisabled(false);
            model.setSalt(PasswordUtil.generateSalt());
            model.setCreationTime(new Date());
            model.setUsingTempPassword(true);
            String password = PasswordUtil.generateTemporaryPassword();

            try {
                model.setPassword(PasswordUtil.encryptPassword(password, model.getSalt()));
            } catch (UtilityException ue) {
                throw new ServiceException("Exception encrypting password", ue);
            }

            accountDAO.create(model);

            if (notifyUsers) {
                notificationTask.addInformation(email, getSubject(), getBody(firstName, lastName, email, password));
            }
        }

        if (notifyUsers) {
            TaskRunner.getInstance().runTask(notificationTask);
        }
    }

    public static String getSubject() {
        return "Nurse Quality Data Management System Account Created";
    }

    public static String getBody(String fName, String lName, String email, String password) {
        return "Dear " +
                fName + " " + lName +
                ", " +
                "\n\nA new account has been created for you on the InfinitiHealth Nurse Quality Data Management System." +
                "\nPlease use the following username and password to access the site." +
                "\n\nUser name: " +
                email +
                "\nPassword: " +
                password +
                "\n\nPlease use the link below to login and finish setting up your account." +
                "\n\nLink: https://infinitihealth.tech\n\n\nThank you" +
                "\n\n\n----------------------------------------------------";
    }
}
