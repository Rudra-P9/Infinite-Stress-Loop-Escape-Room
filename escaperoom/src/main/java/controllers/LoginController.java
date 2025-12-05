package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escape.App;
import com.escape.model.Accounts;
import com.escape.model.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the Login screen.
 * Handles user input, validation, and account creation.
 * 
 * @author Rudra Patel
 */
public class LoginController implements Initializable {

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("TerminalMenu");
    }

    @FXML
    private void goToCreateAccount(MouseEvent event) throws IOException {
        App.setRoot("CreateAccount");
    }

    @FXML
    private void goToDifficultyMenu(MouseEvent event) throws IOException {
        verifyAndLogin();
    }

    @FXML
    private void handleEnterKey(javafx.scene.input.KeyEvent event) throws IOException {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            verifyAndLogin();
        }
    }

    private void verifyAndLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = Accounts.getInstance().getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            // Login successful
            enterButtonLabel.setText("[ Access Granted ]");
            enterButtonLabel.setTextFill(javafx.scene.paint.Color.LIME);

            // Set global user
            App.currentUser = user;
            App.gameFacade.setCurrentUser(user);

            // Navigate to DifficultyMenu after 1 second
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    App.setRoot("DifficultyMenu");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            pause.play();
        } else {
            // Login failed
            enterButtonLabel.setText("[ Invalid Credentials ]");
            enterButtonLabel.setTextFill(javafx.scene.paint.Color.web("#ff0000"));
            // Reset text after a delay
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(() -> {
                                enterButtonLabel.setText("[ Login ]");
                                enterButtonLabel.setTextFill(javafx.scene.paint.Color.LIME);
                            });
                        }
                    },
                    2000);
        }
    }

    @FXML
    private javafx.scene.control.Label welcomeLoginLabel;
    @FXML
    private javafx.scene.control.Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private javafx.scene.control.Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private javafx.scene.control.Label enterButtonLabel;
    @FXML
    private javafx.scene.control.Label createAccountLabel;
    @FXML
    private javafx.scene.control.Label backLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeText(welcomeLoginLabel, "[ Facility 67 Termalink: Login ]", () -> {
            typeText(usernameLabel, "[ Username ]", () -> {
                usernameField.setVisible(true);
                typeText(passwordLabel, "[ Password ]", () -> {
                    passwordField.setVisible(true);
                    typeText(enterButtonLabel, "[ Login ]", () -> {
                        typeText(createAccountLabel, "[ Create Account ]", () -> {
                            typeText(backLabel, "[ Back ]", null);
                        });
                    });
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