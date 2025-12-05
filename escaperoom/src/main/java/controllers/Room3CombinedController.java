package controllers;   // <- make this EXACTLY match your folder/package

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;                // correct Group class
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Room3CombinedController implements Initializable {

    @FXML private ImageView bgImage;            // optional debug
    @FXML private Group dialogRoot;             // <Group fx:id="dialogRoot"> in FXML
    @FXML private Button infoButtonRm3;         // ? button (must be outside dialog)
    @FXML private Button acknowledgeButtonRm3;  // ACK inside dialog

    @FXML private Button leftVaultBtn;
    @FXML private Button rightVaultBtn;
    @FXML private Button coreBtn;

    @FXML private Group hotspotGroup;           // if you used Group for hotspots

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // start with dialog visible (intro shown)
        if (dialogRoot != null) dialogRoot.setVisible(true);

        // info button should always be visible (top-right)
        if (infoButtonRm3 != null) infoButtonRm3.setVisible(true);

        // Debug prints so you can see actual nodes injected
        System.out.println("Room3CombinedController initialized.");
        System.out.println(" dialogRoot = " + (dialogRoot != null));
        System.out.println(" infoButtonRm3 = " + (infoButtonRm3 != null));
        System.out.println(" acknowledgeButtonRm3 = " + (acknowledgeButtonRm3 != null));
        System.out.println(" leftVaultBtn = " + (leftVaultBtn != null));
        System.out.println(" rightVaultBtn = " + (rightVaultBtn != null));
        System.out.println(" coreBtn = " + (coreBtn != null));
        System.out.println(" hotspotGroup = " + (hotspotGroup != null));
        System.out.println(" dialog visible = " + (dialogRoot != null && dialogRoot.isVisible()));
    }

    // ACK - hide the intro overlay
    @FXML
    private void onAcknowledgeRm3(MouseEvent event) {
        System.out.println("ACK pressed ? hiding intro overlay.");
        if (dialogRoot != null) dialogRoot.setVisible(false);
        // make sure info is visible so user can reopen dialog
        if (infoButtonRm3 != null) infoButtonRm3.setVisible(true);
    }

    // question mark - show the intro overlay
    @FXML
    private void onInfoRm3(MouseEvent event) {
        System.out.println("Info pressed ? showing intro overlay.");
        if (dialogRoot != null) dialogRoot.setVisible(true);
        if (infoButtonRm3 != null) infoButtonRm3.setVisible(true);
    }

    // Left vault clicked
    @FXML
    private void onLeftVaultClicked(MouseEvent event) {
        System.out.println("Left vault clicked - open puzzle A");
        // TODO: navigate, e.g. com.escape.App.setRoot("RoomThreeLeft");
    }

    // Right vault clicked
    @FXML
    private void onRightVaultClicked(MouseEvent event) {
        System.out.println("Right vault clicked - open puzzle B");
        // TODO: navigate
    }

    // Core clicked
    @FXML
    private void onCoreClicked(MouseEvent event) {
        System.out.println("Core clicked - open final puzzle");
        // TODO: navigate
    }
}
