package controllers;

import com.escape.App;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class RoomOnePuzzleController {

    @FXML
    public void initialize() {
        System.out.println("RoomOnePuzzle loaded successfully!");
    }

     @FXML
    private void goToNext(MouseEvent event) throws Exception {
        App.setRoot("RoomOneBoard");

    }

}

