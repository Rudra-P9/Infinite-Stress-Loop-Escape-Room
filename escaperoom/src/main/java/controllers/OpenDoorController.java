package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import com.escape.App;

/**
 * Controller for the open door end screen.
 *
 * @author Dylan Diaz
 */
public class OpenDoorController {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView bgImage;

    /**
     * Initializes background scaling and font loading.
     */
    @FXML
    private void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Storm Gust.ttf"), 72);

        bgImage.fitWidthProperty().bind(rootPane.widthProperty());
        bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        bgImage.setPreserveRatio(false);
    }

    /**
     * Returns to the main menu screen.
     *
     * @param event mouse click on the main menu control
     * @throws IOException if the scene cannot be loaded
     */
    @FXML
    private void goToMainMenu(MouseEvent event) throws IOException {
        App.setRoot("MainScreen");
    }

    /**
     * Navigates to the leaderboard screen.
     *
     * @param event mouse click on the leaderboard control
     * @throws IOException if the scene cannot be loaded
     */
    @FXML
    private void goToLeaderboard(MouseEvent event) throws IOException {
        App.setRoot("Leaderboard");
    }
}