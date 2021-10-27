package org.ih.account;

import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.dto.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

class AccountsTest {

    @BeforeEach
    void setUp() {
        HibernateConfiguration.initializeMock();
        HibernateConfiguration.beginTransaction();
    }

    @Test
    public void testCreateAccounts() {
        Accounts accounts = new Accounts();
        Account account = new Account();
        account.setAdministrator(false);
        account.setEmail("test@example.com");
        account.setFirstName("Test");
        account.setLastName("Example");

        account = accounts.createAccount(null, account, false);
        Assert.assertNotNull(account);

        Assert.assertTrue(accounts.accountExists(account.getEmail()));
    }

    @AfterEach
    void tearDown() {
        HibernateConfiguration.commitTransaction();
    }
}