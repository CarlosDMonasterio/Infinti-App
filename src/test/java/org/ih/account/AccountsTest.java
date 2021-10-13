package org.ih.account;

import org.ih.dao.hibernate.HibernateConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountsTest {

    @BeforeEach
    void setUp() {
        HibernateConfiguration.initializeMock();
    }

    @Test
    public void testCreateAccounts() {
        Accounts accounts = new Accounts();
    }

    @AfterEach
    void tearDown() {
    }
}