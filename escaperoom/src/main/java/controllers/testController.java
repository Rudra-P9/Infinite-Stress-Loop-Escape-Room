/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.escape.App;

/**
 *
 * @author Jacob Kinard
 */
public class testController implements Initializable {

    @FXML
    private void onLoginClicked(ActionEvent event) throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void onSignupClicked(ActionEvent event) throws IOException {
        App.setRoot("test");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}