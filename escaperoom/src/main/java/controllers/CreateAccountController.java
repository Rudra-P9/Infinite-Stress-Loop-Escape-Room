package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.escape.App;
import com.escape.model.Accounts;
import com.escape.model.GameDataWriter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Controller for the Create Account screen.
 * Handles user input, validation, and account creation.
 * 
 * @author Rudra Patel
 */
public class CreateAccountController implements Initializable {

    @FXML
    private Label welcomeCreateAccountLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailField;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label enterButtonLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label backLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        typeText(welcomeCreateAccountLabel, "[ Facility 67 Termalink: Create Account ]", () -> {
            typeText(emailLabel, "[ Email ]", () -> {
                emailField.setVisible(true);
                typeText(usernameLabel, "[ Username ]", () -> {
                    usernameField.setVisible(true);
                    typeText(passwordLabel, "[ Password ]", () -> {
                        passwordField.setVisible(true);
                        typeText(enterButtonLabel, "[ Enter ]", () -> {
                            enterButtonLabel.setVisible(true); // Ensure visible
                            typeText(loginLabel, "[ Login ]", () -> {
                                typeText(backLabel, "[ Back ]", null);
                            });
                        });
                    });
                });
            });
        });
    }

    @FXML
    private void handleCreateAccount(MouseEvent event) {
        createAccount();
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            createAccount();
        }
    }

    private void createAccount() {
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validation
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            welcomeCreateAccountLabel.setText("[ All Fields Required ]");
            welcomeCreateAccountLabel.setTextFill(javafx.scene.paint.Color.web("#ff0000"));
            return;
        }

        Accounts accounts = Accounts.getInstance();
        if (accounts.getUser(username) != null) {
            welcomeCreateAccountLabel.setText("[ Username Taken ]");
            welcomeCreateAccountLabel.setTextFill(javafx.scene.paint.Color.web("#ff0000"));
            return;
        }

        if (accounts.getUserByEmail(email) != null) {
            welcomeCreateAccountLabel.setText("[ Email Taken ]");
            welcomeCreateAccountLabel.setTextFill(javafx.scene.paint.Color.web("#ff0000"));
            return;
        }

        // Create Account
        accounts.createAccount(username, password, email);

        // Save Data
        GameDataWriter writer = new GameDataWriter();
        writer.saveAccounts(accounts);

        // Success Feedback
        welcomeCreateAccountLabel.setText("[ Account Created ]");
        welcomeCreateAccountLabel.setTextFill(javafx.scene.paint.Color.web("#00ff00"));

        // Navigate to Login after 2 seconds
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(e -> {
            try {
                App.setRoot("Login");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        pause.play();
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        App.setRoot("TerminalMenu");
    }

    @FXML
    private void goToLogin(MouseEvent event) throws IOException {
        App.setRoot("Login");
    }

    private void typeText(Label label, String text, Runnable onFinished) {
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
