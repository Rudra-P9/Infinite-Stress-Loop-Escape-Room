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
        // Also check persisted users on disk to avoid creating duplicates when
        // the in-memory list hasn't been populated from disk.
        try {
            GameDataLoader loader = new GameDataLoader();
            for (User u : loader.getUsers()) {
                if (u.getUsername() != null && u.getUsername().equals(username)) {
                    System.out.println("Account already exists for username (persisted): " + username);
                    return;
                }
            }
        } catch (Exception e) {
            // If loader fails, continue with creation (we don't want to block account creation)
            // but log for visibility.
            System.out.println("Warning: could not check persisted users: " + e.getMessage());
        }
        accounts.add(new User(Driver.getUUID(),username, password));
        Accounts.toString("Account created for username: " + username);
        
    }

    /**
     * Deletes the user account associated with the specified username.
     *
     * @param username the username of the account to delete
     */
    public void deleteAccount(String username) {
    User userToRemove = getUser(username);
        if (userToRemove != null) {
            accounts.remove(userToRemove);
            System.out.println("Account deleted for username: " + username);
        } else {
            System.out.println("No account found for username: " + username);
        }
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
     * Returns a copy of the internal accounts list for external use (safe copy).
     * @return list of users
     */
    public ArrayList<User> getAccounts() {
        return new ArrayList<>(accounts);
    }

    /*
     * prints input values to console
     */
    private static void toString(String input){
        System.out.println(input);
    }

    /**
     * Main method for testing account creation and writing to JSON.
     */
    public static void main(String[] args) {
        // Create Accounts singleton
        Accounts accounts = Accounts.getInstance();
        String username = "testuser";
        String password = "testpass";

        // Check persisted users first to avoid duplicates
        GameDataLoader loader = new GameDataLoader();
        boolean exists = false;
        try {
            for (User u : loader.getUsers()) {
                if (u.getUsername() != null && u.getUsername().equals(username)) {
                    exists = true;
                    break;
                }
            }
        } catch (Exception e) {
            // If loader fails (file missing), we'll proceed to create user
            System.out.println("Warning: could not read existing users; proceeding to create. " + e.getMessage());
        }

        if (exists) {
            System.out.println("Account already exists for username: " + username + " was not saved.");
            return;
        }

        // Create and persist the user
        accounts.createAccount(username, password);
        ArrayList<User> users = new ArrayList<>();
        User user = accounts.getUser(username);
        if (user != null) users.add(user);
        GameDataWriter writer = new GameDataWriter();
        writer.saveUsers(users);
        System.out.println("Test user created and written to json/test.json");
        
    }
}