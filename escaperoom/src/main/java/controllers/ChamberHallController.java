package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.escape.model.EscapeRoomFacade;
import com.escape.util.SaveAndQuitHandler;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * Controller for the Game Intro screen.
 * Handles the intro story, timer start, and door selection.
 * 
 * @author Rudra Patel
 */
public class ChamberHallController implements Initializable {

    @FXML
    private Group introOverlay;

    @FXML
    private Group doorOverlay;

    @FXML
    private Button acknowledgeButton;

    @FXML
    private Button infoButton;

    @FXML
    private Label timerLabel;

    @FXML
    private Label doorNameLabel;

    @FXML
    private Button door1Button;

    @FXML
    private Button door2Button;

    @FXML
    private Button door3Button;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    private EscapeRoomFacade facade;
    private Timeline timerTimeline;


     /**
     * Initializes the Chamber Hall:
     * - Starts a new game OR resumes existing one
     * - Starts the UI timer
     * - Determines whether to show the intro screen
     * - Updates the visual lock state of doors
     *
     * @param url ignored
     * @param rb ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Check if we already have a running game
            if (com.escape.App.gameFacade == null) {
                // NEW GAME START
                facade = new EscapeRoomFacade();
                com.escape.App.gameFacade = facade; // Save to global state

                // Pass global state to the facade
                if (com.escape.App.currentUser != null) {
                    facade.setCurrentUser(com.escape.App.currentUser);
                }

                // Start with the selected difficulty
                facade.startGame(com.escape.App.currentDifficulty);
                System.out.println("New Game started. Timer running.");

                // Show intro, hide doors initially
                showIntro();
            } else {
                // RESUMING GAME
                facade = com.escape.App.gameFacade;
                System.out.println("Resuming existing game. Timer continuing. Remaining: " + facade.getTimeRemaining());

                // Skip intro, show doors immediately
                showDoors();
            }

            // Always start the UI timer update (it reads from the facade)
            startTimerUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing ChamberHallController: " + e.getMessage());
        }

        // Update door lock states based on collected letters
        updateDoorLockStates();
    }

     /**
     * Starts the visual timer updater.
     * This does not control the countdown itself — only displays it.
     */
    private void startTimerUpdate() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();
        updateTimer(); // Initial update
    }

     /**
     * Updates the timer label every second.
     * Also updates timer color and progress bar.
     */
    private void updateTimer() {
        if (facade != null && timerLabel != null) {
            int remainingSeconds = facade.getTimeRemaining();
            // System.out.println("DEBUG: updateTimer remaining=" + remainingSeconds); //
            // excessive logging, maybe only once?
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

            // Optional: Change color if time is running low
            if (remainingSeconds < 60) {
                timerLabel.setTextFill(javafx.scene.paint.Color.RED);
            } else {
                timerLabel.setTextFill(javafx.scene.paint.Color.LIME); // Or whatever default color
            }
        }

        // Update progress bar
        updateProgress();
    }

     /**
     * Updates the progress bar and percentage text from the facade.
     */
    private void updateProgress() {
        if (facade != null && progressBar != null && progressLabel != null) {
            int percentage = facade.getProgressPercentage();
            progressBar.setProgress(percentage / 100.0);
            progressLabel.setText(percentage + "%");
        }
    }

    /**
     * Updates the visual state of doors based on collected letters.
     * Locked doors are disabled and styled with reduced opacity.
     */
    private void updateDoorLockStates() {
        if (facade == null)
            return;

        // Door 1 - Lock when player has letter 'E' (Room One complete)
        if (door1Button != null) {
            boolean locked = facade.isDoorOneLocked();
            // Don't disable so hover events still work
            if (locked) {
                door1Button.setStyle("-fx-cursor: default; -fx-background-color: transparent;");
            } else {
                door1Button.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
            }
        }

        // Door 2 - Lock when player has letters 'A' and 'M' (Fragment Corridor
        // complete)
        if (door2Button != null) {
            boolean locked = facade.isDoorTwoLocked();
            // Don't disable so hover events still work
            if (locked) {
                door2Button.setStyle("-fx-cursor: default; -fx-background-color: transparent;");
            } else {
                door2Button.setStyle("-fx-cursor: hand; -fx-background-color: transparent;");
            }
        }

        // Door 3 - Always accessible (leads to Room3Combined)
    }

     /**
     * Triggered when clicking the "Acknowledge" button.
     * Hides the intro and shows the doors.
     */
    @FXML
    private void onAcknowledge(MouseEvent event) {
        showDoors();
    }

    /**
     * Triggered when clicking the info button.
     * Shows the intro overlay again.
     */
    @FXML
    private void onInfo(MouseEvent event) {
        showIntro();
    }

    /**
     * Shows the intro overlay and hides the doors.
     */
    private void showIntro() {
        if (introOverlay != null)
            introOverlay.setVisible(true);
        if (doorOverlay != null)
            doorOverlay.setVisible(false);
        if (infoButton != null)
            infoButton.setVisible(false); // Hide info button while intro is up
    }

    /**
     * Shows the door selection overlay and hides the intro.
     */
    private void showDoors() {
        if (introOverlay != null)
            introOverlay.setVisible(false);
        if (doorOverlay != null)
            doorOverlay.setVisible(true);
        if (infoButton != null)
            infoButton.setVisible(true); // Show info button when doors are visible
    }

    /**
     * Displays the name of the hovered door.
     */
    @FXML
    private void handleDoorHover(MouseEvent event) {
        Button source = (Button) event.getSource();
        String doorName = "";

        if (source == door1Button) {
            if (facade != null && facade.isDoorOneLocked()) {
                doorName = "Room Completed";
            } else {
                doorName = "Calibration Hall";
            }
        } else if (source == door2Button) {
            if (facade != null && facade.isDoorTwoLocked()) {
                doorName = "Room Completed";
            } else {
                doorName = "Fragment Corridor";
            }
        } else if (source == door3Button) {
            doorName = "Sync Core";
        }

        if (doorNameLabel != null) {
            doorNameLabel.setText(doorName);
        }
    }

    /**
     * Clears door name on mouse exit.
     */
    @FXML
    private void handleDoorExit(MouseEvent event) {
        if (doorNameLabel != null) {
            doorNameLabel.setText("");
        }
    }

    /**
     * Handles clicking on Door 1.
     * Loads RoomOneBoard if unlocked.
     */
    @FXML
    private void handleDoor1(MouseEvent event) {
        if (facade != null && facade.isDoorOneLocked()) {
            System.out.println("Door 1 is locked - Room One already completed");
            return;
        }
        try {
            com.escape.App.setRoot("RoomOneBoard");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load RoomOneBoard: " + e.getMessage());
        }
    }

     /**
     * Handles clicking on Door 2.
     * Loads FragmentCorridor if unlocked.
     */
    @FXML
    private void handleDoor2(MouseEvent event) {
        if (facade != null && facade.isDoorTwoLocked()) {
            System.out.println("Door 2 is locked - Fragment Corridor already completed");
            return;
        }
        System.out.println("Door 2 clicked");
        try {
            com.escape.App.setRoot("FragmentCorridor");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FragmentCorridor: " + e.getMessage());
        }
    }

    /**
     * Handles clicking on Door 3.
     * Always accessible. Loads Room3Combined.
     */
    @FXML
    private void handleDoor3(MouseEvent event) {
        System.out.println("Door 3 clicked");
        try {
            com.escape.App.setRoot("Room3Combined"); // loads /com/escape/Room3Combined.fxml
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load Room3Combined: " + e.getMessage());
        }
    }

     /**
     * Opens the inventory screen.
     */
    @FXML
    private void openInventory(MouseEvent event) {
        try {
            com.escape.App.setRoot("Inventory");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to open Inventory: " + e.getMessage());
        }
    }
    
     /**
     * Saves the game and exits to desktop.
     * Triggered by the EXIT button.
     */
    @FXML
    private void saveAndExit(MouseEvent event) {
        System.out.println("EXIT button clicked - initiating save and quit");
        SaveAndQuitHandler.saveAndQuit();
    }

}
