package controllers;

import com.escape.App;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class RoomOneIntroController {

    @FXML
    private void goToNext(MouseEvent event) throws Exception {
        App.setRoot("RoomOnePuzzle");
        System.out.println("CLICKED!"); //test

    }

    @FXML
        private void debugClick(MouseEvent e) {
            System.out.println("TEST BOX CLICKED!");
        }
}