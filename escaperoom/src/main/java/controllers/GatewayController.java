package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import com.escape.App;

/**
 * Controller for the Gateway screen.
 * Handles user input, validation, and account creation.
 * 
 * @author Rudra Patel
 */
public class GatewayController implements Initializable {

    @FXML
    private javafx.scene.layout.AnchorPane rootPane;

    @FXML
    private Label introLabel;

    @FXML
    private Label enterLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ensure rootPane is focused to receive key events
        if (rootPane != null) {
            rootPane.setFocusTraversable(true);
            javafx.application.Platform.runLater(() -> rootPane.requestFocus());

            // Clip content to 1920x1080 to prevent layout distortion
            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(1920, 1080);
            rootPane.setClip(clip);
        }

        // Flash introLabel twice
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), introLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(6); // 3 flashes (out-in-out-in-out-in)
        fadeTransition.setAutoReverse(true);

        // Ensure it ends visible
        fadeTransition.setOnFinished(event -> introLabel.setOpacity(1.0));

        fadeTransition.play();
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            App.setRoot("primary");
        }
    }
}
