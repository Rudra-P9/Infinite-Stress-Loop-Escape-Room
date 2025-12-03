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

    private final String[] allowed = {"E", "C", "H", "O"};

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

        // Only E C H O turn green
        boolean isCorrect = java.util.Arrays.asList(allowed).contains(letter);

        if (isCorrect) {
            clicked.setTextFill(Color.LIMEGREEN);
            clicked.setStyle(clicked.getStyle() + "; -fx-effect: dropshadow(gaussian, limegreen, 20, 0.8, 0, 0);");
        }
    }

    @FXML
    private void goToNext(MouseEvent event) throws Exception{

        App.setRoot("RoomOneBoard");
        System.out.println("Clicked");
    }
}
