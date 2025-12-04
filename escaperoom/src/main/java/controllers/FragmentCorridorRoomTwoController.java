package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Fragment Corridor Room Two screen.
 * Handles directional button clicks (UP, DOWN, LEFT, RIGHT).
 * 
 * @author Jacob Kinard
 */
public class FragmentCorridorRoomTwoController implements Initializable {

    @FXML
    private Button UP;

    @FXML
    private Button DOWN;

    @FXML
    private Button LEFT;

    @FXML
    private Button RIGHT;

    /**
     * Initializes the controller.
     * 
     * @param url the location used to resolve relative paths for the root object, or null
     * @param resourceBundle the resources used to localize the root object, or null
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic if needed
    }

    /**
     * Handles UP button click events.
     * 
     * @param event the mouse event triggered by clicking the UP button
     */
    @FXML
    void PressedUp(MouseEvent event) {
        System.out.println("UP button pressed");
        // Add your logic here
    }

    /**
     * Handles DOWN button click events.
     * 
     * @param event the mouse event triggered by clicking the DOWN button
     */
    @FXML
    void PressedDown(MouseEvent event) {
        System.out.println("DOWN button pressed");
        // Add your logic here
    }

    /**
     * Handles LEFT button click events.
     * 
     * @param event the mouse event triggered by clicking the LEFT button
     */
    @FXML
    void PressedLeft(MouseEvent event) {
        System.out.println("LEFT button pressed");
        // Add your logic here
    }

    /**
     * Handles RIGHT button click events.
     * 
     * @param event the mouse event triggered by clicking the RIGHT button
     */
    @FXML
    void PressedRight(MouseEvent event) {
        System.out.println("RIGHT button pressed");
        // Add your logic here
    }

}
