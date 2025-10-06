package com.escape.model;

import java.util.UUID;

/**
 * Abstract base class representing a user in the escape game system.
 * Each user has a unique ID, login credentials, and an inventory.
 * Extends the {@code Accounts} class to inherit account-related functionality.
 * 
 * @author Jacob Kinard
 */
public abstract class User {

    public UUID userID;
    private String username;
    private String password;
    private Inventory inventory;

    /**
     * Constructs a new User with the specified credentials and inventory.
     *
     * @param userID    the unique identifier for the user
     * @param username  the username for login
     * @param password  the password for login
     * @param inventory the inventory associated with the user
     */
    public User(UUID userID, String username, String password,Inventory inventory) {
        this.inventory = inventory;
        this.password = password;
        this.username = username;
        this.userID = userID;
    }

    /**
     * Logs the user in using the provided credentials.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     */
    public void login(String username, String password) {}

    /**
     * Checks whether the provided credentials match the user's stored credentials.
     *
     * @param username the username to verify
     * @param password the password to verify
     */
    public void checkCredentials(String username, String password) {} // TODO

    /**
     * Changes the user's username to the specified new value.
     *
     * @param newUsername the new username to assign
     */
    public void changeUsername(String newUsername) {}

    /**
     * Logs the user out of the system.
     */
    public void logout() {}
}