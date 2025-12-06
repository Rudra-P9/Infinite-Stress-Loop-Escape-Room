package controllers;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.escape.App;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for Puzzle 4.
 * Manages the answer checking, hint toggle, reward letter display,
 * and returning to the Room 3 main menu.
 * Author: Kirtan Patel
 */
public class Room3Puzzle4Controller implements Initializable {

    @FXML
    private ImageView bgImageA;
    @FXML
    private Label questionLabel;
    @FXML
    private TextField answerFieldA;
    @FXML
    private AnchorPane hintPane;
    @FXML
    private Label rewardLetter;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Label hintTextLabel;
    @FXML
    private ImageView closeHintArrow;
    @FXML
    private ImageView hint;
    @FXML
    private ImageView hintNote;
    @FXML
    private Button backBtn;

    private static final String EXPECTED_ANSWER = "SIX";
    private static final String EXPECTED_ANSWER_NUM = "6";
    private static final String REWARD = "R";

    /**
     * Called when the puzzle loads.
     * Sets the initial visibility of the hint and reward areas,
     * and makes sure Enter submits the answer.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Room3Puzzle4Controller initialized.");
        this.facade = com.escape.App.gameFacade;
        startTimerUpdate();

        if (hintPane != null) {
            hintPane.setVisible(false);
        }

        if (rewardLetter != null) {
            rewardLetter.setVisible(false);
        }

        if (feedbackLabel != null) {
            feedbackLabel.setText("");
        }

        if (answerFieldA != null) {
            answerFieldA.setOnAction(this::onCheckAAction);
        }
    }

    /**
     * Shows the hint panel when the user clicks the hint button.
     * Applies penalty and sets the hint text with fade animation.
     */
    @FXML
    private void showHint(MouseEvent event) {
        if (hintPane == null)
            return;

        System.out.println("Hint clicked!");

        // Apply penalty
        if (com.escape.App.gameFacade != null) {
            com.escape.App.gameFacade.applyHintPenalty();
        }

        // Set hint text
        String hint = "If there are six apples and you take away four, how many do you have?";
        if (hintTextLabel != null) {
            hintTextLabel.setText(hint);
        }

        // Show hint pane with fade animation
        hintPane.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.6), hintPane);
        hintPane.setOpacity(0);
        ft.setToValue(1.0);
        ft.play();

        if (hintTextLabel != null)
            hintTextLabel.setVisible(true);
        if (hintNote != null)
            hintNote.setVisible(true);
        if (closeHintArrow != null)
            closeHintArrow.setVisible(true);
    }

    /**
     * Hides the hint pane.
     */
    @FXML
    private void hideHint(MouseEvent event) {
        if (hintPane != null)
            hintPane.setVisible(false);
        if (closeHintArrow != null)
            closeHintArrow.setVisible(false);
    }

    /**
     * Handles the Enter key inside the answer field.
     */
    @FXML
    private void onCheckAAction(ActionEvent evt) {
        checkAnswer();
    }

    /**
     * Handles mouse-based checking if you ever add a check button.
     */
    @FXML
    private void onCheckAMouse(MouseEvent event) {
        checkAnswer();
    }

    /**
     * Checks whether the user's typed answer is correct.
     * Shows feedback and displays the reward letter if correct.
     */
    private void checkAnswer() {

        if (answerFieldA == null)
            return;

        String attempt = answerFieldA.getText();
        if (attempt == null) {
            attempt = "";
        }

        attempt = attempt.trim();
        String normalized = attempt.toUpperCase(Locale.ROOT);

        if (attempt.isEmpty()) {
            if (feedbackLabel != null) {
                feedbackLabel.setText("Type an answer first.");
            }
            return;
        }

        if (normalized.equals(EXPECTED_ANSWER) || normalized.equals(EXPECTED_ANSWER_NUM)) {

            if (feedbackLabel != null) {
                feedbackLabel.setText("Correct!");
            }

            System.out.println("Puzzle4: correct answer entered.");

            // Add collected letter to game state
            if (facade != null && facade.getCurrentUser() != null) {
                com.escape.model.User user = facade.getCurrentUser();
                if (!user.getCollectedLetters().contains("R")) {
                    user.addCollectedLetter("R");
                    System.out.println("Letter 'R' added to inventory.");
                    System.out.println("Current letters: " + user.getCollectedLetters());
                    updateProgress();
                }
            }

            if (rewardLetter != null) {
                rewardLetter.setText(REWARD);
                rewardLetter.setVisible(true);
                rewardLetter.setStyle(
                        "-fx-font-family: 'Felix Titling'; -fx-font-size: 170px; " +
                                "-fx-text-fill: #d2735d; -fx-font-weight: bold;");
                rewardLetter.toFront();

                if (facade != null) {
                    facade.addItem("R");
                }
            }

            answerFieldA.setDisable(true);

            // Auto-navigate back to Room3Combined after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        try {
                            com.escape.App.setRoot("Room3Combined");
                            System.out.println("Auto-navigating to Room3Combined after puzzle completion.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } else {
            if (feedbackLabel != null) {
                feedbackLabel.setText("Incorrect! Try again.");
            }
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

            System.out.println("Puzzle4: wrong attempt -> " + attempt);
        }
    }

    @FXML
    private Label penaltyLabel;

    /**
     * Returns to the Room 3 main menu when Back is clicked.
     */
    @FXML
    private void onBackA(MouseEvent event) {
        System.out.println("Back clicked - return to Room3Combined");
        try {
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

    @FXML
    private void onBack(MouseEvent event) throws Exception {
        // Go to the main screen
        App.setRoot("Room3Combined");
        System.out.println("Returning to Room 3 Combined from Room 3 Puzzle 4");
    }
}
