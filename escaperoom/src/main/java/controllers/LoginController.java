package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import com.escape.App;

/**
 *
 * @author Rudra Patel
 * @author Jacob Kinard
 */

public class LoginController implements Initializable {

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("Landing");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}