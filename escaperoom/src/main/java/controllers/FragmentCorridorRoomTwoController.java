package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Fragment Corridor Room Two screen.
 * Handles directional button puzzle: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT.
 * 
 * @author Jacob Kinard
 */
public class FragmentCorridorRoomTwoController implements Initializable {

    @FXML
    private Button UP;

    @FXML
    private Button DOWN;

    @FXML
    private Button LEFT;

    @FXML
    private Button RIGHT;

    @FXML
    private Label ContinueLabel;

    @FXML
    private Button ContinueButton;

    @FXML
    private Label IncorrectLabel;

    /** The target sequence: UP, LEFT, DOWN, RIGHT, RIGHT, DOWN, UP, LEFT */
    private static final String[] SEQUENCE = {"UP", "LEFT", "DOWN", "RIGHT", "RIGHT", "DOWN", "UP", "LEFT"};
    
    /** Current position in the sequence (0-7, 8 = complete) */
    private int sequencePosition = 0;

    /**
     * Initializes the controller.
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
    }

    /**
     * Handles UP button click events.
     * 
     * @param event the mouse event triggered by clicking the UP button
     */
    @FXML
    void PressedUp(MouseEvent event) {
        handleDirectionClick("UP");
    }

    /**
     * Handles DOWN button click events.
     * 
     * @param event the mouse event triggered by clicking the DOWN button
     */
    @FXML
    void PressedDown(MouseEvent event) {
        handleDirectionClick("DOWN");
    }

    /**
     * Handles LEFT button click events.
     * 
     * @param event the mouse event triggered by clicking the LEFT button
     */
    @FXML
    void PressedLeft(MouseEvent event) {
        handleDirectionClick("LEFT");
    }

    /**
     * Handles RIGHT button click events.
     * 
     * @param event the mouse event triggered by clicking the RIGHT button
     */
    @FXML
    void PressedRight(MouseEvent event) {
        handleDirectionClick("RIGHT");
    }

    /**
     * Handles Continue button click events.
     * 
     * @param event the mouse event triggered by clicking Continue
     */
    @FXML
    void ContinueToNext(MouseEvent event) {
        // Add navigation logic here
        System.out.println("Continue to next screen");
    }

    /**
     * Unified handler for all direction clicks.
     * Validates the clicked direction against the expected sequence position.
     * 
     * @param direction the direction that was clicked ("UP", "DOWN", "LEFT", "RIGHT")
     */
    private void handleDirectionClick(String direction) {
        // Check if we've completed the sequence
        if (sequencePosition >= SEQUENCE.length) {
            return; // puzzle already solved
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
     */
    private void resetProgress() {
        sequencePosition = 0;
    }

    /**
     * Shows an error message briefly when the user clicks the wrong direction.
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
     */
    private void showSuccess() {
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
