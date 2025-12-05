package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.escape.App;

/**
 * Controller for the Room 3 main menu.
 * Handles showing and hiding the intro dialog and opening the three vault hotspots.
 * Author: Kirtan Patel
 */
public class Room3CombinedController implements Initializable {

    @FXML private ImageView bgImage;
    @FXML private Group dialogRoot;
    @FXML private Button infoButtonRm3;
    @FXML private Button acknowledgeButtonRm3;

    @FXML
    private Button leftVaultBtn;
    @FXML
    private Button rightVaultBtn;
    @FXML
    private Button coreBtn;

    @FXML private Group hotspotGroup;

    /**
     * Runs when the scene first loads.
     * Makes the dialog visible and prints some debug information.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (dialogRoot != null) {
            dialogRoot.setVisible(true);
            dialogRoot.setMouseTransparent(false);
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }

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

    /**
     * Called when the user clicks the ACKNOWLEDGE button.
     * Hides the intro dialog and enables the hotspots behind it.
     */
    @FXML
    private void onAcknowledgeRm3(MouseEvent event) {
        System.out.println("ACK pressed, hiding intro overlay.");

        if (dialogRoot != null) {
            dialogRoot.setVisible(false);
            dialogRoot.setMouseTransparent(true);
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }

        if (hotspotGroup != null) {
            hotspotGroup.toFront();
        }
    }

    /**
     * Called when the user clicks the ? button.
     * Shows the intro dialog again.
     */
    @FXML
    private void onInfoRm3(MouseEvent event) {
        System.out.println("Info pressed, showing intro overlay.");

        if (dialogRoot != null) {
            dialogRoot.setVisible(true);
            dialogRoot.setMouseTransparent(false);
            dialogRoot.toFront();
        }

        if (infoButtonRm3 != null) {
            infoButtonRm3.setVisible(true);
        }
    }

    /**
     * Opens Puzzle 4 when the left vault hotspot is clicked.
     */
    @FXML
    private void onLeftVaultClicked(MouseEvent event) {
        System.out.println("Left vault clicked - open puzzle A");
        try {
            com.escape.App.setRoot("Room3Puzzle4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens Puzzle 5 when the right vault hotspot is clicked.
     * (Navigation added when Puzzle 5 is finished.)
     */
    @FXML
    private void onRightVaultClicked(MouseEvent event) {
        System.out.println("Right vault clicked - open puzzle B");
        try {
            com.escape.App.setRoot("Room3Puzzle5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the final core puzzle when the center hotspot is clicked.
     */
    @FXML
    private void onCoreClicked(MouseEvent event) {
        System.out.println("Core clicked - open final puzzle");
        try {
            com.escape.App.setRoot("FinalPuzzle");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
