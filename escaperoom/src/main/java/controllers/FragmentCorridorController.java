package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class FragmentCorridorController implements Initializable {

    @FXML
    private Label EShow;

    @FXML
    private Label M1Show;

    @FXML
    private Label M2Show;

    @FXML
    private Label MKey1;

    @FXML
    private Button MKey1B;

    @FXML
    private Label MKey2;

    @FXML
    private Button Mkey2B;

    @FXML
    private Label OShow;

    @FXML
    private Label RShow;

    @FXML
    private Label YShow;

    // Tracks how many M key clicks have occurred
    private int mClickCount = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ensure the M show labels are initially hidden
        if (M1Show != null) M1Show.setVisible(false);
        if (M2Show != null) M2Show.setVisible(false);
        if (YShow != null) YShow.setVisible(false);
        if (OShow != null) OShow.setVisible(false);
        if (EShow != null) EShow.setVisible(false);
    }

    @FXML
    void MLetterClicked(MouseEvent event) {
        // Reveal M labels in sequence: first click -> M1, second click -> M2
        mClickCount++;
        if (mClickCount == 1) {
            if (M1Show != null) M1Show.setVisible(true);
        } else if (mClickCount == 2) {
            if (M2Show != null) M2Show.setVisible(true);
            // Disable the clickable M keys after both are revealed
            if (MKey1B != null) MKey1B.setDisable(true);
            if (Mkey2B != null) Mkey2B.setDisable(true);
        }
    }

}

