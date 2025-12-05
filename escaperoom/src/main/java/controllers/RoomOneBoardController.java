package controllers;

import com.escape.App;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for the Room One Board
 * Handles the logic for the Room One Board
 * 
 * @author Rudra Patel
 */
public class RoomOneBoardController {

    @FXML
    private AnchorPane hintPane;

    @FXML
    private Label hintText;

    @FXML
    private ImageView closeHintArrow;

    @FXML
    private ImageView hintNote;

    @FXML
    private Label riddleLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label penaltyLabel;

    @FXML
    private Group introOverlay;

    @FXML
    private Group boardOverlay;

    @FXML
    private Button acknowledgeButton;

    @FXML
    private Button infoButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    private javafx.animation.Timeline timerTimeline;

    @FXML
    public void initialize() {
        if (introOverlay != null) {
            if (com.escape.App.gameFacade != null && !com.escape.App.gameFacade.isRoomOneIntroSeen()) {
                showIntro();
            } else {
                showBoard();
            }
        }

        if (hintPane != null)
            hintPane.setVisible(false);
        loadRandomRiddle();
        startTimerUpdate();
    }

    private void showIntro() {
        if (introOverlay != null)
            introOverlay.setVisible(true);
        if (boardOverlay != null)
            boardOverlay.setVisible(false);
        if (infoButton != null)
            infoButton.setVisible(false);
    }

    private void showBoard() {
        if (introOverlay != null)
            introOverlay.setVisible(false);
        if (boardOverlay != null)
            boardOverlay.setVisible(true);
        // Show info button when board is visible
        if (infoButton != null)
            infoButton.setVisible(true);
    }

    @FXML
    private void onAcknowledge(MouseEvent event) {
        if (com.escape.App.gameFacade != null) {
            com.escape.App.gameFacade.setRoomOneIntroSeen(true);
        }
        showBoard();
    }

    @FXML
    private void onInfo(MouseEvent event) {
        showIntro();
    }

    private void startTimerUpdate() {
        timerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timerTimeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timerTimeline.play();
        updateTimer(); // Initial call
    }

    private void updateTimer() {
        if (com.escape.App.gameFacade != null) {
            int remaining = com.escape.App.gameFacade.getTimeRemaining();
            int minutes = remaining / 60;
            int seconds = remaining % 60;
            if (timerLabel != null) {
                timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

                // Change color if time is running low (match ChamberHall)
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

    private void updateProgress() {
        if (com.escape.App.gameFacade != null && progressBar != null && progressLabel != null) {
            int percentage = com.escape.App.gameFacade.getProgressPercentage();
            progressBar.setProgress(percentage / 100.0);
            progressLabel.setText(percentage + "%");
        }
    }

    private void loadRandomRiddle() {
        try {
            // Checks if we already have a riddle for this session
            if (com.escape.App.gameFacade != null) {
                String existingRiddle = com.escape.App.gameFacade.getRoomOneRiddle();
                if (existingRiddle != null && !existingRiddle.isEmpty()) {
                    riddleLabel.setText(existingRiddle);
                    return;
                }
            }

            var stream = getClass().getResourceAsStream("/riddles.txt");
            if (stream == null) {
                System.err.println("Could not find riddles.txt");
                riddleLabel.setText("Riddle unavailable.");
                return;
            }

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(stream));
            java.util.List<String> lines = new java.util.ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }

            if (!lines.isEmpty()) {
                java.util.Random rand = new java.util.Random();
                String randomLine = "";
                String riddleText = "";
                String answerText = "";
                String hintText = "";

                // Get previous riddle to avoid repeat
                String previousRiddle = null;
                if (com.escape.App.gameFacade != null) {
                    previousRiddle = com.escape.App.gameFacade.getPreviousRiddle();
                }

                // Try to find a unique riddle (max 10 attempts to avoid infinite loop)
                for (int i = 0; i < 10; i++) {
                    randomLine = lines.get(rand.nextInt(lines.size()));
                    String[] parts = randomLine.split("::");
                    if (parts.length > 0) {
                        riddleText = parts[0].trim();
                        // If it matches previous, try again
                        if (previousRiddle != null && riddleText.equals(previousRiddle)) {
                            continue;
                        }

                        if (parts.length > 1)
                            answerText = parts[1].trim();
                        if (parts.length > 2)
                            hintText = parts[2].trim();
                        break; // Found a valid one
                    }
                }

                if (!riddleText.isEmpty()) {
                    riddleLabel.setText(riddleText);

                    // Save to facade for persistence
                    if (com.escape.App.gameFacade != null) {
                        com.escape.App.gameFacade.setRoomOneRiddle(riddleText);
                        com.escape.App.gameFacade.setRoomOneAnswer(answerText);
                        com.escape.App.gameFacade.setRoomOneHint(hintText);
                        System.out.println(
                                "Riddle set: " + riddleText + " | Answer: " + answerText + " | Hint: " + hintText);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            riddleLabel.setText("Error loading riddle.");
        }
    }

    @FXML
    private void showHint(MouseEvent event) {
        System.out.println("Hint clicked!");

        // Apply penalty
        if (com.escape.App.gameFacade != null) {
            com.escape.App.gameFacade.applyHintPenalty();
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

        String hint = "Hint unavailable.";
        if (com.escape.App.gameFacade != null) {
            String storedHint = com.escape.App.gameFacade.getRoomOneHint();
            if (storedHint != null && !storedHint.isEmpty()) {
                hint = storedHint;
            }
        }

        hintText.setText(hint);

        hintPane.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.6), hintPane);
        hintPane.setOpacity(0);
        ft.setToValue(1.0);
        ft.play();

        hintPane.setVisible(true);
        hintText.setVisible(true);
        hintNote.setVisible(true);
        closeHintArrow.setVisible(true);
    }

    @FXML
    private void goToNext(MouseEvent event) throws Exception {
        // Go to the main hall
        App.setRoot("ChamberHall");
        System.out.println("Returning to Chamber Hall from Room One");
    }

    @FXML
    private void goToLetter(MouseEvent event) throws Exception {
        // Go to the letter puzzle
        App.setRoot("RoomOneLetter");
        System.out.println("Going to Room One Letter from Room One Board");
    }

    @FXML
    private void hideHint(MouseEvent e) {
        hintPane.setVisible(false);
        closeHintArrow.setVisible(false);
    }

}
