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
 * 
 * @author Rudra Patel
 */

public class TerminalMenuController implements Initializable {

    @FXML
    private void goToLogin(MouseEvent event) throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void goToCreateAccount(MouseEvent event) throws IOException {
        App.setRoot("CreateAccount");
    }

    @FXML
    private void goToMainScreen(MouseEvent event) throws IOException {
        App.setRoot("MainScreen");
    }

    @FXML
    private javafx.scene.control.Label welcomeLabel;
    @FXML
    private javafx.scene.control.Label loginLabel;
    @FXML
    private javafx.scene.control.Label newUserLabel;
    @FXML
    private javafx.scene.control.Label backLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeText(welcomeLabel, "[ Welcome To The Facility 67 Termalink ]", () -> {
            typeText(loginLabel, "[ Login ]", () -> {
                typeText(newUserLabel, "[ New User ]", () -> {
                    typeText(backLabel, "[ Back ]", null);
                });
            });
        });
    }

    private void typeText(javafx.scene.control.Label label, String text, Runnable onFinished) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(60 * (i + 1)),
                    event -> {
                        sb.append(text.charAt(index));
                        label.setText(sb.toString());
                    });
            timeline.getKeyFrames().add(keyFrame);
        }

        if (onFinished != null) {
            timeline.setOnFinished(e -> onFinished.run());
        }

        timeline.play();
    }

}