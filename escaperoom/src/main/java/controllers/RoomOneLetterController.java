package controllers;

import com.escape.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class RoomOneLetterController {

    @FXML
    private FlowPane letterPane;

    private StringBuilder selectedLetters = new StringBuilder();

    @FXML
    private Label noteText;


    @FXML
    public void initialize() {
        for (var node : letterPane.getChildren()) {
            if (node instanceof Label label) {

                 label.setStyle(
                "-fx-font-size: 75px;" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Times New Roman';"
                );

                // Make letters clickable
                label.setOnMouseClicked(this::handleLetterClick);

                // Optional hover highlight
                label.setOnMouseEntered(e -> label.setOpacity(1.0));
                label.setOnMouseExited(e -> label.setOpacity(0.9));
            }
        }
    }

    private void handleLetterClick(MouseEvent event) {
        Label clicked = (Label) event.getSource();
        String letter = clicked.getText();

    //removes letter on second click
        if (clicked.getTextFill().equals(Color.BLACK)) {

            clicked.setTextFill(Color.WHITE);

            removeLetterFromNote(letter);
            return;
        }

        // Toggle ON
        clicked.setTextFill(Color.BLACK);

        selectedLetters.append(letter);
        noteText.setText(selectedLetters.toString());
    }

    private void removeLetterFromNote(String letter) {
        String current = selectedLetters.toString();

        int index = current.indexOf(letter);
        if (index != -1) {
            selectedLetters.deleteCharAt(index);
            noteText.setText(selectedLetters.toString());
        }
    }

    @FXML
    private void goToNext(MouseEvent event) throws Exception{

        App.setRoot("RoomOneBoard");
        System.out.println("Clicked");
    }


}
