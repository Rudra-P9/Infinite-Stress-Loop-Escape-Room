package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;

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

    private final String[] order = {"UnclickedR","UnclickedE","UnclickedA","UnclickedL","UnclickedM"};
    private int index = 0;

    private final Map<String,String> clickedMap = new HashMap<>();
    private final Map<String,String> unclickedMap = new HashMap<>();

    @FXML
    private void initialize() {

        Font.loadFont(getClass().getResourceAsStream("/fonts/Storm Gust.ttf"), 42);

        clickedMap.put("UnclickedR","/images/ClickedR.png");
        clickedMap.put("UnclickedE","/images/ClickedE.png");
        clickedMap.put("UnclickedA","/images/ClickedA.png");
        clickedMap.put("UnclickedL","/images/ClickedL.png");
        clickedMap.put("UnclickedM","/images/ClickedM.png");

        unclickedMap.put("UnclickedR","/images/UnclickedR.png");
        unclickedMap.put("UnclickedE","/images/UnclickedE.png");
        unclickedMap.put("UnclickedA","/images/UnclickedA.png");
        unclickedMap.put("UnclickedL","/images/UnclickedL.png");
        unclickedMap.put("UnclickedM","/images/UnclickedM.png");

        hintBottomFlow.setVisible(false);
        hintBottomFlow.setOpacity(0.0);

        monitorPane.setVisible(false);
        monitorPane.setManaged(false);

        RealmImage.setVisible(false);

        AnchorPane parent = (AnchorPane) bgImage.getParent();
        bgImage.fitWidthProperty().bind(parent.widthProperty());
        bgImage.fitHeightProperty().bind(parent.heightProperty());
        bgImage.setPreserveRatio(false);
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
        monitorPane.setOpacity(1.0);

        RealmImage.setVisible(false);

        index = 0;
    }

    private void setImage(ImageView iv, String path) {
        iv.setImage(new Image(getClass().getResourceAsStream(path)));
    }

    private void onComplete() {
        RealmImage.setVisible(true);
    }

    @FXML
    private void onRealmClicked(MouseEvent event) {
        AnchorPane parent = (AnchorPane) monitorPane.getParent();
        double parentH = parent.getHeight();
        double paneH = monitorPane.getPrefHeight() > 0 ? monitorPane.getPrefHeight() : 700.0;
        double finalY = (parentH - paneH) / 2.0;

        monitorPane.setManaged(false);
        monitorPane.setVisible(true);
        monitorPane.setLayoutY(finalY);
        monitorPane.setOpacity(0.0);

        FadeTransition paneFade = new FadeTransition(Duration.millis(600), monitorPane);
        paneFade.setFromValue(0.0);
        paneFade.setToValue(1.0);
        paneFade.setOnFinished(e -> monitorPane.setManaged(true));
        paneFade.play();

        missionLabel.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.millis(700), missionLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setDelay(Duration.millis(200));
        ft.play();
    }

    @FXML
    private void hideMonitor(MouseEvent event) {
        FadeTransition paneFade = new FadeTransition(Duration.millis(400), monitorPane);
        paneFade.setFromValue(monitorPane.getOpacity());
        paneFade.setToValue(0.0);
        paneFade.setOnFinished(e -> {
            monitorPane.setVisible(false);
            monitorPane.setManaged(false);
            monitorPane.setOpacity(1.0);
        });
        paneFade.play();
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
}