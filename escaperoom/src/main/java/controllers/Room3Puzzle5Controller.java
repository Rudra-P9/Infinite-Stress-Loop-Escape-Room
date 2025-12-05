package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Room3Puzzle5Controller
 * Author: Kirtan Patel
 *
 * Simple student-level controller for the right vault puzzle.
 * Contains a hint toggle, back button, play audio button, an answer field
 * that submits on Enter and shows correct/incorrect feedback.
 *
 * If you want a different correct answer, change EXPECTED_ANSWER.
 */
public class Room3Puzzle5Controller implements Initializable {

    @FXML private ImageView bgImageB;
    @FXML private Button playButtonB;
    @FXML private AnchorPane hintPaneB;
    @FXML private Button hintButtonB;
    @FXML private Button backButtonB;
    @FXML private TextField answerFieldB;
    @FXML private Label rewardLetterB;
    @FXML private Label feedbackLabelB;
    @FXML private Label hintTextLabelB;

    // Change this to the expected answer for puzzle 5
    private static final String EXPECTED_ANSWER = "MIRROR";

    // Reward letter for puzzle 5
    private static final String REWARD = "L";

    // MediaPlayer for audio playback (optional)
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // start with hint and reward hidden
        if (hintPaneB != null) hintPaneB.setVisible(false);
        if (rewardLetterB != null) rewardLetterB.setVisible(false);
        if (feedbackLabelB != null) feedbackLabelB.setText("");
    }

    // toggle hint visibility
    @FXML
    private void onHintB(MouseEvent event) {
        boolean now = !hintPaneB.isVisible();
        hintPaneB.setVisible(now);
        hintPaneB.toFront();
        System.out.println("Puzzle5: hint toggled -> " + now);
    }

    // Play audio when user presses play
    @FXML
    private void onPlayB(MouseEvent event) {
        System.out.println("Puzzle5: play button pressed");
        // Try to load audio resource from resources/audio/Puzzle5Track.mp3
        try {
            URL mediaUrl = getClass().getResource("/audio/Puzzle5Track.mp3");
            if (mediaUrl == null) {
                System.out.println("Puzzle5: audio not found at /audio/Puzzle5Track.mp3");
                return;
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Check answer when Enter pressed or called by UI
    @FXML
    private void onCheckB() {
        String attempt = "";
        if (answerFieldB != null) {
            attempt = answerFieldB.getText();
        }
        if (attempt == null) attempt = "";
        attempt = attempt.trim();

        if (attempt.isEmpty()) {
            if (feedbackLabelB != null) feedbackLabelB.setText("Type an answer first.");
            return;
        }

        // compare case-insensitive
        if (attempt.equalsIgnoreCase(EXPECTED_ANSWER)) {
            if (feedbackLabelB != null) feedbackLabelB.setText("Correct!");
            System.out.println("Puzzle5: correct answer entered.");

            if (rewardLetterB != null) {
                rewardLetterB.setText(REWARD);
                rewardLetterB.setVisible(true);
                rewardLetterB.toFront();
            }

            if (answerFieldB != null) answerFieldB.setDisable(true);
        } else {
            if (feedbackLabelB != null) feedbackLabelB.setText("Incorrect! Try again.");
            System.out.println("Puzzle5: wrong attempt -> " + attempt);
        }
    }

    // Back to the combined room
    @FXML
    private void onBackB(MouseEvent event) {
        System.out.println("Puzzle5: Back clicked - returning to Room3Combined");
        try {
            com.escape.App.setRoot("Room3Combined");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
