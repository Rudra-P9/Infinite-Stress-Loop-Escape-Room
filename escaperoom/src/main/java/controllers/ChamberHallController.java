package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import com.escape.model.EscapeRoomFacade;

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

    private EscapeRoomFacade facade;
    private Timeline timerTimeline;

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

    }

    private void startTimerUpdate() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();
        updateTimer(); // Initial update
    }

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
    }

    @FXML
    private void onAcknowledge(MouseEvent event) {
        showDoors();
    }

    @FXML
    private void onInfo(MouseEvent event) {
        showIntro();
    }

    private void showIntro() {
        if (introOverlay != null)
            introOverlay.setVisible(true);
        if (doorOverlay != null)
            doorOverlay.setVisible(false);
        if (infoButton != null)
            infoButton.setVisible(false); // Hide info button while intro is up
    }

    private void showDoors() {
        if (introOverlay != null)
            introOverlay.setVisible(false);
        if (doorOverlay != null)
            doorOverlay.setVisible(true);
        if (infoButton != null)
            infoButton.setVisible(true); // Show info button when doors are visible
    }

    @FXML
    private void handleDoorHover(MouseEvent event) {
        if (doorNameLabel == null)
            return;

        Object source = event.getSource();
        if (source == door1Button) {
            doorNameLabel.setText("CALIBRATION HALL");
        } else if (source == door2Button) {
            doorNameLabel.setText("FRAGMENT CORRIDOR");
        } else if (source == door3Button) {
            doorNameLabel.setText("SYNC CORE");
        }
    }

    @FXML
    private void handleDoorExit(MouseEvent event) {
        if (doorNameLabel != null) {
            doorNameLabel.setText("");
        }
    }

    @FXML
    private void handleDoor1(MouseEvent event) {
        try {
            com.escape.App.setRoot("RoomOneIntro");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load RoomOneIntro: " + e.getMessage());
        }
    }

    @FXML
    private void handleDoor2(MouseEvent event) {
        System.out.println("Door 2 clicked");
        try {
            com.escape.App.setRoot("FragmentCorridor");
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FragmentCorridor: " + e.getMessage());
        }
    }

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

}
