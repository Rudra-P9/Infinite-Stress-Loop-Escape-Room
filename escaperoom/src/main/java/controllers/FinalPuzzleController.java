package controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class FinalPuzzleController {

    @FXML private ImageView hintIcon;    
    @FXML private AnchorPane hintPane;   
    @FXML private Label hintText;        
    @FXML private ImageView closeHint;   

    @FXML
    private void initialize() {
        if (hintPane != null) {
            hintPane.setVisible(false);
        }
    }

    @FXML
    private void showHint(MouseEvent event) {
        if (hintPane != null) {
            hintPane.setVisible(true);
        }
    }

    @FXML
    private void hideHint(MouseEvent event) {
        if (hintPane != null) {
            hintPane.setVisible(false);
        }
    }
}