package controllers;

import com.escape.App;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

/**
 * Controller for the Room One Letter screen.
 * Handles the letter display and timer.
 * 
 * @author Rudra Patel
 */
public class RoomOneLetterController {

    @FXML
    private FlowPane letterPane;

    @FXML
    private Label noteText;

    @FXML
    private Label timerLabel;

    @FXML
    private Label penaltyLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    private String targetAnswer = "";
    private int currentIndex = 0;
    private boolean isResetting = false;
    private javafx.animation.Timeline timerTimeline;

    @FXML
    public void initialize() {
        startTimerUpdate();
        // Retrieve the answer from the global facade
        if (App.gameFacade != null) {
            String ans = App.gameFacade.getRoomOneAnswer();
            if (ans != null) {
                targetAnswer = ans.toUpperCase();
            }
        }
        System.out.println("Target Answer: " + targetAnswer);

        for (var node : letterPane.getChildren()) {
            if (node instanceof Label label) {
                label.setStyle(
                        "-fx-font-size: 75px;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-family: 'Times New Roman';");

                // Make letters clickable
                label.setOnMouseClicked(this::handleLetterClick);

                // Optional hover highlight
                label.setOnMouseEntered(e -> {
                    if (!isResetting && label.getTextFill().equals(Color.WHITE)) {
                        label.setOpacity(0.8);
                    }
                });
                label.setOnMouseExited(e -> label.setOpacity(1.0));
            }
        }
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
            System.out.println("[RoomOneLetter] Progress updated: " + percentage + "% (" + 
                (com.escape.App.gameFacade.getCurrentUser() != null ? com.escape.App.gameFacade.getCurrentUser().getCollectedLetters().size() : 0) + " letters)");
        }
    }

    private void handleLetterClick(MouseEvent event) {
        if (isResetting || targetAnswer.isEmpty())
            return;

        Label clicked = (Label) event.getSource();
        String letter = clicked.getText().toUpperCase();

        // Ignore if already clicked
        if (clicked.getTextFill().equals(Color.LIME))
            return;

        char expectedChar = targetAnswer.charAt(currentIndex);

        if (letter.charAt(0) == expectedChar) {
            // correct LETTER
            clicked.setTextFill(Color.LIME);
            currentIndex++;

            if (currentIndex >= targetAnswer.length()) {

                int currentStage = (App.gameFacade != null)
                        ? App.gameFacade.getRoomOneStage()
                        : 0;

                // first riddle done
                if (currentStage == 0) {
                    noteText.setText("Correct! One last riddle...");
                    noteText.setTextFill(Color.BLACK);

                    if (App.gameFacade != null) {
                        App.gameFacade.setRoomOneStage(1);
                        App.gameFacade.setPreviousRiddle(App.gameFacade.getRoomOneRiddle());
                        App.gameFacade.setRoomOneRiddle(null);
                        App.gameFacade.setRoomOneAnswer(null);
                        App.gameFacade.setRoomOneHint(null);
                    }

                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished(e -> {
                        try {
                            App.setRoot("RoomOneBoard");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    delay.play();
                    return;
                }

                // second riddle complete
                if (currentStage == 1) {
                    System.out.println("[RoomOneLetter] Room complete! Adding letter E");
                    System.out.println("[RoomOneLetter] gameFacade = " + App.gameFacade);
                    
                    noteText.setText("Room Complete!");
                    noteText.setTextFill(Color.BLACK);

                    if (App.gameFacade != null) {
                        App.gameFacade.setRoomOneStage(2);
                    }
                    
                    // Add letter E to current user's inventory
                    if (App.gameFacade != null && App.gameFacade.getCurrentUser() != null) {
                        com.escape.model.User user = App.gameFacade.getCurrentUser();
                        System.out.println("[RoomOneLetter] user = " + user);
                        
                        if (!user.getCollectedLetters().contains("E")) {
                            boolean added = user.addCollectedLetter("E");
                            if (added) {
                                System.out.println("Letter 'E' added to inventory.");
                                System.out.println("Current letters: " + user.getCollectedLetters());
                                updateProgress();
                            }
                        }
                    }

                    // Disable letters
                    for (var node : letterPane.getChildren())
                        if (node instanceof Label lbl)
                            lbl.setOnMouseClicked(null);

                    isResetting = true;

                    // Show the earned letter after 2 sec
                    PauseTransition showLetter = new PauseTransition(Duration.seconds(2));
                    showLetter.setOnFinished(ev -> {
                        noteText.setText("You've Earned A Letter! 'E'");
                        noteText.setTextFill(Color.BLACK);
                    });

                    // Leave the room after 5 sec
                    PauseTransition leave = new PauseTransition(Duration.seconds(7));
                    leave.setOnFinished(ev -> {
                        try {
                            App.setRoot("ChamberHall");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                    showLetter.play();
                    leave.play();
                    return;
                }
            }

        } else {
            // INCORRECT
            clicked.setTextFill(Color.RED);
            noteText.setText("INCORRECT!");
            noteText.setTextFill(Color.RED);
            isResetting = true;

            // Apply penalty
            if (com.escape.App.gameFacade != null) {
                com.escape.App.gameFacade.applyHintPenalty();
                updateTimer();
            }

            // Animate penalty label if present
            if (penaltyLabel != null) {
                penaltyLabel.setOpacity(1.0);
                FadeTransition fade = new FadeTransition(Duration.seconds(2.0), penaltyLabel);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            }

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> resetPuzzle());
            pause.play();
        }
    }

    private void resetPuzzle() {
        currentIndex = 0;
        noteText.setText("");
        noteText.setTextFill(Color.WHITE);

        for (var node : letterPane.getChildren()) {
            if (node instanceof Label label) {
                label.setTextFill(Color.WHITE);
            }
        }
        isResetting = false;
    }

    @FXML
    private void goToNext(MouseEvent event) throws Exception {
        App.setRoot("RoomOneBoard");
        System.out.println("Returning to Board");
    }

    @FXML
    private void goBack(MouseEvent event) throws Exception {
        App.setRoot("RoomOneBoard");
        System.out.println("Going back to Board");
    }
}
