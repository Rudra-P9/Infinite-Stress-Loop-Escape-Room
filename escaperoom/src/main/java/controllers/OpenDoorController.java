package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import com.escape.App;

public class OpenDoorController {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView bgImage;

    @FXML
    private void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Storm Gust.ttf"), 72);

        bgImage.fitWidthProperty().bind(rootPane.widthProperty());
        bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        bgImage.setPreserveRatio(false);
    }

    @FXML
    private void goToMainMenu(MouseEvent event) throws IOException {
        App.setRoot("MainScreen");
    }

     @FXML
    private void goToLeaderboard(MouseEvent event) throws IOException {
        App.setRoot("Leaderboard");
    }
}