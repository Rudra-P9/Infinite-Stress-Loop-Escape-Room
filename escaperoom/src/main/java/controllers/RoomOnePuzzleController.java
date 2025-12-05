package controllers;

import com.escape.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;

public class RoomOnePuzzleController {

    @FXML
    private Label timerLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    private javafx.animation.Timeline timerTimeline;

    @FXML
    public void initialize() {
        startTimerUpdate();
    }

    private void startTimerUpdate() {
        timerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> updateTimer()));
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

    @FXML
    private void goToNext(MouseEvent event) throws Exception {
        App.setRoot("RoomOneBoard");
    }

    @FXML
    private void goBack(MouseEvent event) throws Exception {
        App.setRoot("ChamberHall");
    }
}
