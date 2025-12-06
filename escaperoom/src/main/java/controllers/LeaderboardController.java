package controllers;

import com.escape.App;
import com.escape.model.Leaderboard;
import com.escape.model.Score;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for the leaderboard view.
 * 
 * @author Rudra Patel
 * @author Talan Kinard
 */
public class LeaderboardController {

    @FXML
    private VBox scoresBox;

    public void initialize() {
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        scoresBox.getChildren().clear();

        if (App.gameFacade == null) {
            System.out.println("ERROR: gameFacade is null.");
            return;
        }

        Leaderboard lb = App.gameFacade.getLeaderboard();
        if (lb == null) {
            System.out.println("No leaderboard found.");
            return;
        }

        // TOP 5
        var topFive = lb.topN(5);

        int rank = 1;
        for (Score s : topFive) {

            HBox row = new HBox(40);
            row.setStyle("-fx-padding: 10;");
            row.setPrefWidth(700);

            Label rankLabel = new Label("#" + rank);
            rankLabel.setFont(javafx.scene.text.Font.font("Monospaced", 36));
            rankLabel.setTextFill(javafx.scene.paint.Color.LIME);
            rankLabel.setMinWidth(80);

            Label name = new Label(s.getUsername());
            name.setFont(javafx.scene.text.Font.font("Monospaced", 36));
            name.setTextFill(javafx.scene.paint.Color.LIME);
            name.setMinWidth(300);

            // Create a spacer to push score to the right
            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            Label scoreLbl = new Label(String.valueOf(s.getScore()));
            scoreLbl.setFont(javafx.scene.text.Font.font("Monospaced", 36));
            scoreLbl.setTextFill(javafx.scene.paint.Color.LIME);

            row.getChildren().addAll(rankLabel, name, spacer, scoreLbl);

            scoresBox.getChildren().add(row);
            rank++;
        }
    }

    @FXML
    private void goBack() throws Exception {
        App.setRoot("MainScreen");
    }
}
