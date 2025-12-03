package controllers;

import com.escape.App;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


public class RoomOneBoardController {

    @FXML
    private AnchorPane hintPane;

    @FXML
    private Label hintText;

    @FXML
    private ImageView closeHintArrow;

    @FXML private ImageView hintNote;

    @FXML
    public void initialize() {
        hintPane.setVisible(false);
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
    private void goToNext(MouseEvent event) throws Exception{

        App.setRoot("RoomOneLetter");
        System.out.println("Clicked");
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

