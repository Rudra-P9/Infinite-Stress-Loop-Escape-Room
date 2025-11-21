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
import javafx.scene.input.MouseEvent;

import com.escape.App;

/**
 *
 * @author Rudra Patel
 * @author Jacob Kinard
 */
public class LandingController implements Initializable {

    @FXML
    private void goToLogin(MouseEvent event) throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void goToCreateAccount(MouseEvent event) throws IOException {
        App.setRoot("CreateAccount");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}