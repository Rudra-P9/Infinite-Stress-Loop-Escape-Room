package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escape.App;
import com.escape.model.Difficulty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the difficulty menu.
 * 
 * @author Rudra Patel
 */

public class DifficultyMenuController implements Initializable {

    @FXML
    private javafx.scene.control.Label welcomeLabel;
    @FXML
    private javafx.scene.control.Label easyLabel;
    @FXML
    private javafx.scene.control.Label mediumLabel;
    @FXML
    private javafx.scene.control.Label hardLabel;
    @FXML
    private javafx.scene.control.Label backLabel;

    /**
     * Handles selecting Easy difficulty.
     * Sets global difficulty and loads the StartGame screen.
     *
     * @param event mouse click
     * @throws IOException if StartGame.fxml fails to load
     */
    @FXML
    private void goToEasy(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.EASY;
        App.setRoot("StartGame");
    }

    /**
     * Handles selecting Medium difficulty.
     * Sets global difficulty and loads the StartGame screen.
     *
     * @param event mouse click
     * @throws IOException if StartGame.fxml fails to load
     */
    @FXML
    private void goToMedium(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.MEDIUM;
        App.setRoot("StartGame");
    }

    /**
     * Handles selecting Hard difficulty.
     * Sets global difficulty and loads the StartGame screen.
     *
     * @param event mouse click
     * @throws IOException if StartGame.fxml fails to load
     */
    @FXML
    private void goToHard(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.HARD;
        App.setRoot("StartGame");
    }

    /**
     * Navigates back to the TerminalMenu screen.
     *
     * @param event mouse click
     * @throws IOException if TerminalMenu.fxml fails to load
     */
    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("TerminalMenu");
    }

    /**
     * Initializes the difficulty menu with a Terminal-style
     * animated sequence revealing each label one after another.
     *
     * @param url ignored
     * @param rb ignored
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeText(welcomeLabel, "[ Facility 67 Termalink: Difficulty ]", () -> {
            typeText(easyLabel, "[ Easy ]", () -> {
                typeText(mediumLabel, "[ Medium ]", () -> {
                    typeText(hardLabel, "[ Hard ]", () -> {
                        typeText(backLabel, "[ Back ]", null);
                    });
                });
            });
        });
    }

    /**
     * Utility method that applies a typewriter effect to a label.
     * Characters appear one at a time to mimic a retro terminal.
     *
     * @param label the label to animate
     * @param text the full text to reveal
     * @param onFinished optional callback after animation completes
     */
    private void typeText(javafx.scene.control.Label label, String text, Runnable onFinished) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(60 * (i + 1)),
                    event -> {
                        sb.append(text.charAt(index));
                        label.setText(sb.toString());
                    });
            timeline.getKeyFrames().add(keyFrame);
        }

        if (onFinished != null) {
            timeline.setOnFinished(e -> onFinished.run());
        }

        timeline.play();
    }
}