package com.escape;

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

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    // Define the target resolution (design resolution)
    private static final double TARGET_WIDTH = 1920;
    private static final double TARGET_HEIGHT = 1080;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the initial view wrapped in the scaler
        Parent root = loadFXML("Landing");

        // Create scene with the scalable root
        scene = new Scene(root, 640, 480);
        scene.setFill(Color.BLACK); // Letterbox color

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent content = fxmlLoader.load();
        return makeScalable(content);
    }

    /**
     * Wraps the content in a scaling container that preserves aspect ratio.
     */
    private static Parent makeScalable(Parent content) {
        // Ensure the content has the target size
        // If the FXML root is a Region (like AnchorPane), we can enforce pref size
        if (content instanceof javafx.scene.layout.Region) {
            ((javafx.scene.layout.Region) content).setPrefSize(TARGET_WIDTH, TARGET_HEIGHT);
            ((javafx.scene.layout.Region) content).setMinSize(TARGET_WIDTH, TARGET_HEIGHT);
            ((javafx.scene.layout.Region) content).setMaxSize(TARGET_WIDTH, TARGET_HEIGHT);
        }

        // Wrap in a Group to isolate transformations
        Group contentGroup = new Group(content);

        // Wrap in a StackPane to center the Group
        StackPane stackPane = new StackPane(contentGroup);
        stackPane.setStyle("-fx-background-color: black;"); // Optional: ensure background is black

        // Bind scale properties
        // We need to bind the Group's scale to the StackPane's size

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