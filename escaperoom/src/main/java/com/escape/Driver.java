package com.escape;
import java.util.UUID;
import com.escape.model.UI;

/**
 * Driver class to start the Escape Room application.
 * Initializes the UI and starts the main application loop.
 * 
 * @author Rudra Patel
 */
public class Driver {

    // Attribute
    private UI ui;

    // Constructor
    public Driver() {
        ui = new UI();
    }

    // Methods
    public void start() {
        ui.run(); // start the main UI
    }

    //get random UUID
    public static UUID getUUID() {
        return UUID.randomUUID();
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.start();
    }
}