package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import com.escape.App;

/**
 * Main Screen Controller class
 *
 * @author Rudra Patel
 */

public class MainScreenController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private Rectangle clickArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load custom font
        try {
            String fontPath = "/fonts/Storm Gust.ttf";
            if (getClass().getResource(fontPath) != null) {
                Font customFont = Font.loadFont(getClass().getResourceAsStream(fontPath), 150);
                if (customFont != null) {
                    titleLabel.setFont(customFont);
                }
            } else {
                System.out.println("Font file not found at: " + fontPath);
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        }

        // Set initial opacity to 0
        titleLabel.setOpacity(0);

        // Create FadeTransition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), titleLabel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);

        // Start animation
        fadeTransition.play();

        // Pulse Animation for Click Area
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), clickArea);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();

        FadeTransition borderFade = new FadeTransition(Duration.seconds(1), clickArea);
        borderFade.setFromValue(0.6);
        borderFade.setToValue(1.0);
        borderFade.setCycleCount(Animation.INDEFINITE);
        borderFade.setAutoReverse(true);
        borderFade.play();
    }

    @FXML
    private void goToTerminalMenu(MouseEvent event) throws IOException {
        App.setRoot("TerminalMenu");
    }
}
