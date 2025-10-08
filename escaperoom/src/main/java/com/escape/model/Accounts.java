package com.escape.model;

import java.util.ArrayList;

import com.escape.Driver;

/**
 * Singleton class that manages user accounts in the escape game system.
 * Provides methods to create, delete, and retrieve user accounts.
 * 
 * @author Jacob Kinard
 */
public class Accounts {
    

    private ArrayList<User> accounts = new ArrayList<>();
    private static Accounts instance;

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
        if (instance == null) {
            instance = new Accounts();
        }
        return instance;
    }

    /**
     * Creates a new user account with the specified credentials.
     *
     * @param username the desired username
     * @param password the desired password
     */
    public void createAccount(String username, String password) {
        if (getUser(username) != null) {
            System.out.println("Account already exists for username: " + username);
            return;
        }
        accounts.add(new User(Driver.getUUID(),username, password));
        Accounts.toString("Account created for username: " + username);
        
    }
    /*
     * prints input values to console
     */
    private static void toString(String input){
        System.out.println(input);
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
    public User getUser(String username) {
        for (User user : accounts) {
            if (user.getUser().equals(username)) {
                return user;
            }
        }
        return null;
    }
    /**
     * Main method for testing account creation and writing to JSON.
     */
    public static void main(String[] args) {
        // Create Accounts singleton
        Accounts accounts = Accounts.getInstance();
        // Create a user account
        accounts.createAccount("testuser", "testpass");
        // Collect users into a list for writing
        ArrayList<User> users = new ArrayList<>();
        User user = accounts.getUser("testuser");
        if (user != null) {
            users.add(user);
        }
        // Write users to JSON
        GameDataWriter writer = new GameDataWriter();
        writer.saveUsers(users);
        System.out.println("Test user created and written to json/playerData.json");
    }
}