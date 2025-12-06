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
            
            // Ensure gameFacade exists
            if (App.gameFacade == null) {
                App.gameFacade = new com.escape.model.EscapeRoomFacade();
            }
            
            App.gameFacade.setCurrentUser(user);
            
            // Restore user's progress to check if they have a saved game
            App.gameFacade.restoreProgressForCurrentUser();
            com.escape.model.Progress progress = App.gameFacade.getProgress();
            
            System.out.println("[Login] User logged in: " + username);
            if (progress != null) {
                System.out.println("[Login] Progress found - StoryPos: " + progress.getStoryPos() 
                    + ", Room: " + progress.getCurrentRoomID() 
                    + ", Difficulty: " + progress.getDifficulty()
                    + ", TimeRemaining: " + progress.getTimeRemainingSeconds());
            } else {
                System.out.println("[Login] No progress found for user");
            }

            // Navigate after 1 second
            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                    javafx.util.Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    // Check if user has saved progress
                    // Consider saved game if: has room ID, difficulty, or non-default time
                    // (Don't rely solely on storyPos since user might save before solving puzzles)
                    boolean hasSavedGame = progress != null && (
                        (progress.getCurrentRoomID() != null && !progress.getCurrentRoomID().isEmpty()) ||
                        (progress.getDifficulty() != null && !progress.getDifficulty().isEmpty()) ||
                        progress.getTimeRemainingSeconds() > 0
                    );
                    
                    System.out.println("[Login] hasSavedGame check: " + hasSavedGame);
                    
                    if (hasSavedGame) {
                        // User has saved game - restore everything and skip difficulty menu
                        System.out.println("[Login] *** RESTORING SAVED GAME ***");
                        System.out.println("  Story Position: " + progress.getStoryPos());
                        System.out.println("  Room: " + progress.getCurrentRoomID());
                        System.out.println("  Time Remaining: " + progress.getTimeRemainingSeconds() + " seconds");
                        System.out.println("  Difficulty: " + progress.getDifficulty());
                        
                        // Restore difficulty from saved progress
                        String savedDifficulty = progress.getDifficulty();
                        if (savedDifficulty != null && !savedDifficulty.isEmpty()) {
                            try {
                                App.currentDifficulty = com.escape.model.Difficulty.valueOf(savedDifficulty);
                            } catch (IllegalArgumentException ex) {
                                System.err.println("[Login] Invalid difficulty: " + savedDifficulty);
                                App.currentDifficulty = com.escape.model.Difficulty.EASY;
                            }
                        } else {
                            App.currentDifficulty = com.escape.model.Difficulty.EASY;
                        }
                        
                        // Start game with saved difficulty
                        App.gameFacade.startGame(App.currentDifficulty);
                        
                        // Restore timer to saved time
                        if (App.gameFacade.getTimer() != null && progress.getTimeRemainingSeconds() > 0) {
                            App.gameFacade.getTimer().setRemainingSeconds(progress.getTimeRemainingSeconds());
                            App.gameFacade.getTimer().start();
                            System.out.println("[Login] Timer restored to " + progress.getTimeRemainingSeconds() + " seconds");
                        }
                        
                        // Navigate to the saved room or ChamberHall if no specific room saved
                        String savedRoom = progress.getCurrentRoomID();
                        if (savedRoom != null && !savedRoom.isEmpty()) {
                            // Map room IDs to their FXML files
                            String fxmlFile = mapRoomIDToFXML(savedRoom);
                            System.out.println("[Login] Navigating to saved room: " + fxmlFile);
                            App.setRoot(fxmlFile);
                        } else {
                            System.out.println("[Login] No saved room, going to ChamberHall");
                            App.setRoot("ChamberHall");
                        }
                    } else {
                        // No saved game - new game, go to difficulty selection
                        System.out.println("[Login] No saved game found - going to difficulty selection");
                        App.setRoot("DifficultyMenu");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("[Login] Navigation failed: " + ex.getMessage());
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
    
    /**
     * Maps room IDs to their corresponding FXML file names.
     * Extend this method as you add more rooms to your game.
     */
    private String mapRoomIDToFXML(String roomID) {
        if (roomID == null) return "ChamberHall";
        
        switch (roomID.toLowerCase()) {
            case "room1":
            case "room2":
            case "room3":
            case "final":
                // For all saved rooms, return to ChamberHall (Calibration Hall)
                return "ChamberHall";
            default:
                System.out.println("[Login] Unknown room ID: " + roomID + ", defaulting to ChamberHall");
                return "ChamberHall";
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