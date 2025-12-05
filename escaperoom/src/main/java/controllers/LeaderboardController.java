package controllers;


import com.escape.App;
import com.escape.model.Leaderboard;
import com.escape.model.Score;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
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
      
       Label rankLabel = new Label("#" + rank);
       rankLabel.setStyle("-fx-font-family: 'Orbitron'; -fx-font-size: 36px; -fx-text-fill: #FFFFFF;");


       Label name = new Label(s.getUsername());
       name.setStyle("-fx-font-family: 'Orbitron'; -fx-font-size: 36px; -fx-text-fill: #7DF9FF;");


       Label scoreLbl = new Label(String.valueOf(s.getScore()));
       scoreLbl.setStyle("-fx-font-family: 'Orbitron'; -fx-font-size: 36px; -fx-text-fill: #C9B2FF;");


       row.getChildren().addAll(rankLabel, name, scoreLbl);


       scoresBox.getChildren().add(row);
       rank++;
   }
}


   @FXML
   private void goBack() throws Exception {
       App.setRoot("OpenDoor");
   }
}
