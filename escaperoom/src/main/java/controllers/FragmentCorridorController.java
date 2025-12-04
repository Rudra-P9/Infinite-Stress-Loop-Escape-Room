package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Fragment Corridor screen.
 * Manages letter reveal logic where two M keys must be clicked in sequence
 * from different sources to reveal both M letters.
 * 
 * @author Jacob Kinard
 */
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

    /** Tracks which M key revealed the first M (0 = none, 1 = left key, 2 = right key). */
    private int firstMRevealedBy = 0;
    
    /** Whether the first M label has been revealed. */
    private boolean m1Revealed = false;
    
    /** Whether the second M label has been revealed. */
    private boolean m2Revealed = false;

    /**
     * Initializes the controller by hiding letter labels and applying consistent styling.
     * 
     * @param url the location used to resolve relative paths for the root object, or null
     * @param resourceBundle the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ensure the M show labels are initially hidden
        if (M1Show != null) M1Show.setVisible(false);
        if (M2Show != null) M2Show.setVisible(false);
        if (YShow != null) YShow.setVisible(false);
        if (OShow != null) OShow.setVisible(false);
        if (EShow != null) EShow.setVisible(false);
        if (RShow != null) RShow.setVisible(false);

        // Apply consistent letter styling (matches other screens)
        try {
            if (M1Show != null) {
                M1Show.getStyleClass().add("letter");
                M1Show.setStyle("-fx-text-fill: white;");
                M1Show.setFont(javafx.scene.text.Font.font("Times New Roman", 100));
            }
            if (M2Show != null) {
                M2Show.getStyleClass().add("letter");
                M2Show.setStyle("-fx-text-fill: white;");
                M2Show.setFont(javafx.scene.text.Font.font("Times New Roman", 100));
            }
            if (MKey1 != null) {
                MKey1.getStyleClass().add("letter");
                MKey1.setFont(javafx.scene.text.Font.font("Times New Roman", 96));
            }
            if (MKey2 != null) {
                MKey2.getStyleClass().add("letter");
                MKey2.setFont(javafx.scene.text.Font.font("Times New Roman", 77));
            }
        } catch (Exception ignore) {
            // styling optional — ignore failures
        }
    }

    /**
     * Handles M key click events.
     * First click reveals M1, second click from a different key reveals M2.
     * Prevents the same key from revealing both M letters.
     * 
     * @param event the mouse event triggered by clicking an M key
     */
    @FXML
    void MLetterClicked(MouseEvent event) {
        Object src = event.getSource();
        int sourceId = 0; // 1 = left M key, 2 = right M key
        if (src == MKey1 || src == MKey1B) sourceId = 1;
        else if (src == MKey2 || src == Mkey2B) sourceId = 2;

        if (sourceId == 0) return; // ignore unknown sources

        // First reveal: always reveal M1 and record which key triggered it
        if (!m1Revealed) {
            if (M1Show != null) M1Show.setVisible(true);
            m1Revealed = true;
            firstMRevealedBy = sourceId;
            // disable the exact clickable area so it can't be spam-clicked
            if (sourceId == 1 && MKey1B != null) MKey1B.setDisable(true);
            if (sourceId == 2 && Mkey2B != null) Mkey2B.setDisable(true);
            return;
        }

        // Second reveal: only allow if clicked from the other key
        if (!m2Revealed && sourceId != firstMRevealedBy) {
            if (M2Show != null) M2Show.setVisible(true);
            m2Revealed = true;
            if (MKey1B != null) MKey1B.setDisable(true);
            if (Mkey2B != null) Mkey2B.setDisable(true);
        }
        // if same key clicked again, do nothing
    }

}

