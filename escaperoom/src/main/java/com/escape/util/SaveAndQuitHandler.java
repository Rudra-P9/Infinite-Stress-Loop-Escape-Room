package com.escape.util;

import com.escape.App;
import javafx.scene.control.Alert;

import java.io.IOException;

/**
 * Utility class to handle save and quit functionality across all rooms.
 * Saves complete game state including timer, room, difficulty, progress, and letters.
 * 
 * @author Rudra Patel
 */
public class SaveAndQuitHandler {

    /**
     * Saves the current user's complete game state, logs them out, and returns to the main menu.
     * 
     * @return true if the operation was successful, false otherwise
     */
    public static boolean saveAndQuit() {
        try {
            // Validate user is logged in
            if (App.currentUser == null) {
                System.out.println("[SaveAndQuit] No user logged in");
                showError("No user is currently logged in.");
                return false;
            }

            if (App.gameFacade == null) {
                System.out.println("[SaveAndQuit] No game facade available");
                showError("Game system is not initialized.");
                return false;
            }

            System.out.println("[SaveAndQuit] Starting save and quit for user: " + App.currentUser.getUsername());

            // 1. Save complete game state (timer, room, difficulty, progress, letters)
            App.gameFacade.saveGame();
            System.out.println("[SaveAndQuit] Game state saved successfully");

            // 2. Logout user
            App.gameFacade.logout();
            App.currentUser = null;
            App.currentDifficulty = null;
            
            // Clear the facade to force fresh state on next login
            App.gameFacade = null;
            
            System.out.println("[SaveAndQuit] User logged out successfully");

            // 3. Navigate to main menu
            App.setRoot("MainScreen");
            System.out.println("[SaveAndQuit] Returned to main menu");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("[SaveAndQuit] Failed to return to main menu: " + e.getMessage());
            showError("Failed to save and quit: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[SaveAndQuit] Unexpected error: " + e.getMessage());
            showError("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Quick save without logout (useful for auto-save functionality).
     */
    public static void quickSave() {
        if (App.gameFacade != null && App.currentUser != null) {
            App.gameFacade.saveGame();
            System.out.println("[QuickSave] Progress saved for " + App.currentUser.getUsername());
        }
    }
    
    /**
     * Shows an error dialog to the user.
     */
    private static void showError(String message) {
        try {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Save & Quit Error");
            errorAlert.setHeaderText("Failed to Save and Quit");
            errorAlert.setContentText(message);
            errorAlert.showAndWait();
        } catch (Exception e) {
            System.err.println("[SaveAndQuit] Failed to show error dialog: " + e.getMessage());
        }
    }
}
