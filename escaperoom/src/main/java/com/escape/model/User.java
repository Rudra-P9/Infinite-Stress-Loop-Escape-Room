package com.escape.model;

import java.util.UUID;
import java.util.ArrayList;

/**
 * Abstract base class representing a user in the escape game system.
 * Each user has a unique ID, login credentials, and an inventory.
 * Extends the {@code Accounts} class to inherit account-related functionality.
 * 
 * @author Jacob Kinard
 */
public class User {
    /**
     * Returns the username of this user.
     * @return the username
     */
    public String getUser() {
        return this.username;
    }

    public UUID userID;
    private String username;
    private String password;
    private Inventory inventory;

    /**
     * Returns the username (same as getUser but clearer name for writers/loaders).
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the user's password (used by the writer; consider removing in production).
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Constructs a new User with the specified credentials and inventory.
     *
     * @param userID    the unique identifier for the user
     * @param username  the username for login
     * @param password  the password for login
     * @param inventory the inventory associated with the user
     */
    public User(UUID userID, String username, String password) {
        this.password = password;
        this.username = username;
        this.userID = userID;
        // Initialize a default inventory
        this.inventory = new Inventory(5);
    }

    /**
     * Logs the user in using the provided credentials.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     */
    public void login(String username, String password) {

    }

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

    /*
     * Inventory accessors. We keep a convenience method named
     * getCollectedLetters() for compatibility with existing code that
     * expects an ArrayList of strings (delegates to Inventory.getItems()).
     */
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ArrayList<String> getCollectedLetters() {
        return inventory == null ? new ArrayList<>() : inventory.getItems();
    }

    /**
     * Adds a collected letter to the user's inventory.
     * Returns true if the item was added (capacity permitting).
     */
    public boolean addCollectedLetter(String letter) {
        if (letter == null) return false;
        if (this.inventory == null) this.inventory = new Inventory(26);
        if (this.inventory.hasItem(letter)) return false; // avoid duplicates
        return this.inventory.addItem(letter);
    }
    
    /**
     * TESTING THE USER CLASS CODE BELOW
     */

     public static void main(String[] args){
        
     }

     private int score;

    /** Returns the user's current score. */
    public int getScore() {
        return score;
    }

    /** Sets the user's score. */
    public void setScore(int score) {
       this.score = score;
    }
}