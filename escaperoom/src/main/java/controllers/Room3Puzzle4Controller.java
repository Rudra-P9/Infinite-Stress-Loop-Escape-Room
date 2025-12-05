package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for Puzzle 4.
 * Manages the answer checking, hint toggle, reward letter display,
 * and returning to the Room 3 main menu.
 * Author: Kirtan Patel
 */
public class Room3Puzzle4Controller implements Initializable {

    @FXML private ImageView bgImageA;
    @FXML private Label questionLabel;
    @FXML private TextField answerFieldA;
    @FXML private AnchorPane hintPane;
    @FXML private Label rewardLetter;
    @FXML private Label feedbackLabel;
    @FXML private Label hintTextLabel;

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
     * Toggles the hint panel when the user clicks the hint button.
     */
    @FXML
    private void onHintA(MouseEvent event) {
        if (hintPane == null) return;
        boolean now = !hintPane.isVisible();
        hintPane.setVisible(now);
        hintPane.toFront();
        System.out.println("Hint toggled -> " + now);
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

        if (answerFieldA == null) return;

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

            if (rewardLetter != null) {
                rewardLetter.setText(REWARD);
                rewardLetter.setVisible(true);
                rewardLetter.setStyle(
                        "-fx-font-family: 'Felix Titling'; -fx-font-size: 170px; " +
                        "-fx-text-fill: #d2735d; -fx-font-weight: bold;"
                );
                rewardLetter.toFront();
            }

            answerFieldA.setDisable(true);

        } else {
            if (feedbackLabel != null) {
                feedbackLabel.setText("Incorrect! Try again.");
            }
            System.out.println("Puzzle4: wrong attempt -> " + attempt);
        }
    }

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
}
