package com.escape.model;

import java.io.FileWriter;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Persists game data back to JSON files.
 *
 * Scope:
 *  Users - playerData.json ("users" array)
 *  Scores - playerData.json ("scores" array)
 *  Leaderboard entries → playerData.json ("leaderboard" array)
 *  SavedData and Rooms in a minimal, schema-aligned way for our project
 *
 * Design goals:
 *  Idempotent updates: if a user with same userID/username exists, update instead of duplicating.
 *  Pretty-printed output for readable diffs.
 *  Fail-quietly for missing files (create new JSON objects when needed).
 *
 * Authors: Dylan Diaz, Rudra Patel
 * Tweaks: Kirtan Patel
 * Version: 5.0
 */
public class GameDataWriter {

    /* ========================= USERS ========================= */

    /**
     * Merge a list of users into playerData.json - "users".
     * If an entry matches by userID or username, it is replaced/updated; otherwise, it is appended.
     */
    public void saveUsers(ArrayList<User> users) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray usersArray = (JSONArray) root.getOrDefault("users", new JSONArray());

        for (User user : users) {
            // Build a minimal JSON record for a user
            JSONObject userObj = new JSONObject();
            userObj.put("userID", user.userID == null ? null : user.userID.toString());
            userObj.put("username", user.getUsername());
            userObj.put("password", user.getPassword());
            // Inventory block (capacity + items)
            JSONObject invObj = new JSONObject();
            JSONArray items = new JSONArray();
            if (user.getInventory() != null && user.getInventory().getItems() != null) {
                for (String it : user.getInventory().getItems()) items.add(it);
                invObj.put("capacity", user.getInventory().getCapacity());
            } else {
                invObj.put("capacity", 0);
            }
            invObj.put("items", items);
            userObj.put("inventory", invObj);

            // Replace existing entry if same userID or username; else append
            boolean replaced = false;
            for (Object o : new ArrayList<>(usersArray)) {
                if (!(o instanceof JSONObject)) continue;
                JSONObject existing = (JSONObject) o;

                // Prefer to match by userID when available
                if (user.userID != null && user.userID.toString().equals(existing.get("userID"))) {
                    usersArray.remove(existing);
                    usersArray.add(userObj);
                    replaced = true;
                    break;
                }

                // Fallback: match by username if IDs are not set
                if (user.getUsername() != null && user.getUsername().equals(existing.get("username"))) {
                    // Preserve existing userID if new object lacks one
                    Object existingID = existing.get("userID");
                    if (existingID != null && userObj.get("userID") == null) {
                        userObj.put("userID", existingID);
                    }
                    usersArray.remove(existing);
                    usersArray.add(userObj);
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
     * Merge a single user into playerData.json - "users".
     * Same replace-or-append behavior as saveUsers(List).
     */
    public void saveUser(User user) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray usersArray = (JSONArray) root.getOrDefault("users", new JSONArray());

        JSONObject userObj = new JSONObject();
        userObj.put("userID", user.userID == null ? null : user.userID.toString());
        userObj.put("username", user.getUsername());
        userObj.put("password", user.getPassword());
        // Persist inventory (capacity + items)
        JSONObject invObj = new JSONObject();
        JSONArray items = new JSONArray();
        if (user.getInventory() != null && user.getInventory().getItems() != null) {
            for (String it : user.getInventory().getItems()) items.add(it);
            invObj.put("capacity", user.getInventory().getCapacity());
        } else {
            invObj.put("capacity", 0);
        }
        invObj.put("items", items);
        userObj.put("inventory", invObj);

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

    /* ========================= ROOMS (MINIMAL) ========================= */

    /**
     * Persist a simplified rooms section to game.json.
     * This method writes a consistent structure used by our project and
     * leaves TODOs where model getters aren’t finalized yet.
     */
    public void saveRooms(ArrayList<Rooms> rooms) {
        JSONObject gameData = new JSONObject();

        // Hard-coded difficulties block for now (keeps file self-contained)
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

        // Serialize rooms array (only the parts we have stable access to)
        JSONArray roomsArray = new JSONArray();
        for (Rooms room : rooms) {
            JSONObject roomObj = new JSONObject();

            // TODO: uncomment when getters are finalized
            // roomObj.put("roomID", room.getRoomID());
            // roomObj.put("difficulty", room.getDifficulty().toString());

            // Placeholder puzzleIDs array (use actual getter when available)
            JSONArray puzzleIDsArray = new JSONArray();
            roomObj.put("puzzleIDs", puzzleIDsArray);

            // Optional puzzle details if we wish to materialize them
            JSONArray puzzleArray = new JSONArray();
            ArrayList<Puzzle> puzzles = room.getPuzzles();
            if (puzzles != null) {
                for (Puzzle puzzle : puzzles) {
                    JSONObject puzzleObj = new JSONObject();
                    // TODO: emit puzzle fields via getters when stable

                    // Hints list placeholder
                    JSONArray hintsArray = new JSONArray();
                    puzzleObj.put("hints", hintsArray);

                    puzzleArray.add(puzzleObj);
                }
            }
            roomObj.put("puzzle", puzzleArray);
            roomsArray.add(roomObj);
        }
        gameData.put("rooms", roomsArray);

        // Minimal story block (keeps file coherent for testing)
        JSONObject story = new JSONObject();
        story.put("storyText", "Escape the manor by following the clues. Each solved puzzle advances the plot.");
        story.put("storyPos", 0);
        story.put("storyBeats", new JSONArray());
        gameData.put("story", story);

        // Timer section aligned with difficulties
        JSONObject timer = new JSONObject();
        timer.put("EASY", 1800);
        timer.put("MEDIUM", 1500);
        timer.put("HARD", 1200);
        gameData.put("timer", timer);

        writeFile("escaperoom/src/main/resources/json/game.json", gameData);
    }

    /* ========================= SCORES & LEADERBOARD ========================= */

    /**
     * Append a score entry to playerData.json -- "scores".
     * We do not deduplicate here; scores are historical records.
     */
    public void saveScore(Score score) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray scoresArray = (JSONArray) root.getOrDefault("scores", new JSONArray());

        JSONObject scoreObj = new JSONObject();
        scoreObj.put("username",   score.getUsername());
        scoreObj.put("difficulty", score.getDifficulty() == null ? null : score.getDifficulty().toString());
        // Some models use "timeLeftSec", some "timeSeconds"; we persist "timeSeconds"
        scoreObj.put("timeSeconds", score.getTimeLeftSec());
        scoreObj.put("score",       score.getScore());
        scoreObj.put("date",        score.getDate() == null ? null : score.getDate().toString());

        scoresArray.add(scoreObj);
        root.put("scores", scoresArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

        /**
     * Upsert (save or replace) the player's progress into playerData.json.
     * Keeps exactly one entry per userUUID inside the "progress" array.
     */
    public void saveProgress(Progress p) {
        if (p == null) return;

        final String PATH = "escaperoom/src/main/resources/json/playerData.json";

        org.json.simple.JSONObject root = readJsonObject(PATH);
        if (root == null) root = new org.json.simple.JSONObject();

        org.json.simple.JSONArray arr = (org.json.simple.JSONArray)
                root.getOrDefault("progress", new org.json.simple.JSONArray());

        org.json.simple.JSONObject jo = new org.json.simple.JSONObject();
        jo.put("userUUID",     p.getUserUUID()     == null ? null : p.getUserUUID().toString());
        jo.put("progressUUID", p.getProgressUUID() == null ? null : p.getProgressUUID().toString());
        jo.put("c",            Integer.valueOf(p.getStoryPos()));
        jo.put("answered",     Integer.valueOf(p.getQuestionsAnswered()));
        jo.put("hints",        Integer.valueOf(p.getHintsUsed()));

        boolean replaced = false;
        for (int i = 0; i < arr.size(); i++) {
            org.json.simple.JSONObject existing = (org.json.simple.JSONObject) arr.get(i);
            Object uid = existing.get("userUUID");
            if (uid != null && uid.equals(jo.get("userUUID"))) {
                arr.set(i, jo);
                replaced = true;
                break;
            }
        }
        if (!replaced) arr.add(jo);

        root.put("progress", arr);
        writeFile(PATH, root);
    }


    /**
     * Persist a simplified leaderboard to playerData.json -- "leaderboard".
     * This implementation serializes user identity/username; extend with
     * per-entry score/timing as your Leaderboard design stabilizes.
     */
    public void saveLeaderboard(Leaderboard leaderboard) {
        //Read current data from playerData.json
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        //rebuilds the leaderboard array from the  current leaderboard
        JSONArray leaderboardArray = (JSONArray) root.getOrDefault("leaderboard", new JSONArray());
        
        if (leaderboard != null) {
            ArrayList<Score> scores = leaderboard.getLB();
            if (scores != null) {
                for (Score s : scores) {
                    JSONObject entryObj = new JSONObject();
                    entryObj.put("username",   s.getUsername());
                    entryObj.put("difficulty", (s.getDifficulty() == null) ? null : s.getDifficulty().toString());

        
                entryObj.put("timeLeftSec",  s.getTimeLeftSec());   
                entryObj.put("score",      s.getScore());
                entryObj.put("date",       (s.getDate() == null) ? null : s.getDate().toString());

                leaderboardArray.add(entryObj);
                }
            }   
        }
        root.put("leaderboard", leaderboardArray);
            writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }
    /*SAVED DATA & ACCOUNTS

    /**
     * Append a saved-game snapshot to playerData.json -- "savedData".
     * Fields mirror the SavedData structure we use in this project.
     */
    public void saveSavedData(SavedData data) {
        JSONObject root = readJsonObject("escaperoom/src/main/resources/json/playerData.json");
        JSONArray savedDataArray = (JSONArray) root.getOrDefault("savedData", new JSONArray());

        JSONObject saveObj = new JSONObject();
        saveObj.put("room",   data.room);
        saveObj.put("score",  data.score);
        saveObj.put("hints",  data.hints);
        saveObj.put("puzzle", data.puzzle);

        savedDataArray.add(saveObj);
        root.put("savedData", savedDataArray);
        writeFile("escaperoom/src/main/resources/json/playerData.json", root);
    }

    /**
     * Convenience method: persist all account users as a batch.
     */
    public void saveAccounts(Accounts accounts) {
        if (accounts == null) return;
        ArrayList<User> list = accounts.getAccounts();
        if (list != null && !list.isEmpty()) {
            saveUsers(list);
        }
    }

    /* I/O & PRINT HELPERS

    /**
     * Read JSON object from path. If file does not exist, return an empty JSON object.
     * This keeps writer calls simple: read; mutate; write.
     */
    private JSONObject readJsonObject(String path) {
        try (java.io.FileReader r = new java.io.FileReader(path)) {
            return (JSONObject) new org.json.simple.parser.JSONParser().parse(r);
        } catch (java.io.FileNotFoundException fnf) {
            return new JSONObject(); // start from an empty JSON object
        } catch (Exception e) {
            System.out.println("Error reading " + path + " - returning empty object: " + e.getMessage());
            return new JSONObject();
        }
    }

    /**
     * Write a JSON object/array to disk with stable, human-readable formatting.
     * The pretty-printer below is intentionally simple: it preserves order within
     * a single run and produces two-space indentation to keep diffs short.
     */
    private void writeFile(String filename, Object jsonData) {
        try (FileWriter file = new FileWriter(filename)) {
            String out = prettyPrint(jsonData);
            file.write(out);
            System.out.println("Saved to " + filename);
        } catch (Exception e) {
            System.out.println("Error saving to " + filename);
            e.printStackTrace();
        }
    }

    //pretty printing

    private String prettyPrint(Object json) { return prettyPrint(json, 0) + System.lineSeparator(); }

    private String prettyPrint(Object json, int indent) {
        if (json == null) return "null";
        if (json instanceof JSONObject) return prettyPrintObject((JSONObject) json, indent);
        if (json instanceof JSONArray)  return prettyPrintArray((JSONArray)  json, indent);
        if (json instanceof String)     return '"' + escapeJson((String) json) + '"';
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
            sb.append( prettyPrint(val, indent + 1) );
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
            sb.append(pad).append( prettyPrint(arr.get(i), indent + 1) );
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
                case '"' : sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20 || c > 0x7F) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else sb.append(c);
            }
        }
        return sb.toString();
 
 
    }
    public static void main(String[] args) {
    GameDataLoader loader = new GameDataLoader();
    GameDataWriter writer = new GameDataWriter();

    // Before
    int before = loader.getUsers().size();
    System.out.println("Before: users=" + before);

    // Write a unique user
    String uname = "TestUser_" + System.currentTimeMillis();
    User u = new User(null, uname, "pw");
    writer.saveUser(u);
    System.out.println("Appended: " + uname);

    // After
    int after = loader.getUsers().size();
    System.out.println("After: users=" + after + " (should be " + (before + 1) + ")");

    // quick cleanup so we can re-run
    try {
        removeUserByUsername(uname); // helper below
        System.out.println("Cleanup removed: " + uname);
    } catch (Exception e) {
        System.out.println("Cleanup skipped: " + e.getMessage());
    }
        
}

// minimal cleanup helper (same path our writer uses)
private static final String PLAYER_PATH = "escaperoom/src/main/resources/json/playerData.json";

@SuppressWarnings("unchecked")
private static void removeUserByUsername(String uname) throws Exception {
    var parser = new org.json.simple.parser.JSONParser();
    org.json.simple.JSONObject root;
    try (java.io.FileReader r = new java.io.FileReader(PLAYER_PATH)) {
        root = (org.json.simple.JSONObject) parser.parse(r);
    }
    var users = (org.json.simple.JSONArray) root.getOrDefault("users", new org.json.simple.JSONArray());
    java.util.Iterator<?> it = users.iterator();
    while (it.hasNext()) {
        Object o = it.next();
        if (o instanceof org.json.simple.JSONObject) {
            var jo = (org.json.simple.JSONObject) o;
            if (uname.equals(String.valueOf(jo.get("username")))) it.remove();
        }
    }
    root.put("users", users);
    GameDataWriter gw = new GameDataWriter();
    gw.writeFile(PLAYER_PATH, root);   // writeFile(...) already pretty-prints
    }
}


