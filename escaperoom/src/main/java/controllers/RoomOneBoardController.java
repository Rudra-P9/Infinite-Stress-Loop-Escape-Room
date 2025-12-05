package controllers;

import com.escape.App;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for the Room One Board
 * Handles the logic for the Room One Board
 * 
 * @author Talan
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
    public void initialize() {
        hintPane.setVisible(false);
        loadRandomRiddle();
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

            var stream = getClass().getResourceAsStream("/com/escape/model/riddles.txt");
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
                String randomLine = lines.get(rand.nextInt(lines.size()));
                // Split by "::" and take the first part
                String[] parts = randomLine.split("::");
                if (parts.length > 0) {
                    String riddleText = parts[0].trim();
                    riddleLabel.setText(riddleText);

                    String answerText = "";
                    if (parts.length > 1) {
                        answerText = parts[1].trim();
                    }

                    String hintText = "";
                    if (parts.length > 2) {
                        hintText = parts[2].trim();
                    }

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

        // load hint directly from JSON
        String hint = loadHintFromJson();
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

    private String loadHintFromJson() {
        try {
            var stream = getClass().getResourceAsStream("/json/game.json");

            org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
            java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
            org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(reader);

            org.json.simple.JSONArray rooms = (org.json.simple.JSONArray) json.get("rooms");
            org.json.simple.JSONObject room1 = (org.json.simple.JSONObject) rooms.get(0);

            org.json.simple.JSONArray puzzles = (org.json.simple.JSONArray) room1.get("puzzles");
            org.json.simple.JSONObject puzzle1 = (org.json.simple.JSONObject) puzzles.get(0);

            return puzzle1.get("hint").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Hint unavailable.";
        }
    }

    @FXML
    private void hideHint(MouseEvent e) {
        hintPane.setVisible(false);
        closeHintArrow.setVisible(false);
    }

}
