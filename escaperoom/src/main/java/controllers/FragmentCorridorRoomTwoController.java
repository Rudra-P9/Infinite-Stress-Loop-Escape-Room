package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.escape.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Fragment Corridor Room Two screen.
 * Handles directional button puzzle: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP,
 * LEFT.
 * 
 * @author Jacob Kinard
 */
public class FragmentCorridorRoomTwoController implements Initializable {

    /** Button for UP direction input */
    @FXML
    private Button UP;

    /** Button for DOWN direction input */
    @FXML
    private Button DOWN;

    /** Button for LEFT direction input */
    @FXML
    private Button LEFT;

    /** Button for RIGHT direction input */
    @FXML
    private Button RIGHT;

    /** Label for continue button text */
    @FXML
    private Label ContinueLabel;

    /** Button to continue to next screen after puzzle completion */
    @FXML
    private Button ContinueButton;

    /** Label displayed when incorrect direction is clicked */
    @FXML
    private Label IncorrectLabel;

    /** ImageView to hide the arrow sequence when user starts inputting */
    @FXML
    private ImageView ArrowHider;

    /** Hint pane container */
    @FXML
    private AnchorPane hintPane;

    /** Label displaying the hint text */
    @FXML
    private Label hintText;

    /** ImageView for closing the hint */
    @FXML
    private ImageView closeHintArrow;

    /** ImageView for the hint note background */
    @FXML
    private ImageView hintNote;

    /** The target sequence: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT */
    private static final String[] SEQUENCE = { "UP", "LEFT", "DOWN", "RIGHT", "RIGHT", "DOWN", "UP", "LEFT" };

    /** Current position in the sequence (0-7, 8 = complete) */
    private int sequencePosition = 0;

    /**
     * Initializes the controller.
     * Hides the Continue elements and error label until the puzzle is solved.
     * 
     * @param url            the location used to resolve relative paths for the
     *                       root object, or null
     * @param resourceBundle the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hide hint pane initially
        if (hintPane != null) hintPane.setVisible(false);
        
        // Hide Continue elements until puzzle is solved
        if (ContinueLabel != null)
            ContinueLabel.setVisible(false);
        if (ContinueButton != null)
            ContinueButton.setVisible(false);
        if (IncorrectLabel != null)
            IncorrectLabel.setVisible(false);
        // Hide arrow cover initially so user can see the sequence
        if (ArrowHider != null)
            ArrowHider.setVisible(false);

        // Initialize Facade and Timer
        this.facade = App.gameFacade;
        startTimerUpdate();
    }

    /**
     * Shows the hint for the directional arrow puzzle.
     * Displays the sequence hint.
     * 
     * @param event the mouse event triggered by clicking the hint icon
     */
    @FXML
    private void showHint(MouseEvent event) {
        System.out.println("Hint clicked!");

        // Set hint text
        String hint = "The sequence is: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT";
        if (hintText != null) {
            hintText.setText(hint);
        }

        // Show hint pane with fade animation
        if (hintPane != null) {
            hintPane.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.seconds(0.6), hintPane);
            hintPane.setOpacity(0);
            ft.setToValue(1.0);
            ft.play();
        }

        if (hintText != null) hintText.setVisible(true);
        if (hintNote != null) hintNote.setVisible(true);
        if (closeHintArrow != null) closeHintArrow.setVisible(true);
    }

    /**
     * Hides the hint pane.
     * 
     * @param event the mouse event triggered by clicking the close arrow
     */
    @FXML
    private void hideHint(MouseEvent event) {
        if (hintPane != null) hintPane.setVisible(false);
        if (closeHintArrow != null) closeHintArrow.setVisible(false);
    }

    /**
     * Handles UP button click events.
     * Validates if UP is the correct next direction in the sequence.
     * 
     * @param event the mouse event triggered by clicking the UP button
     */
    @FXML
    void PressedUp(MouseEvent event) {
        handleDirectionClick("UP");
    }

    /**
     * Handles DOWN button click events.
     * Validates if DOWN is the correct next direction in the sequence.
     * 
     * @param event the mouse event triggered by clicking the DOWN button
     */
    @FXML
    void PressedDown(MouseEvent event) {
        handleDirectionClick("DOWN");
    }

    /**
     * Handles LEFT button click events.
     * Validates if LEFT is the correct next direction in the sequence.
     * 
     * @param event the mouse event triggered by clicking the LEFT button
     */
    @FXML
    void PressedLeft(MouseEvent event) {
        handleDirectionClick("LEFT");
    }

    /**
     * Handles RIGHT button click events.
     * Validates if RIGHT is the correct next direction in the sequence.
     * 
     * @param event the mouse event triggered by clicking the RIGHT button
     */
    @FXML
    void PressedRight(MouseEvent event) {
        handleDirectionClick("RIGHT");
    }

    /**
     * Handles Continue button click events.
     * Navigates back to ChamberHall after puzzle completion.
     * 
     * @param event the mouse event triggered by clicking Continue
     */
    @FXML
    void ContinueToNext(MouseEvent event) {
        try {
            App.setRoot("ChamberHall");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unified handler for all direction clicks.
     * Validates the clicked direction against the expected sequence position.
     * Advances on correct input or resets and shows error on incorrect input.
     * 
     * @param direction the direction that was clicked ("UP", "DOWN", "LEFT",
     *                  "RIGHT")
     */
    private void handleDirectionClick(String direction) {
        // Check if we've completed the sequence
        if (sequencePosition >= SEQUENCE.length) {
            return; // puzzle already solved
        }

        // Show arrow cover on first input to hide the sequence
        if (sequencePosition == 0 && ArrowHider != null) {
            ArrowHider.setVisible(true);
        }

        // Check if clicked direction matches expected position
        if (direction.equals(SEQUENCE[sequencePosition])) {
            // Correct direction - advance sequence
            sequencePosition++;

            // Check if puzzle is complete
            if (sequencePosition >= SEQUENCE.length) {
                showSuccess();
            }
        } else {
            // Wrong direction - reset progress and show error
            resetProgress();
            showError();
        }
    }

    @FXML
    private Label penaltyLabel;

    /**
     * Resets the puzzle progress.
     * Sets the sequence position back to the beginning and hides the arrow cover
     * so the user can re-memorize the sequence.
     */
    private void resetProgress() {
        // Apply penalty for incorrect direction
        if (facade != null) {
            facade.applyHintPenalty();
            updateTimer();
        }

        // Animate penalty label if present
        if (penaltyLabel != null) {
            penaltyLabel.setOpacity(1.0);
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(
                    javafx.util.Duration.seconds(2.0), penaltyLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        }

        sequencePosition = 0;
        // Hide arrow cover so user can see the sequence again
        if (ArrowHider != null)
            ArrowHider.setVisible(false);
    }

    /**
     * Shows an error message briefly when the user clicks the wrong direction.
     * Displays the error for 2 seconds then hides it automatically.
     */
    private void showError() {
        if (IncorrectLabel != null) {
            IncorrectLabel.setVisible(true);

            // Hide error message after 2 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                if (IncorrectLabel != null)
                    IncorrectLabel.setVisible(false);
            });
            pause.play();
        }
    }

    /**
     * Shows the Continue button with breathing animation when puzzle is solved.
     * Creates a pulsing scale animation to draw attention to the continue button.
     */
    private void showSuccess() {
        // Award the letter "M" to the user's inventory (from game.json)
        if (App.currentUser != null) {
            // Check if user already has the letter before adding
            if (!App.currentUser.getCollectedLetters().contains("M")) {
                boolean added = App.currentUser.addCollectedLetter("M");
                if (added) {
                    System.out.println("Letter 'M' added to inventory.");
                } else {
                    System.out.println("Failed to add letter 'M' - inventory may be full.");
                }
            } else {
                System.out.println("Letter 'M' already in inventory.");
            }
        }

        if (ContinueLabel != null)
            ContinueLabel.setVisible(true);
        if (ContinueButton != null)
            ContinueButton.setVisible(true);

        // Create breathing animation for Continue label
        if (ContinueLabel != null) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), ContinueLabel);
            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.setCycleCount(Timeline.INDEFINITE);
            scaleTransition.setAutoReverse(true);
            scaleTransition.play();
        }
    }

    @FXML
    private Label timerLabel;
    private com.escape.model.EscapeRoomFacade facade;
    private Timeline timerTimeline;

    private void startTimerUpdate() {
        timerTimeline = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(1), event -> updateTimer()));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();
        updateTimer(); // Initial update
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
    }

}
