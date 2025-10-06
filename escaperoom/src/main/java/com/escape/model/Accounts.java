package com.escape.model;

import java.util.ArrayList;

/**
 * Singleton class that manages user accounts in the escape game system.
 * Provides methods to create, delete, and retrieve user accounts.
 * 
 * @author Jacob Kinard
 */
public class Accounts {

    private ArrayList<User> account;
    private static Accounts accounts;

    /**
     * Private constructor to enforce singleton pattern.
     */
    
    private Accounts() {}

    /**
     * Returns the singleton instance of the Accounts manager.
     *
     * @return the shared Accounts instance
     */
    public static Accounts getInstance() {
        return accounts;
    }

    /**
     * Creates a new user account with the specified credentials.
     *
     * @param username the desired username
     * @param password the desired password
     */
    public void createAccount(String username, String password) {
    }

    /**
     * Deletes the user account associated with the specified username.
     *
     * @param username the username of the account to delete
     */
    public void deleteAccount(String username) {
    }

    /**
     * Retrieves the user account associated with the specified username.
     *
     * @param username the username of the account to retrieve
     * @return the User object (currently commented out)
     */
    public void /*User*/ getUser(String username) {
        
    }
}