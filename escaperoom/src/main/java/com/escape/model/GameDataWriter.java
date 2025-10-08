package com.escape.model;

import java.io.FileWriter;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Saves different types of game data to JSON files
 * @author Dylan Diaz
 * @author Rudra Patel
 * @version 2.0
 */
public class GameDataWriter {

    /**
     * Saves a list of user profiles to playerData.json
     * @param users the users to save
     */
    public void saveUsers(ArrayList<User> users) {
        JSONArray usersArray = new JSONArray();
        
        for (User user : users) {
            JSONObject userObj = new JSONObject();
            userObj.put("userID", user.userID.toString());
            // TODO: Add username, password, inventory when getters are ready
            usersArray.add(userObj);
        }
        
        // Save to playerData.json
        JSONObject root = new JSONObject();
        root.put("users", usersArray);
        writeFile("json/playerData.json", root);
    }

    /**
     * Saves a single user profile to playerData.json
     * @param user the user to save
     */
    public void saveUser(User user) {
        // Note: This adds to the existing users array in playerData.json
        JSONObject userObj = new JSONObject();
        userObj.put("userID", user.userID.toString());
        // TODO: Add username, password, inventory
        
        JSONArray usersArray = new JSONArray();
        usersArray.add(userObj);
        
        JSONObject root = new JSONObject();
        root.put("users", usersArray);
        writeFile("json/playerData.json", root);
    }

    /**
     * Saves all room data to game.json
     * @param rooms the list of rooms to save
     */
    public void saveRooms(ArrayList<Rooms> rooms) {
        JSONObject gameData = new JSONObject();
        
        // Add difficulties section
        JSONObject difficulties = new JSONObject();
        
        JSONObject easy = new JSONObject();
        easy.put("hintsAllowed", 3);
        easy.put("initialSeconds", 1800);
        
        JSONObject medium = new JSONObject();
        medium.put("hintsAllowed", 2);
        medium.put("initialSeconds", 1500);
        
        JSONObject hard = new JSONObject();
        hard.put("hintsAllowed", 1);
        hard.put("initialSeconds", 1200);
        
        difficulties.put("EASY", easy);
        difficulties.put("MEDIUM", medium);
        difficulties.put("HARD", hard);
        gameData.put("difficulties", difficulties);
        
        // Add rooms array
        JSONArray roomsArray = new JSONArray();
        for (Rooms room : rooms) {
            JSONObject roomObj = new JSONObject();
            // TODO: Add roomID and difficulty when getters are ready
            // roomObj.put("roomID", room.getRoomID());
            // roomObj.put("difficulty", room.getDifficulty().toString());
            
            // Add puzzleIDs array
            JSONArray puzzleIDsArray = new JSONArray();
            // TODO: Get puzzle IDs from room
            roomObj.put("puzzleIDs", puzzleIDsArray);
            
            // Add puzzle details array
            JSONArray puzzleArray = new JSONArray();
            ArrayList<Puzzle> puzzles = room.getPuzzles();
            if (puzzles != null) {
                for (Puzzle puzzle : puzzles) {
                    JSONObject puzzleObj = new JSONObject();
                    // TODO: Add puzzle fields when getters are ready
                    // puzzleObj.put("puzzleID", puzzle.getPuzzleID());
                    // puzzleObj.put("type", puzzle.getType());
                    // puzzleObj.put("title", puzzle.getTitle());
                    // puzzleObj.put("objective", puzzle.getObjective());
                    // puzzleObj.put("difficulty", puzzle.getDifficulty().toString());
                    // puzzleObj.put("solution", puzzle.getSolution());
                    // puzzleObj.put("solved", puzzle.isSolved());
                    
                    // Add hints array
                    JSONArray hintsArray = new JSONArray();
                    // TODO: Add hints from puzzle
                    puzzleObj.put("hints", hintsArray);
                    
                    puzzleArray.add(puzzleObj);
                }
            }
            roomObj.put("puzzle", puzzleArray);
            roomsArray.add(roomObj);
        }
        gameData.put("rooms", roomsArray);
        
        // Add story section
        JSONObject story = new JSONObject();
        story.put("storyText", "Escape the manor by following the clues. Each solved puzzle advances the plot.");
        story.put("storyPos", 0);
        
        JSONArray storyBeats = new JSONArray();
        // TODO: Add story beats if you have a Story class
        story.put("storyBeats", storyBeats);
        gameData.put("story", story);
        
        // Add timer section
        JSONObject timer = new JSONObject();
        timer.put("EASY", 1800);
        timer.put("MEDIUM", 1500);
        timer.put("HARD", 1200);
        gameData.put("timer", timer);
        
        writeFile("json/game.json", gameData);
    }

    /**
     * Saves a score entry to playerData.json
     * @param score the score to save
     */
    public void saveScore(Score score) {
        JSONObject scoreObj = new JSONObject();
        scoreObj.put("username", score.getUsername());
        scoreObj.put("difficulty", score.getDifficulty().toString());
        scoreObj.put("timeSeconds", score.getTimeLeftSec());
        scoreObj.put("score", score.getScore());
        scoreObj.put("date", score.getDate().toString());
        
        // Add to scores array in playerData.json
        JSONArray scoresArray = new JSONArray();
        scoresArray.add(scoreObj);
        
        JSONObject root = new JSONObject();
        root.put("scores", scoresArray);
        writeFile("json/playerData.json", root);
    }

    /**
     * Saves leaderboard data to playerData.json
     * @param leaderBoard the leaderboard to save
     */
    public void saveLeaderBoard(LeaderBoard leaderBoard) {
        JSONArray leaderBoardArray = new JSONArray();
        
        ArrayList<User> entries = leaderBoard.getLB();
        if (entries != null) {
            for (User user : entries) {
                JSONObject entryObj = new JSONObject();
                entryObj.put("userID", user.userID.toString());
                // TODO: Add username and score details
                leaderBoardArray.add(entryObj);
            }
        }
        
        JSONObject root = new JSONObject();
        root.put("leaderBoard", leaderBoardArray);
        writeFile("json/playerData.json", root);
    }

    /**
     * Saves general game state data to playerData.json
     * @param data the saved game data
     */
    public void saveSavedData(SavedData data) {
        JSONObject saveObj = new JSONObject();
        saveObj.put("room", data.room);
        saveObj.put("score", data.score);
        saveObj.put("hints", data.hints);
        saveObj.put("puzzle", data.puzzle);
        
        // Add to savedData array in playerData.json
        JSONArray savedDataArray = new JSONArray();
        savedDataArray.add(saveObj);
        
        JSONObject root = new JSONObject();
        root.put("savedData", savedDataArray);
        writeFile("json/playerData.json", root);
    }

    /**
     * Saves account information to playerData.json
     * @param accounts the accounts to save
     */
    public void saveAccounts(Accounts accounts) {
        JSONObject accountsObj = new JSONObject();
        // TODO: Add account data - this goes in playerData.json under users
        
        writeFile("json/playerData.json", accountsObj);
    }

    /**
     * Writes JSON data to a file
     * @param filename where to save the file
     * @param jsonData the data to save
     */
    private void writeFile(String filename, Object jsonData) {
        try {
            FileWriter file = new FileWriter(filename);
            file.write(jsonData.toString());
            file.close();
            System.out.println("Saved to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving to " + filename);
            e.printStackTrace();
        }
    }
}