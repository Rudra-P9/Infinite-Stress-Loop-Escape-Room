package controllers;

import com.escape.App;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class RoomOneLetterController {

    @FXML
    private FlowPane letterPane;

    @FXML
    private Label noteText;

    @FXML
    private Label timerLabel;

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
    }

    private void handleLetterClick(MouseEvent event) {
        if (isResetting || targetAnswer.isEmpty())
            return;

        Label clicked = (Label) event.getSource();
        String letter = clicked.getText().toUpperCase();

        // Ignore if already clicked (Green)
        if (clicked.getTextFill().equals(Color.LIME)) {
            return;
        }

        // Check if the clicked letter matches the expected character
        char expectedChar = targetAnswer.charAt(currentIndex);
        if (letter.charAt(0) == expectedChar) {
            // CORRECT
            clicked.setTextFill(Color.LIME); // Turn Green
            currentIndex++;

            // Check if puzzle is complete
            if (currentIndex >= targetAnswer.length()) {
                System.out.println("Puzzle Solved!");

                int currentStage = 0;
                if (App.gameFacade != null) {
                    currentStage = App.gameFacade.getRoomOneStage();
                }

                if (currentStage == 0) {
                    // First riddle solved
                    noteText.setText("Correct! One last riddle...");
                    noteText.setTextFill(Color.LIME);

                    if (App.gameFacade != null) {
                        // Advance stage
                        App.gameFacade.setRoomOneStage(1);
                        // Store previous riddle to avoid repeat
                        App.gameFacade.setPreviousRiddle(App.gameFacade.getRoomOneRiddle());
                        // Clear current riddle so Board loads a new one
                        App.gameFacade.setRoomOneRiddle(null);
                        App.gameFacade.setRoomOneAnswer(null);
                        App.gameFacade.setRoomOneHint(null);
                    }

                    // Delay before returning to Board for the second riddle
                    PauseTransition delay = new PauseTransition(Duration.seconds(2.0));
                    delay.setOnFinished(e -> {
                        try {
                            App.setRoot("RoomOneBoard");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    delay.play();

                } else {
                    // Second riddle solved (Stage 1 -> Complete)
                    noteText.setText("Room Complete!");
                    noteText.setTextFill(Color.LIME);

                    if (App.gameFacade != null) {
                        App.gameFacade.setRoomOneStage(2); // Mark as complete
                    }

                    // Delay before leaving to Hall
                    PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                    delay.setOnFinished(e -> {
                        try {
                            App.setRoot("ChamberHall");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    delay.play();
                }
            }
        } else {
            // INCORRECT
            clicked.setTextFill(Color.RED); // Turn Red
            noteText.setText("INCORRECT!");
            noteText.setTextFill(Color.RED);
            isResetting = true;

            // Reset sequence after a short delay
            PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
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
}
