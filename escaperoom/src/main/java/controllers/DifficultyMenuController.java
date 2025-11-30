package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import com.escape.App;
import com.escape.model.Difficulty;

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

    @FXML
    private void goToEasy(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.EASY;
        App.setRoot("Gateway");
    }

    @FXML
    private void goToMedium(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.MEDIUM;
        App.setRoot("Gateway");
    }

    @FXML
    private void goToHard(MouseEvent event) throws IOException {
        App.currentDifficulty = Difficulty.HARD;
        App.setRoot("Gateway");
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("TerminalMenu");
    }

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