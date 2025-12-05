package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import com.escape.App;
import java.io.IOException;

/**
 * Controller for the Inventory screen.
 * Handles the inventory display and back button.
 * 
 * @author Rudra Patel
 */
public class InventoryController {
    @FXML
    private Label A;
    @FXML
    private Label L;
    @FXML
    private Label M;
    @FXML
    private Label R;
    @FXML
    private Label E;
    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        // Initially hide all letters
        if (A != null)
            A.setVisible(false);
        if (L != null)
            L.setVisible(false);
        if (M != null)
            M.setVisible(false);
        if (R != null)
            R.setVisible(false);
        if (E != null)
            E.setVisible(false);

        // Check facade for collected items and reveal them
        if (App.gameFacade != null) {
            if (App.gameFacade.hasItem("A"))
                A.setVisible(true);
            if (App.gameFacade.hasItem("L"))
                L.setVisible(true);
            if (App.gameFacade.hasItem("M"))
                M.setVisible(true);
            if (App.gameFacade.hasItem("R"))
                R.setVisible(true);
            if (App.gameFacade.hasItem("E"))
                E.setVisible(true);
        }
    }

    @FXML
    private void onBackBtnClicked(MouseEvent event) throws IOException {
        App.setRoot("ChamberHall");
        System.out.println("Returning to Chamber Hall from Inventory");
    }

    @FXML
    private void onBack() throws IOException {
        App.setRoot("ChamberHall");
        System.out.println("Returning to Chamber Hall from Inventory");
    }

}
