package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressBar;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Room3 Puzzle 5 (Frequency Spectrum).
 * Simple puzzle screen with a play button, answer field, hint and back
 * controls.
 *
 * Author: Kirtan Patel
 *
 * Notes:
 * - Enter triggers onCheckB (wired via TextField onAction in FXML).
 * - When correct, shows a large 'L' in the centre (styled like Puzzle4).
 * - The displayed letter is mouse transparent so Back remains clickable.
 */
public class Room3Puzzle5Controller implements Initializable {

    // FXML-injected nodes (must match fx:id values in FXML)
    @FXML
    private AnchorPane hintPaneB;
    @FXML
    private Button playButtonB;
    @FXML
    private Button hintButtonB;
    @FXML
    private Button backButtonB;
    @FXML
    private Label rewardLetterB;
    @FXML
    private Label feedbackLabelB;
    @FXML
    private TextField answerFieldB;
    @FXML
    private Label hintTextLabelB;
    @FXML
    private ImageView closeHintArrowB;
    @FXML
    private ImageView hint;
    @FXML
    private ImageView hintNote;

    // expected answer for puzzle 5 (change if needed)
    private static final String EXPECTED_ANSWER_B = "MIRROR";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // start with hint and reward hidden (if present)
        if (hintPaneB != null)
            hintPaneB.setVisible(false);
        if (rewardLetterB != null) {
            rewardLetterB.setVisible(false);
            // ensure letter does not intercept mouse events (so Back still works)
            rewardLetterB.setMouseTransparent(true);
        }
        if (feedbackLabelB != null) {
            feedbackLabelB.setText("");
            feedbackLabelB.setStyle("-fx-text-fill: yellow; -fx-font-size:16px;");
        }

        // ensure play button and back are enabled
        if (playButtonB != null)
            playButtonB.setDisable(false);
        if (backButtonB != null)
            backButtonB.setDisable(false);

        // optional: set focus to answer field so Enter works immediately
        if (answerFieldB != null) {
            // do not forcibly steal focus if you don't want it; comment out if not desired
            // answerFieldB.requestFocus();
        }

        this.facade = com.escape.App.gameFacade;
        startTimerUpdate();
    }

    /**
     * Shows the hint panel when the user clicks the hint button.
     * Applies penalty and sets the hint text with fade animation.
     */
    @FXML
    private void showHint(MouseEvent event) {
        if (hintPaneB == null)
            return;
        
        System.out.println("Hint clicked!");
        
        // Apply penalty
        if (com.escape.App.gameFacade != null) {
            com.escape.App.gameFacade.applyHintPenalty();
        }
        
        // Set hint text
        String hint = "Listen carefully to the audio clip. The answer is a word that describes what you see when you look at yourself.";
        if (hintTextLabelB != null) {
            hintTextLabelB.setText(hint);
        }
        
        // Show hint pane with fade animation
        hintPaneB.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.6), hintPaneB);
        hintPaneB.setOpacity(0);
        ft.setToValue(1.0);
        ft.play();
        
        if (hintTextLabelB != null)
            hintTextLabelB.setVisible(true);
        if (hintNote != null)
            hintNote.setVisible(true);
        if (closeHintArrowB != null)
            closeHintArrowB.setVisible(true);
    }

    /**
     * Hides the hint pane.
     */
    @FXML
    private void hideHint(MouseEvent event) {
        if (hintPaneB != null)
            hintPaneB.setVisible(false);
        if (closeHintArrowB != null)
            closeHintArrowB.setVisible(false);
    }

    /**
     * Handle the "Play" button click.
     * You can add media playback logic here later.
     */
    @FXML
    private void onPlayB(MouseEvent event) {
        System.out.println("Play button clicked (puzzle 5). Add audio here if desired.");
        // Example: play audio. Skipped per request.
    }

    /**
     * Called when the user presses Enter in the answer TextField
     * or when wired to some button. Uses ActionEvent so Enter works.
     */
    @FXML
    private void onCheckB(ActionEvent event) {
        if (answerFieldB == null || feedbackLabelB == null)
            return;

        String attempt = answerFieldB.getText();
        if (attempt == null)
            attempt = "";
        attempt = attempt.trim();

        if (attempt.isEmpty()) {
            feedbackLabelB.setText("Type an answer first.");
            feedbackLabelB.setStyle("-fx-text-fill: yellow; -fx-font-size:16px;");
            return;
        }

        // compare case-insensitively
        if (attempt.equalsIgnoreCase(EXPECTED_ANSWER_B)) {
            feedbackLabelB.setText("Correct");
            // same style as puzzle 4
            feedbackLabelB.setStyle("-fx-text-fill: #2fa4c6; -fx-font-size:20px;");

            // Add collected letter to game state
            if (facade != null && facade.getCurrentUser() != null) {
                com.escape.model.User user = facade.getCurrentUser();
                if (!user.getCollectedLetters().contains("L")) {
                    user.addCollectedLetter("L");
                    System.out.println("Letter 'L' added to inventory.");
                    System.out.println("Current letters: " + user.getCollectedLetters());
                    updateProgress();
                }
            }

            // show big "L" with same style as Puzzle4 'R'
            if (rewardLetterB != null) {
                rewardLetterB.setText("L");
                rewardLetterB.setVisible(true);
                // style to match the other puzzle letter appearance
                rewardLetterB.setStyle(
                        "-fx-font-family: 'Felix Titling'; " +
                                "-fx-font-size: 200px; " +
                                "-fx-text-fill: #d2735d; " +
                                "-fx-font-weight: bold;");
                // make sure it looks above background but does not intercept mouse clicks
                rewardLetterB.toFront();
                rewardLetterB.setMouseTransparent(true);

                if (facade != null) {
                    facade.addItem("L");
                }
            }

            // re-enable Back button and bring it to front so user can click it
            if (backButtonB != null) {
                backButtonB.setDisable(false);
                backButtonB.toFront();
            }

            // optionally disable answer input to prevent further attempts
            answerFieldB.setDisable(true);
        } else {
            feedbackLabelB.setText("Incorrect");
            feedbackLabelB.setStyle("-fx-text-fill: #ff6666; -fx-font-size:16px;");
        }
    }

    /**
     * Back button - return to Room3Combined menu.
     * This should be wired to the Back button in FXML.
     */
    @FXML
    private void onBackB(MouseEvent event) {
        System.out.println("Back clicked - return to Room3Combined");
        try {
            // call your app helper to change the root scene
            com.escape.App.setRoot("Room3Combined");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Label timerLabel;
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

}
