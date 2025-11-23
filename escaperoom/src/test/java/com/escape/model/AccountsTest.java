package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class AccountsTest {

    private Accounts accounts;

    @Before
    public void setUp() {
        // Reset singleton instance before each test
        accounts = Accounts.getInstance();
        // Clear all accounts for clean test state
        ArrayList<User> allAccounts = accounts.getAccounts();
        for (User user : allAccounts) {
            accounts.deleteAccount(user.getUsername());
        }
    }

    /**
     * Tests that getInstance returns the same singleton instance.
     */
    @Test
    public void getInstance_shouldReturnSingletonInstance() {
        Accounts instance1 = Accounts.getInstance();
        Accounts instance2 = Accounts.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    /**
     * Tests that createAccount successfully creates a new user account.
     */
    @Test
    public void createAccount_withNewUsername_shouldCreateAccount() {
        String username = "newuser_" + System.currentTimeMillis();
        String password = "password123";

        accounts.createAccount(username, password, "test@example.com");
        User user = accounts.getUser(username);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    /**
     * Tests that createAccount does not create duplicate accounts with the same
     * username.
     */
    @Test
    public void createAccount_withDuplicateUsername_shouldNotCreateDuplicate() {
        String username = "duplicateuser";
        String password = "pass1";

        accounts.createAccount(username, password, "test@example.com");
        User firstUser = accounts.getUser(username);

        // Try to create duplicate
        accounts.createAccount(username, "pass2", "test2@example.com");
        User secondUser = accounts.getUser(username);

        assertNotNull(firstUser);
        assertNotNull(secondUser);
        assertSame(firstUser, secondUser);
    }

    /**
     * Tests that createAccount handles null username gracefully without throwing
     * exceptions.
     */
    @Test
    public void createAccount_withNullUsername_shouldHandleGracefully() {
        accounts.createAccount(null, "password", "test@example.com");
        // Should not throw exceptions
    }

    /**
     * Tests that createAccount handles null password gracefully without throwing
     * exceptions.
     */
    @Test
    public void createAccount_withNullPassword_shouldHandleGracefully() {
        accounts.createAccount("username", null, "test@example.com");
        // Should not throw exceptions
    }

    /**
     * Tests that createAccount handles both null username and password gracefully.
     */
    @Test
    public void createAccount_withBothNull_shouldHandleGracefully() {
        accounts.createAccount(null, null, "test@example.com");
        // Should not throw exceptions
    }

    /**
     * Tests that deleteAccount successfully removes an existing user account.
     */
    @Test
    public void deleteAccount_withExistingUser_shouldRemoveAccount() {
        String username = "deleteuser";
        accounts.createAccount(username, "password", "test@example.com");

        assertNotNull(accounts.getUser(username));

        accounts.deleteAccount(username);
        assertNull(accounts.getUser(username));
    }

    /**
     * Tests that deleteAccount handles nonexistent users gracefully without
     * throwing exceptions.
     */
    @Test
    public void deleteAccount_withNonexistentUser_shouldHandleGracefully() {
        accounts.deleteAccount("nonexistent");
        // Should not throw exceptions
    }

    /**
     * Tests that deleteAccount handles null username gracefully without throwing
     * exceptions.
     */
    @Test
    public void deleteAccount_withNullUsername_shouldHandleGracefully() {
        accounts.deleteAccount(null);
        // Should not throw exceptions
    }

    /**
     * Tests that getUser returns the correct User object for an existing username.
     */
    @Test
    public void getUser_withExistingUsername_shouldReturnUser() {
        String username = "existinguser";
        accounts.createAccount(username, "password", "test@example.com");

        User user = accounts.getUser(username);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    /**
     * Tests that getUser returns null for a nonexistent username.
     */
    @Test
    public void getUser_withNonexistentUsername_shouldReturnNull() {
        User user = accounts.getUser("nonexistent");
        assertNull(user);
    }

    /**
     * Tests that getUser returns null when given a null username.
     */
    @Test
    public void getUser_withNullUsername_shouldReturnNull() {
        User user = accounts.getUser(null);
        assertNull(user);
    }

    /**
     * Tests that getAccounts returns a safe copy of the accounts list.
     */
    @Test
    public void getAccounts_shouldReturnCopyOfAccountsList() {
        String username1 = "user1";
        String username2 = "user2";

        accounts.createAccount(username1, "pass1", "user1@example.com");
        accounts.createAccount(username2, "pass2", "user2@example.com");

        ArrayList<User> accountsList = accounts.getAccounts();

        assertNotNull(accountsList);
        assertEquals(2, accountsList.size());

        // Verify it's a copy by modifying returned list
        accountsList.clear();
        assertEquals(2, accounts.getAccounts().size());
    }

    /**
     * Tests that getAccounts returns an empty list when no accounts exist.
     */
    @Test
    public void getAccounts_whenEmpty_shouldReturnEmptyList() {
        ArrayList<User> accountsList = accounts.getAccounts();

        assertNotNull(accountsList);
        assertTrue(accountsList.isEmpty());
    }

    /**
     * Tests that getUserCaseInsensitive finds users regardless of case.
     */
    @Test
    public void getUserCaseInsensitive_withMatchingUsername_shouldReturnUser() {
        String username = "TestUser";
        accounts.createAccount(username, "password", "test@example.com");

        User user1 = accounts.getUserCaseInsensitive("testuser");
        User user2 = accounts.getUserCaseInsensitive("TESTUSER");
        User user3 = accounts.getUserCaseInsensitive("TestUser");

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);
        assertEquals(username, user1.getUsername());
        assertSame(user1, user2);
        assertSame(user1, user3);
    }

    /**
     * Tests that getUserCaseInsensitive returns null for a nonexistent username.
     */
    @Test
    public void getUserCaseInsensitive_withNonexistentUsername_shouldReturnNull() {
        User user = accounts.getUserCaseInsensitive("nonexistent");
        assertNull(user);
    }

    /**
     * Tests that getUserCaseInsensitive returns null when given a null username.
     */
    @Test
    public void getUserCaseInsensitive_withNullUsername_shouldReturnNull() {
        User user = accounts.getUserCaseInsensitive(null);
        assertNull(user);
    }

    /**
     * Tests that getUserCaseInsensitive returns null when the accounts list is
     * empty.
     */
    @Test
    public void getUserCaseInsensitive_whenAccountsEmpty_shouldReturnNull() {
        User user = accounts.getUserCaseInsensitive("anyuser");
        assertNull(user);
    }

    /**
     * Tests that multiple accounts can be created and maintained separately.
     */
    @Test
    public void multipleAccounts_shouldMaintainSeparateUsers() {
        accounts.createAccount("user1", "pass1", "user1@example.com");
        accounts.createAccount("user2", "pass2", "user2@example.com");
        accounts.createAccount("user3", "pass3", "user3@example.com");

        User user1 = accounts.getUser("user1");
        User user2 = accounts.getUser("user2");
        User user3 = accounts.getUser("user3");

        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(user3);

        assertNotSame(user1, user2);
        assertNotSame(user1, user3);
        assertNotSame(user2, user3);

        assertEquals(3, accounts.getAccounts().size());
    }

    /**
     * Tests that deleteAccount only deletes the specified user and leaves others
     * intact.
     */
    @Test
    public void deleteAccount_shouldOnlyDeleteSpecifiedUser() {
        accounts.createAccount("user1", "pass1", "user1@example.com");
        accounts.createAccount("user2", "pass2", "user2@example.com");
        accounts.createAccount("user3", "pass3", "user3@example.com");

        accounts.deleteAccount("user2");

        assertNotNull(accounts.getUser("user1"));
        assertNull(accounts.getUser("user2"));
        assertNotNull(accounts.getUser("user3"));
        assertEquals(2, accounts.getAccounts().size());
    }
}
