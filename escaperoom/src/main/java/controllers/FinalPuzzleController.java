package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class FinalPuzzleController {

    @FXML private ImageView FPHintIcon;
    @FXML private AnchorPane hintPane;
    @FXML private TextFlow hintBottomFlow;

    @FXML
    private void initialize() {
        javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/Storm Gust.ttf"), 36);
        if (hintPane != null) hintPane.setVisible(false);
        if (hintBottomFlow != null) {
            hintBottomFlow.setVisible(false);
            hintBottomFlow.setOpacity(0.0);
        }
        if (FPHintIcon != null) FPHintIcon.setFocusTraversable(false);
    }

    @FXML
    private void showHint(MouseEvent event) {
        if (FPHintIcon != null) FPHintIcon.setVisible(false);
        if (hintPane != null) hintPane.setVisible(false);
        if (hintBottomFlow != null) {
            hintBottomFlow.setOpacity(0.0);
            hintBottomFlow.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(450), hintBottomFlow);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
        }
    }

    @FXML
    private void hideHint(MouseEvent event) {
        if (hintBottomFlow != null && hintBottomFlow.isVisible()) {
            FadeTransition ft = new FadeTransition(Duration.millis(300), hintBottomFlow);
            ft.setFromValue(hintBottomFlow.getOpacity());
            ft.setToValue(0.0);
            ft.setOnFinished(e -> hintBottomFlow.setVisible(false));
            ft.play();
        }
        if (FPHintIcon != null) FPHintIcon.setVisible(true);
    }
}