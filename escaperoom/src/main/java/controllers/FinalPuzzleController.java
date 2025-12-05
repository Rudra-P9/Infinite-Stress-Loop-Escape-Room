package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.escape.App;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class FinalPuzzleController {

    @FXML private ImageView bgImage;
    @FXML private ImageView FPHintIcon;
    @FXML private AnchorPane hintPane;
    @FXML private TextFlow hintBottomFlow;

    @FXML private ImageView UnclickedR;
    @FXML private ImageView UnclickedE;
    @FXML private ImageView UnclickedA;
    @FXML private ImageView UnclickedL;
    @FXML private ImageView UnclickedM;

    @FXML private ImageView RealmImage;

    @FXML private AnchorPane monitorPane;
    @FXML private Label missionLabel;
    @FXML private ImageView exitButton;
    @FXML private Label penaltyLabel;

    @FXML private AnchorPane introPane;
    @FXML private Label introLabel;
    @FXML private ImageView introExitButton;

    @FXML private Label timerLabel;

    private final String[] order = {"UnclickedR", "UnclickedE", "UnclickedA", "UnclickedL", "UnclickedM"};
    private int index = 0;

    private final Map<String, String> clickedMap = new HashMap<>();
    private final Map<String, String> unclickedMap = new HashMap<>();

    private com.escape.model.EscapeRoomFacade facade;
    private javafx.animation.Timeline timerTimeline;

    @FXML
    private void initialize() {

        Font.loadFont(getClass().getResourceAsStream("/fonts/Storm Gust.ttf"), 42);

        clickedMap.put("UnclickedR", "/images/ClickedR.png");
        clickedMap.put("UnclickedE", "/images/ClickedE.png");
        clickedMap.put("UnclickedA", "/images/ClickedA.png");
        clickedMap.put("UnclickedL", "/images/ClickedL.png");
        clickedMap.put("UnclickedM", "/images/ClickedM.png");

        unclickedMap.put("UnclickedR", "/images/UnclickedR.png");
        unclickedMap.put("UnclickedE", "/images/UnclickedE.png");
        unclickedMap.put("UnclickedA", "/images/UnclickedA.png");
        unclickedMap.put("UnclickedL", "/images/UnclickedL.png");
        unclickedMap.put("UnclickedM", "/images/UnclickedM.png");

        hintBottomFlow.setVisible(false);
        hintBottomFlow.setOpacity(0.0);

        monitorPane.setVisible(false);
        monitorPane.setManaged(false);

        RealmImage.setVisible(false);

        AnchorPane parent = (AnchorPane) bgImage.getParent();
        bgImage.fitWidthProperty().bind(parent.widthProperty());
        bgImage.fitHeightProperty().bind(parent.heightProperty());
        bgImage.setPreserveRatio(false);

        introPane.setVisible(true);
        introPane.setManaged(true);

        setPuzzleVisible(false);

        this.facade = App.gameFacade;
        startTimerUpdate();
    }

    private void setPuzzleVisible(boolean visible) {
        FPHintIcon.setVisible(visible);
        FPHintIcon.setDisable(!visible);

        UnclickedR.setVisible(visible);
        UnclickedE.setVisible(visible);
        UnclickedA.setVisible(visible);
        UnclickedL.setVisible(visible);
        UnclickedM.setVisible(visible);

        UnclickedR.setDisable(!visible);
        UnclickedE.setDisable(!visible);
        UnclickedA.setDisable(!visible);
        UnclickedL.setDisable(!visible);
        UnclickedM.setDisable(!visible);
    }

    @FXML
    private void hideIntroPane(MouseEvent event) {
        introPane.setVisible(false);
        introPane.setManaged(false);
        setPuzzleVisible(true);
    }

    @FXML
    private void onLetterClicked(MouseEvent event) {
        ImageView iv = (ImageView) event.getSource();
        String id = iv.getId();

        if (order[index].equals(id)) {
            iv.setImage(new Image(getClass().getResourceAsStream(clickedMap.get(id))));
            iv.setDisable(true);
            index++;
            if (index == order.length)
                onComplete();
            return;
        }
        resetAll();
    }

    private void resetAll() {
        if (facade != null) {
            facade.applyHintPenalty();
            updateTimer();
        }

        if (penaltyLabel != null) {
            penaltyLabel.setOpacity(1.0);
            FadeTransition fade = new FadeTransition(Duration.seconds(2.0), penaltyLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        }

        setImage(UnclickedR, unclickedMap.get("UnclickedR"));
        setImage(UnclickedE, unclickedMap.get("UnclickedE"));
        setImage(UnclickedA, unclickedMap.get("UnclickedA"));
        setImage(UnclickedL, unclickedMap.get("UnclickedL"));
        setImage(UnclickedM, unclickedMap.get("UnclickedM"));

        UnclickedR.setDisable(false);
        UnclickedE.setDisable(false);
        UnclickedA.setDisable(false);
        UnclickedL.setDisable(false);
        UnclickedM.setDisable(false);

        monitorPane.setVisible(false);
        monitorPane.setManaged(false);
        RealmImage.setVisible(false);

        index = 0;
    }

    private void setImage(ImageView iv, String path) {
        iv.setImage(new Image(getClass().getResourceAsStream(path)));
    }

    private void onComplete() {
        RealmImage.setVisible(true);
        onCompleteFinalPuzzle(); // ← keep your logic
    }

    @FXML
    private void onRealmClicked(MouseEvent event) throws IOException {
        AnchorPane parent = (AnchorPane) monitorPane.getParent();
        double parentH = parent.getHeight();
        double paneH = monitorPane.getPrefHeight() > 0 ? monitorPane.getPrefHeight() : 700.0;
        double finalY = (parentH - paneH) / 2.0;

        monitorPane.setManaged(false);
        monitorPane.setVisible(true);
        monitorPane.setLayoutY(finalY);
        monitorPane.setOpacity(0.0);

        FadeTransition fade = new FadeTransition(Duration.millis(600), monitorPane);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setOnFinished(e -> monitorPane.setManaged(true));
        fade.play();

        missionLabel.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(700), missionLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setDelay(Duration.millis(200));
        ft.play();
    }

    @FXML
    private void hideMonitor(MouseEvent event) throws IOException {
        App.setRoot("OpenDoor");
    }

    @FXML
    private void showHint(MouseEvent event) {
        hintBottomFlow.setOpacity(0.0);
        hintBottomFlow.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(450), hintBottomFlow);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        FPHintIcon.setVisible(false);
    }

    @FXML
    private void hideHint(MouseEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), hintBottomFlow);
        ft.setFromValue(hintBottomFlow.getOpacity());
        ft.setToValue(0.0);
        ft.setOnFinished(e -> hintBottomFlow.setVisible(false));
        ft.play();
        FPHintIcon.setVisible(true);
    }

    /** TIMER SYSTEM */
    private void startTimerUpdate() {
        timerTimeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> updateTimer()));
        timerTimeline.setCycleCount(javafx.animation.Timeline.INDEFINITE);
        timerTimeline.play();
        updateTimer();
    }

    private void updateTimer() {
        if (facade != null && timerLabel != null) {
            int remainingSeconds = facade.getTimeRemaining();
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

            if (remainingSeconds < 60)
                timerLabel.setTextFill(javafx.scene.paint.Color.RED);
            else
                timerLabel.setTextFill(javafx.scene.paint.Color.LIME);
        }
    }

    /** FINAL PUZZLE END GAME */
    private void onCompleteFinalPuzzle() {
        if (App.gameFacade != null) {
            App.gameFacade.endGame();
        }
    }
}