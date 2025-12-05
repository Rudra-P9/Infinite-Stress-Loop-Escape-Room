package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.escape.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Fragment Corridor Room Two screen.
 * Handles directional button puzzle: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT.
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

    /** The target sequence: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT */
    private static final String[] SEQUENCE = {"UP", "LEFT", "DOWN", "RIGHT", "RIGHT", "DOWN", "UP", "LEFT"};
    
    /** Current position in the sequence (0-7, 8 = complete) */
    private int sequencePosition = 0;

    /**
     * Initializes the controller.
     * Hides the Continue elements and error label until the puzzle is solved.
     * 
     * @param url the location used to resolve relative paths for the root object, or null
     * @param resourceBundle the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hide Continue elements until puzzle is solved
        if (ContinueLabel != null) ContinueLabel.setVisible(false);
        if (ContinueButton != null) ContinueButton.setVisible(false);
        if (IncorrectLabel != null) IncorrectLabel.setVisible(false);
        // Hide arrow cover initially so user can see the sequence
        if (ArrowHider != null) ArrowHider.setVisible(false);
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
     * @param direction the direction that was clicked ("UP", "DOWN", "LEFT", "RIGHT")
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

    /**
     * Resets the puzzle progress.
     * Sets the sequence position back to the beginning and hides the arrow cover
     * so the user can re-memorize the sequence.
     */
    private void resetProgress() {
        sequencePosition = 0;
        // Hide arrow cover so user can see the sequence again
        if (ArrowHider != null) ArrowHider.setVisible(false);
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
                if (IncorrectLabel != null) IncorrectLabel.setVisible(false);
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
        
        if (ContinueLabel != null) ContinueLabel.setVisible(true);
        if (ContinueButton != null) ContinueButton.setVisible(true);
        
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

}
