package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Room3 Puzzle 5 (Frequency Spectrum).
 * Simple puzzle screen with a play button, answer field, hint and back controls.
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
    @FXML private AnchorPane hintPaneB;
    @FXML private Button playButtonB;
    @FXML private Button hintButtonB;
    @FXML private Button backButtonB;
    @FXML private Label rewardLetterB;
    @FXML private Label feedbackLabelB;
    @FXML private TextField answerFieldB;

    // expected answer for puzzle 5 (change if needed)
    private static final String EXPECTED_ANSWER_B = "MIRROR";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // start with hint and reward hidden (if present)
        if (hintPaneB != null) hintPaneB.setVisible(false);
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
        if (playButtonB != null) playButtonB.setDisable(false);
        if (backButtonB != null) backButtonB.setDisable(false);

        // optional: set focus to answer field so Enter works immediately
        if (answerFieldB != null) {
            // do not forcibly steal focus if you don't want it; comment out if not desired
            // answerFieldB.requestFocus();
        }
    }

    /**
     * Toggle the hint panel visible/hidden.
     * Called from the Hint button onMouseClicked.
     */
    @FXML
    private void onHintB(MouseEvent event) {
        if (hintPaneB == null) return;
        boolean now = !hintPaneB.isVisible();
        hintPaneB.setVisible(now);
        if (now) hintPaneB.toFront();
        System.out.println("Hint toggled -> " + now);
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
        if (answerFieldB == null || feedbackLabelB == null) return;

        String attempt = answerFieldB.getText();
        if (attempt == null) attempt = "";
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

            // show big "L" with same style as Puzzle4 'R'
            if (rewardLetterB != null) {
                rewardLetterB.setText("L");
                rewardLetterB.setVisible(true);
                // style to match the other puzzle letter appearance
                rewardLetterB.setStyle(
                    "-fx-font-family: 'Felix Titling'; " +
                    "-fx-font-size: 200px; " +
                    "-fx-text-fill: #d2735d; " +
                    "-fx-font-weight: bold;"
                );
                // make sure it looks above background but does not intercept mouse clicks
                rewardLetterB.toFront();
                rewardLetterB.setMouseTransparent(true);
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
}
