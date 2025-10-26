package com.escape.model;
import java.util.UUID;

/**
 * Driver class to start the Escape Room application.
 * Initializes the UI and starts the main application loop.
 * 
 * @author Rudra Patel
 */

public class Driver {

    private UI ui;
    /**
     * Constructor for the Driver class.
     * Initializes the UI instance.
     */
    public Driver() {
        ui = new UI();
    }

    /**
     * Starts the main application loop by running the UI.
     */
    public void start() {
        ui.run(); // start the main UI
    }

    /**
     * Generates a random UUID.
     * @return a randomly generated UUID
     */
    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.start();
    }
}