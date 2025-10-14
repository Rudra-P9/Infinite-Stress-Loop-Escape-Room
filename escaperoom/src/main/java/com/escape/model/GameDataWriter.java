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
        // load existing file and merge users array
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray usersArray = (JSONArray) root.getOrDefault("users", new JSONArray());

        for (User user : users) {
            JSONObject userObj = new JSONObject();
            userObj.put("userID", user.userID == null ? null : user.userID.toString());
            userObj.put("username", user.getUsername());
            userObj.put("password", user.getPassword());
            // inventory currently not implemented in User; placeholder
            // userObj.put("inventory", ...);

            // replace existing entry with same userID (or username) if present
            boolean replaced = false;
            for (Object o : new ArrayList<>(usersArray)) {
                if (!(o instanceof JSONObject)) continue;
                JSONObject existing = (JSONObject) o;
                if (user.userID != null && user.userID.toString().equals(existing.get("userID"))) {
                    // same userID -> replace whole object
                    usersArray.remove(existing);
                    usersArray.add(userObj);
                    replaced = true;
                    break;
                }
                if (user.getUsername() != null && user.getUsername().equals(existing.get("username"))) {
                    // username exists in persisted file: update fields but preserve existing userID
                    Object existingID = existing.get("userID");
                    if (existingID != null) userObj.put("userID", existingID);
                    // update password (or other fields) on the existing object instead of replacing
                    existing.put("password", user.getPassword());
                    existing.put("username", user.getUsername());
                    // ensure inventory handling would be merged here if implemented
                    replaced = true;
                    break;
                }
            }
            if (!replaced) usersArray.add(userObj);
        }

        root.put("users", usersArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

    /**
     * Saves a single user profile to playerData.json
     * @param user the user to save
     */
    public void saveUser(User user) {
        // Note: This adds to the existing users array in playerData.json
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray usersArray = (JSONArray) root.getOrDefault("users", new JSONArray());

        JSONObject userObj = new JSONObject();
        userObj.put("userID", user.userID == null ? null : user.userID.toString());
        userObj.put("username", user.getUsername());
        userObj.put("password", user.getPassword());

        // replace existing if same userID or username
        boolean replaced = false;
        for (Object o : new ArrayList<>(usersArray)) {
            if (!(o instanceof JSONObject)) continue;
            JSONObject existing = (JSONObject) o;
            if (user.userID != null && user.userID.toString().equals(existing.get("userID"))) {
                usersArray.remove(existing);
                usersArray.add(userObj);
                replaced = true;
                break;
            }
            if (user.getUsername() != null && user.getUsername().equals(existing.get("username"))) {
                usersArray.remove(existing);
                usersArray.add(userObj);
                replaced = true;
                break;
            }
        }
        if (!replaced) usersArray.add(userObj);

        root.put("users", usersArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
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
        
        writeFile("escaperoom/src/main/resources/json/game.json", gameData);
    }

    /**
     * Saves a score entry to playerData.json
     * @param score the score to save
     */
    public void saveScore(Score score) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray scoresArray = (JSONArray) root.getOrDefault("scores", new JSONArray());

        JSONObject scoreObj = new JSONObject();
        scoreObj.put("username", score.getUsername());
        scoreObj.put("difficulty", score.getDifficulty() == null ? null : score.getDifficulty().toString());
        scoreObj.put("timeSeconds", score.getTimeLeftSec());
        scoreObj.put("score", score.getScore());
        scoreObj.put("date", score.getDate() == null ? null : score.getDate().toString());

        scoresArray.add(scoreObj);
        root.put("scores", scoresArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

    /**
     * Saves leaderboard data to playerData.json
     * @param leaderBoard the leaderboard to save
     */
    public void saveLeaderBoard(Leaderboard leaderBoard) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray leaderBoardArray = (JSONArray) root.getOrDefault("leaderboard", new JSONArray());

        ArrayList<User> entries = leaderBoard.getLB();
        if (entries != null) {
            for (User user : entries) {
                JSONObject entryObj = new JSONObject();
                entryObj.put("userID", user.userID == null ? null : user.userID.toString());
                entryObj.put("username", user.getUsername());
                // TODO: Add score details if you have them on User or Leaderboard entries
                leaderBoardArray.add(entryObj);
            }
        }

        root.put("leaderboard", leaderBoardArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

    /**
     * Saves general game state data to playerData.json
     * @param data the saved game data
     */
    public void saveSavedData(SavedData data) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray savedDataArray = (JSONArray) root.getOrDefault("savedData", new JSONArray());

        JSONObject saveObj = new JSONObject();
        saveObj.put("room", data.room);
        saveObj.put("score", data.score);
        saveObj.put("hints", data.hints);
        saveObj.put("puzzle", data.puzzle);

        savedDataArray.add(saveObj);
        root.put("savedData", savedDataArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

    /**
     * Saves account information to playerData.json
     * @param accounts the accounts to save
     */
    public void saveAccounts(Accounts accounts) {
        if (accounts == null) return;
        ArrayList<User> list = accounts.getAccounts();
        if (list != null && !list.isEmpty()) {
            saveUsers(list);
        }
    }

    /**
     * Reads a JSON object from file if it exists, otherwise returns an empty JSONObject.
     */
    @SuppressWarnings("unchecked")
    private JSONObject readJsonObject(String path) {
        try (java.io.FileReader r = new java.io.FileReader(path)) {
            return (JSONObject) new org.json.simple.parser.JSONParser().parse(r);
        } catch (java.io.FileNotFoundException fnf) {
            return new JSONObject();
        } catch (Exception e) {
            System.out.println("Error reading " + path + " - returning empty object: " + e.getMessage());
            return new JSONObject();
        }
    }

    /**
     * Writes JSON data to a file
     * @param filename where to save the file
     * @param jsonData the data to save
     */
    private void writeFile(String filename, Object jsonData) {
        try {
            FileWriter file = new FileWriter(filename);
            String out = prettyPrint(jsonData);
            file.write(out);
            file.close();
            System.out.println("Saved to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving to " + filename);
            e.printStackTrace();
        }
    }

    // --- pretty printing helpers (simple, preserves structure with 2-space indent) ---
    private String prettyPrint(Object json) {
        return prettyPrint(json, 0) + System.lineSeparator();
    }

    private String prettyPrint(Object json, int indent) {
        if (json == null) return "null";
        if (json instanceof JSONObject) return prettyPrintObject((JSONObject) json, indent);
        if (json instanceof JSONArray) return prettyPrintArray((JSONArray) json, indent);
        // primitives
        if (json instanceof String) return '"' + escapeJson((String) json) + '"';
        return json.toString();
    }

    private String prettyPrintObject(JSONObject obj, int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append('{').append('\n');
        int i = 0;
        String pad = "  ".repeat(indent + 1);
        for (Object keyObj : obj.keySet()) {
            String key = keyObj == null ? "null" : keyObj.toString();
            Object val = obj.get(keyObj);
            sb.append(pad).append('"').append(escapeJson(key)).append('"').append(": ");
            sb.append(prettyPrint(val, indent + 1));
            if (i < obj.size() - 1) sb.append(',');
            sb.append('\n');
            i++;
        }
        sb.append("  ".repeat(indent)).append('}');
        return sb.toString();
    }

    private String prettyPrintArray(JSONArray arr, int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append('\n');
        String pad = "  ".repeat(indent + 1);
        for (int i = 0; i < arr.size(); i++) {
            Object v = arr.get(i);
            sb.append(pad).append(prettyPrint(v, indent + 1));
            if (i < arr.size() - 1) sb.append(',');
            sb.append('\n');
        }
        sb.append("  ".repeat(indent)).append(']');
        return sb.toString();
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20 || c > 0x7F) {
                        sb.append(String.format("\\u%04x", (int)c));
                    } else sb.append(c);
            }
        }
        return sb.toString();
    }
}