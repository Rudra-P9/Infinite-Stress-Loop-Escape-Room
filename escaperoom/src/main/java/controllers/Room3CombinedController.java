package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.escape.App;

/**
 * Controller for the Room 3 main menu.
 * Handles showing and hiding the intro dialog and opening the three vault
 * hotspots.
 * 
 * @author Rudra Patel
 * @author Kirtan Patel
 */
public class Room3CombinedController implements Initializable {

    @FXML
    private ImageView bgImage;
    @FXML
    private Group dialogRoot;
    @FXML
    private Button infoButtonRm3;
    @FXML
    private Button acknowledgeButtonRm3;

    @FXML
    private Button leftVaultBtn;
    @FXML
    private Button rightVaultBtn;
    @FXML
    private Button coreBtn;

    @FXML
    private Group hotspotGroup;

    @FXML
    private Button backBtn;

    /**
     * Runs when the scene first loads.
     * Makes the dialog visible and prints some debug information.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.facade = App.gameFacade;
        startTimerUpdate();

        // Check if intro has been seen before
        if (facade != null && facade.isRoom3IntroSeen()) {
            // Hide intro, show hotspots
            if (dialogRoot != null) {
                dialogRoot.setVisible(false);
                dialogRoot.setMouseTransparent(true);
            }
            if (hotspotGroup != null) {
                hotspotGroup.toFront();
            }
        } else {
            // Show intro on first visit
            if (dialogRoot != null) {
                dialogRoot.setVisible(true);
                dialogRoot.setMouseTransparent(false);
            }
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }

        System.out.println("Room3CombinedController initialized.");
        System.out.println(" dialogRoot = " + (dialogRoot != null));
        System.out.println(" infoButtonRm3 = " + (infoButtonRm3 != null));
        System.out.println(" acknowledgeButtonRm3 = " + (acknowledgeButtonRm3 != null));
        System.out.println(" leftVaultBtn = " + (leftVaultBtn != null));
        System.out.println(" rightVaultBtn = " + (rightVaultBtn != null));
        System.out.println(" coreBtn = " + (coreBtn != null));
        System.out.println(" hotspotGroup = " + (hotspotGroup != null));
        System.out.println(" dialog visible = " + (dialogRoot != null && dialogRoot.isVisible()));

        // Update core button state based on collected letters
        updateCoreButtonState();
    }

    /**
     * Called when the user clicks the ACKNOWLEDGE button.
     * Hides the intro dialog and enables the hotspots behind it.
     */
    @FXML
    private void onAcknowledgeRm3(MouseEvent event) {
        System.out.println("ACK pressed, hiding intro overlay.");

        // Mark intro as seen
        if (facade != null) {
            facade.setRoom3IntroSeen(true);
        }

        if (dialogRoot != null) {
            dialogRoot.setVisible(false);
            dialogRoot.setMouseTransparent(true);
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }

        if (hotspotGroup != null) {
            hotspotGroup.toFront();
        }
    }

    /**
     * Called when the user clicks the ? button.
     * Shows the intro dialog again.
     */
    @FXML
    private void onInfoRm3(MouseEvent event) {
        System.out.println("Info pressed, showing intro overlay.");

        if (dialogRoot != null) {
            dialogRoot.setVisible(true);
            dialogRoot.setMouseTransparent(false);
            dialogRoot.toFront();
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }
    }

    /**
     * Opens Puzzle 4 when the left vault hotspot is clicked.
     */
    @FXML
    private void onLeftVaultClicked(MouseEvent event) {
        System.out.println("Left vault clicked - open puzzle A");
        try {
            com.escape.App.setRoot("Room3Puzzle4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens Puzzle 5 when the right vault hotspot is clicked.
     * (Navigation added when Puzzle 5 is finished.)
     */
    @FXML
    private void onRightVaultClicked(MouseEvent event) {
        System.out.println("Right vault clicked - open puzzle B");
        try {
            com.escape.App.setRoot("Room3Puzzle5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the final core puzzle when the center hotspot is clicked.
     */
    @FXML
    private void onCoreClicked(MouseEvent event) {
        if (facade != null && !facade.canAccessFinalPuzzle()) {
            System.out.println("Core locked - collect all 5 letters first (R, E, A, L, M)");
            return;
        }
        System.out.println("Core clicked - open final puzzle");
        System.out.println("Event source: " + event.getSource());
        System.out.println("Event target: " + event.getTarget());

        try {
            System.out.println("Attempting to navigate to FinalPuzzle...");
            com.escape.App.setRoot("FinalPuzzle");
            System.out.println("Navigation successful!");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to navigate to FinalPuzzle!");
            e.printStackTrace();
        }
    }

    @FXML
    private javafx.scene.control.Label timerLabel;
    private com.escape.model.EscapeRoomFacade facade;
    private javafx.animation.Timeline timerTimeline;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;

    private void startTimerUpdate() {
        timerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), event -> updateTimer()));
        timerTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timerTimeline.play();
        updateTimer();
    }

    private void updateTimer() {
        if (facade != null && timerLabel != null) {
            int remainingSeconds = facade.getTimeRemaining();
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            if (remainingSeconds < 60) {
                timerLabel.setTextFill(javafx.scene.paint.Color.RED);
            } else {
                timerLabel.setTextFill(javafx.scene.paint.Color.LIME);
            }
        }
        updateProgress();
    }

    private void updateProgress() {
        if (facade != null && progressBar != null && progressLabel != null) {
            int percentage = facade.getProgressPercentage();
            progressBar.setProgress(percentage / 100.0);
            progressLabel.setText(percentage + "%");
        }
    }

    /**
     * Updates the visual state of the core button based on collected letters.
     * Core button is disabled until all 5 letters are collected.
     */
    private void updateCoreButtonState() {
        if (facade == null)
            return;

        // Core button - requires all 5 letters (R, E, A, L, M)
        if (coreBtn != null) {
            boolean canAccess = facade.canAccessFinalPuzzle();
            coreBtn.setDisable(!canAccess);
            if (!canAccess) {
                coreBtn.setStyle("-fx-opacity: 0.5;");
            }
        }
    }

    @FXML
    private void onBack(MouseEvent event) throws Exception {
        // Go to the main screen
        App.setRoot("ChamberHall");
        System.out.println("Returning to Chamber Hall from Room 3 Combined");
    }
}
