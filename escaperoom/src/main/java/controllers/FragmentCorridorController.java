package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
 * Controller for the Fragment Corridor screen.
 * Manages the MEMORY puzzle: letters must be clicked in the exact sequence
 * M-E-M-O-R-Y.
 * Clicking the wrong letter resets progress and shows an error message.
 * 
 * @author Jacob Kinard
 * @author Rudra Patel
 */
public class FragmentCorridorController implements Initializable {

    /** Label showing the E letter when revealed */
    @FXML
    private Label EShow;

    /** Label showing the first M letter when revealed */
    @FXML
    private Label M1Show;

    /** Label showing the second M letter when revealed */
    @FXML
    private Label M2Show;

    /** Label for the first M key (clickable area) */
    @FXML
    private Label MKey1;

    /** Button for the first M key click event */
    @FXML
    private Button MKey1B;

    /** Label for the second M key (clickable area) */
    @FXML
    private Label MKey2;

    /** Button for the second M key click event */
    @FXML
    private Button Mkey2B;

    /** Label for the E key (clickable area) */
    @FXML
    private Label EKey1;

    /** Button for the E key click event */
    @FXML
    private Button EKey;

    /** Label showing the O letter when revealed */
    @FXML
    private Label OShow;

    /** Label showing the R letter when revealed */
    @FXML
    private Label RShow;

    /** Label showing the Y letter when revealed */
    @FXML
    private Label YShow;

    /** Label displayed when incorrect letter is clicked */
    @FXML
    private Label IncorrectLable;

    /** Label for the continue button text */
    @FXML
    private Label ContinueLabel;

    /** Button to continue to next screen after puzzle completion */
    @FXML
    private Button ContinueButton;

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

    /** Label displaying the timer */
    @FXML
    private Label timerLabel;

    /** Label displaying the penalty when hint is used */
    @FXML
    private Label penaltyLabel;

    /** Progress bar showing game completion */
    @FXML
    private ProgressBar progressBar;

    /** Label showing progress percentage */
    @FXML
    private Label progressLabel;

    /** Timeline for updating the timer */
    private Timeline timerTimeline;

    /** The target sequence to spell: MEMORY */
    private static final String[] SEQUENCE = { "M", "E", "M", "O", "R", "Y" };

    /** Current position in the sequence (0-5, 6 = complete) */
    private int sequencePosition = 0;

    /**
     * Initializes the controller by hiding letter labels and applying consistent
     * styling.
     * Sets up initial visibility states and applies fonts to letter elements.
     * 
     * @param url            the location used to resolve relative paths for the
     *                       root object, or null
     * @param resourceBundle the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Hide hint pane initially
        if (hintPane != null)
            hintPane.setVisible(false);

        // Start timer update
        startTimerUpdate();

        // Ensure all show labels are initially hidden
        if (M1Show != null)
            M1Show.setVisible(false);
        if (M2Show != null)
            M2Show.setVisible(false);
        if (YShow != null)
            YShow.setVisible(false);
        if (OShow != null)
            OShow.setVisible(false);
        if (EShow != null)
            EShow.setVisible(false);
        if (RShow != null)
            RShow.setVisible(false);
        if (IncorrectLable != null)
            IncorrectLable.setVisible(false);
        if (ContinueLabel != null)
            ContinueLabel.setVisible(false);
        if (ContinueButton != null)
            ContinueButton.setVisible(false);

        // Apply consistent letter styling (matches other screens)
        try {
            if (M1Show != null) {
                M1Show.getStyleClass().add("letter");
                M1Show.setStyle("-fx-text-fill: white;");
                M1Show.setFont(javafx.scene.text.Font.font("Times New Roman", 100));
            }
            if (M2Show != null) {
                M2Show.getStyleClass().add("letter");
                M2Show.setStyle("-fx-text-fill: white;");
                M2Show.setFont(javafx.scene.text.Font.font("Times New Roman", 100));
            }
            if (MKey1 != null) {
                MKey1.getStyleClass().add("letter");
                MKey1.setFont(javafx.scene.text.Font.font("Times New Roman", 96));
            }
            if (MKey2 != null) {
                MKey2.getStyleClass().add("letter");
                MKey2.setFont(javafx.scene.text.Font.font("Times New Roman", 77));
            }
            if (EShow != null) {
                EShow.getStyleClass().add("letter");
                EShow.setStyle("-fx-text-fill: white;");
                EShow.setFont(javafx.scene.text.Font.font("Times New Roman", 100));
            }
            if (EKey1 != null) {
                EKey1.getStyleClass().add("letter");
                EKey1.setFont(javafx.scene.text.Font.font("Times New Roman", 77));
            }
        } catch (Exception ignore) {
            // styling optional — ignore failures
        }
    }

    /**
     * Starts the timer update timeline.
     * Creates a timeline that updates the timer display every second.
     */
    private void startTimerUpdate() {
        timerTimeline = new Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timerTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timerTimeline.play();
        updateTimer(); // Initial call
    }

    /**
     * Updates the timer display with remaining time.
     * Changes color to red if time is running low.
     */
    private void updateTimer() {
        if (App.gameFacade != null) {
            int remaining = App.gameFacade.getTimeRemaining();
            int minutes = remaining / 60;
            int seconds = remaining % 60;
            if (timerLabel != null) {
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

                // Change color if time is running low
                if (remaining < 60) {
                    timerLabel.setTextFill(javafx.scene.paint.Color.RED);
                } else {
                    timerLabel.setTextFill(javafx.scene.paint.Color.LIME);
                }
            }
            if (remaining <= 0) {
                if (timerTimeline != null)
                    timerTimeline.stop();
                // Handle game over if needed
            }
        }
        
        // Update progress bar
        updateProgress();
    }

    /**
     * Updates the progress bar based on collected letters.
     */
    private void updateProgress() {
        if (App.gameFacade != null && progressBar != null && progressLabel != null) {
            int percentage = App.gameFacade.getProgressPercentage();
            progressBar.setProgress(percentage / 100.0);
            progressLabel.setText(percentage + "%");
        }
    }

    /**
     * Handles M key click events.
     * Checks if M is the correct letter in the MEMORY sequence.
     * 
     * @param event the mouse event triggered by clicking an M key
     */
    @FXML
    void MLetterClicked(MouseEvent event) {
        handleLetterClick("M", event.getSource());
    }

    /**
     * Handles E key click events.
     * Checks if E is the correct letter in the MEMORY sequence.
     * 
     * @param event the mouse event triggered by clicking the E key
     */
    @FXML
    void ELetterClicked(MouseEvent event) {
        handleLetterClick("E", event.getSource());
    }

    /**
     * Handles O key click events.
     * Checks if O is the correct letter in the MEMORY sequence.
     * 
     * @param event the mouse event triggered by clicking the O key
     */
    @FXML
    void OLetterClicked(MouseEvent event) {
        handleLetterClick("O", event.getSource());
    }

    /**
     * Handles R key click events.
     * Checks if R is the correct letter in the MEMORY sequence.
     * 
     * @param event the mouse event triggered by clicking the R key
     */
    @FXML
    void RLetterClicked(MouseEvent event) {
        handleLetterClick("R", event.getSource());
    }

    /**
     * Handles Y key click events.
     * Checks if Y is the correct letter in the MEMORY sequence.
     * 
     * @param event the mouse event triggered by clicking the Y key
     */
    @FXML
    void YLetterClicked(MouseEvent event) {
        handleLetterClick("Y", event.getSource());
    }

    /**
     * Unified handler for all letter clicks.
     * Validates the clicked letter against the expected sequence position.
     * Reveals the correct show label if valid, or resets progress and shows error
     * if invalid.
     * 
     * @param letter the letter that was clicked ("M", "E", "O", "R", "Y")
     * @param source the UI element that triggered the click
     */
    private void handleLetterClick(String letter, Object source) {
        // Check if we've completed the sequence
        if (sequencePosition >= SEQUENCE.length) {
            return; // puzzle already solved
        }

        // Check if clicked letter matches expected position
        if (letter.equals(SEQUENCE[sequencePosition])) {
            // Correct letter - reveal the corresponding show label
            revealLetter(sequencePosition);
            sequencePosition++;

            // Check if puzzle is complete
            if (sequencePosition >= SEQUENCE.length) {
                showSuccess();
            }

            // Optional: disable the clicked button/label
            if (source instanceof Button) {
                ((Button) source).setDisable(true);
            }
        } else {
            // Wrong letter - reset progress and show error
            resetProgress();
            showError();
        }
    }

    /**
     * Reveals the show label at the given sequence position.
     * Makes the appropriate letter visible based on the current progress.
     * 
     * @param position the position in the MEMORY sequence (0-5)
     */
    private void revealLetter(int position) {
        switch (position) {
            case 0: // First M
                if (M1Show != null)
                    M1Show.setVisible(true);
                break;
            case 1: // E
                if (EShow != null)
                    EShow.setVisible(true);
                break;
            case 2: // Second M
                if (M2Show != null)
                    M2Show.setVisible(true);
                break;
            case 3: // O
                if (OShow != null)
                    OShow.setVisible(true);
                break;
            case 4: // R
                if (RShow != null)
                    RShow.setVisible(true);
                break;
            case 5: // Y
                if (YShow != null)
                    YShow.setVisible(true);
                break;
        }
    }

    /**
     * Resets the puzzle progress by hiding all show labels and re-enabling
     * clickable areas.
     * Called when the user clicks an incorrect letter.
     */
    private void resetProgress() {
        sequencePosition = 0;

        // Hide all show labels
        if (M1Show != null)
            M1Show.setVisible(false);
        if (EShow != null)
            EShow.setVisible(false);
        if (M2Show != null)
            M2Show.setVisible(false);
        if (OShow != null)
            OShow.setVisible(false);
        if (RShow != null)
            RShow.setVisible(false);
        if (YShow != null)
            YShow.setVisible(false);

        // Re-enable all clickable areas
        if (MKey1B != null)
            MKey1B.setDisable(false);
        if (Mkey2B != null)
            Mkey2B.setDisable(false);
        if (EKey != null)
            EKey.setDisable(false);

        // Apply penalty for incorrect sequence
        if (com.escape.App.gameFacade != null) {
            com.escape.App.gameFacade.applyHintPenalty();
            updateTimer(); // Update immediately
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
    }

    /**
     * Shows an error message briefly when the user clicks the wrong letter.
     * Displays the error for 2 seconds then hides it automatically.
     */
    private void showError() {
        if (IncorrectLable != null) {
            IncorrectLable.setVisible(true);

            // Hide error message after 2 seconds
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(2));
            pause.setOnFinished(e -> {
                if (IncorrectLable != null)
                    IncorrectLable.setVisible(false);
            });
            pause.play();
        }
    }

    /**
     * Shows the Continue button with breathing animation when puzzle is solved.
     * Creates a pulsing scale animation to draw attention to the continue button.
     */
    private void showSuccess() {
        System.out.println("[FragmentCorridor] showSuccess() called");
        System.out.println("[FragmentCorridor] gameFacade = " + App.gameFacade);
        
        // Award the letter "A" to the user's inventory (from game.json)
        if (App.gameFacade != null && App.gameFacade.getCurrentUser() != null) {
            com.escape.model.User user = App.gameFacade.getCurrentUser();
            System.out.println("[FragmentCorridor] user = " + user);
            
            // Check if user already has the letter before adding
            if (!user.getCollectedLetters().contains("A")) {
                boolean added = user.addCollectedLetter("A");
                if (added) {
                    System.out.println("Letter 'A' added to inventory.");
                    System.out.println("Current letters: " + user.getCollectedLetters());
                    updateProgress();
                } else {
                    System.out.println("Failed to add letter 'A' - inventory may be full.");
                }
            } else {
                System.out.println("Letter 'A' already in inventory.");
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

    /**
     * Shows the hint for the MEMORY puzzle.
     * Applies a time penalty and displays the sequence hint.
     * 
     * @param event the mouse event triggered by clicking the hint icon
     */
    @FXML
    private void showHint(MouseEvent event) {
        System.out.println("Hint clicked!");

        // Apply penalty
        if (App.gameFacade != null) {
            App.gameFacade.applyHintPenalty();
            updateTimer(); // Update immediately
        }

        // Animate penalty label
        if (penaltyLabel != null) {
            penaltyLabel.setOpacity(1.0);
            FadeTransition fade = new FadeTransition(Duration.seconds(2.0), penaltyLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        }

        // Set hint text
        String hint = "The sequence spells: M-E-M-O-R-Y";
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

        if (hintText != null)
            hintText.setVisible(true);
        if (hintNote != null)
            hintNote.setVisible(true);
        if (closeHintArrow != null)
            closeHintArrow.setVisible(true);
    }

    /**
     * Hides the hint pane.
     * 
     * @param event the mouse event triggered by clicking the close arrow
     */
    @FXML
    private void hideHint(MouseEvent event) {
        if (hintPane != null)
            hintPane.setVisible(false);
        if (closeHintArrow != null)
            closeHintArrow.setVisible(false);
    }

    /**
     * Handles Continue button click to navigate to the next room.
     * Navigates to FragmentCorridorRoomTwo after puzzle completion.
     * 
     * @param event the mouse event triggered by clicking Continue
     */
    @FXML
    void ContinueToNext(MouseEvent event) {
        try {
            App.setRoot("FragmentCorridorRoomTwo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
