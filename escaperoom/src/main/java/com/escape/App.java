package com.escape;

import java.io.IOException;

import com.escape.model.Difficulty;
import com.escape.model.User;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 * Handles the main application logic and state management.
 * 
 * @author Rudra Patel
 */
public class App extends Application {

    public static User currentUser;
    public static Difficulty currentDifficulty;
    public static com.escape.model.EscapeRoomFacade gameFacade;

    private static Scene scene;
    private static boolean transitionSoundsEnabled = false;
    
    // Define the target resolution (design resolution)
    private static final double TARGET_WIDTH = 1920;
    private static final double TARGET_HEIGHT = 1080;

    @Override
    public void start(Stage stage) throws IOException {

        if (gameFacade == null) {
            gameFacade = new com.escape.model.EscapeRoomFacade();
        }

        // Initialize audio controller and ensure SFX is enabled
        controllers.AudioController audio = controllers.AudioController.getInstance();
        audio.setSfxEnabled(true);
        audio.setMusicEnabled(true);
        System.out.println("[App] Audio initialized - SFX: " + audio.isSfxEnabled() + ", Music: " + audio.isMusicEnabled());

        // Load the initial view wrapped in the scaler
        Parent root = loadFXML("MainScreen");

        // Create scene with the scalable root
        scene = new Scene(root, 640, 480);
        scene.setFill(Color.BLACK); // Letterbox color

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        // Play woosh sound on screen transitions (after reaching ChamberHall)
        controllers.AudioController audio = controllers.AudioController.getInstance();
        
        if (transitionSoundsEnabled && !fxml.equals("MainScreen") && !fxml.equals("Login")) {
            // Special case for Room3Combined - play deep woosh
            if (fxml.equals("Room3Combined")) {
                audio.playSoundEffect("audio/deep-woosh.wav");
            } else {
                audio.playSoundEffect("audio/woosh-mark_diangelo-4778593.wav");
            }
        }
        
        // Enable transition sounds once we reach ChamberHall
        if (fxml.equals("ChamberHall")) {
            transitionSoundsEnabled = true;
        }
        
        // Disable when returning to main menu or login
        if (fxml.equals("MainScreen") || fxml.equals("Login")) {
            transitionSoundsEnabled = false;
        }
        
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        java.net.URL fxmlUrl = App.class.getResource(fxml + ".fxml");
        // Try absolute path fallback if relative lookup fails
        if (fxmlUrl == null) {
            fxmlUrl = App.class.getResource("src/main/resources/com/escape/" + fxml + ".fxml");
        }

        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML resource not found: '" + fxml + ".fxml' -- looked for: '" + fxml
                    + ".fxml' relative to " + App.class.getName() + " and '/com/escape/" + fxml + ".fxml'.\n" +
                    "Ensure the FXML file is located in 'src/main/resources/com/escape/' and is included on the module/classpath.");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        Parent content = fxmlLoader.load();
        return makeScalable(content);
    }

    /**
     * Wraps the content in a scaling container that preserves aspect ratio.
     */
    private static Parent makeScalable(Parent content) {
        // Ensure the content has the target size
        if (content instanceof javafx.scene.layout.Region region) {
            region.setPrefSize(TARGET_WIDTH, TARGET_HEIGHT);
            region.setMinSize(TARGET_WIDTH, TARGET_HEIGHT);
            region.setMaxSize(TARGET_WIDTH, TARGET_HEIGHT);
        }

        // Wrap in a Group to isolate transformations
        Group contentGroup = new Group(content);

        // Wrap in a StackPane to center the Group
        StackPane stackPane = new StackPane(contentGroup);
        stackPane.setStyle("-fx-background-color: black;"); // Optional: ensure background is black

        // Bind scale properties

        // Calculate the scale factor
        DoubleProperty scaleFactor = new SimpleDoubleProperty();

        scaleFactor.bind(Bindings.min(
                stackPane.widthProperty().divide(TARGET_WIDTH),
                stackPane.heightProperty().divide(TARGET_HEIGHT)));

        contentGroup.scaleXProperty().bind(scaleFactor);
        contentGroup.scaleYProperty().bind(scaleFactor);

        return stackPane;
    }

    public static void main(String[] args) {
        launch();
    }

}