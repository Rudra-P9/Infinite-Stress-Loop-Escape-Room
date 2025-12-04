package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class FinalPuzzleController {

    @FXML private ImageView hintIcon;    // the clickable hint icon
    @FXML private AnchorPane hintPane;   // the overlay pane that appears when hint is shown
    @FXML private Label hintText;        // the label inside the hint pane
    @FXML private ImageView closeHint;   // the little close arrow

    @FXML
    private void initialize() {
        // Make sure the hint panel starts hidden
        if (hintPane != null) {
            hintPane.setVisible(false);
        }
    }

    @FXML
    private void showHint(MouseEvent event) {
        // Show the panel with the hint text
        if (hintPane != null) {
            hintPane.setVisible(true);
        }
    }

    @FXML
    private void hideHint(MouseEvent event) {
        // Hide the hint panel again
        if (hintPane != null) {
            hintPane.setVisible(false);
        }
    }
}