package com.escape.model;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Reads game data from JSON into lightweight model objects.
 *
 * Sources during development:
 *   - game.json        : rooms, story, puzzle metadata (we only consume what we need)
 *   - playerData.json  : users, scores, leaderboard, saved progress
 *
 * Notes:
 *  • Loader is intentionally "read-only": it never writes back to JSON.
 *  • We keep the loader tolerant to teammates' in-progress classes:
 *      - Reflection is used to call setters only when they exist.
 *      - A public no-arg constructor is required for instantiated types.
 *  • We try both classpath resources and filesystem paths so it works from IDE or build.
 *
 * Author: Kirtan Patel
 * Version: 5.0
 */
public class GameDataLoader {

    /* ----------------------------------------------------------------------
     * Candidate paths for JSON files.
     * Keep the order stable: classpath-first would also work, but here we
     * try these common filesystem locations (dev-friendly) and also the
     * classpath inside readObjectFromCandidates().
     * -------------------------------------------------------------------- */
    private static final String[] GAME_CANDIDATES = {
        "json/game.json",
        "escaperoom/src/main/resources/json/game.json"
    };

    private static final String[] PLAYER_CANDIDATES = {
        "json/playerData.json",
        "escaperoom/src/main/resources/json/playerData.json"
    };

        // Candidate locations for playerData.json (same pattern we already use for game.json)
    private static final String[] PLAYERDATA_CANDIDATES = {
        "escaperoom/src/main/resources/json/playerData.json",
        "src/main/resources/json/playerData.json",
        "resources/json/playerData.json",
        "playerData.json"
    };


    /* ========================= PUBLIC API ========================= */

    /**
     * Parse users from playerData.json -- "users" array.
     * Builds minimal User objects (UUID/username/password). Inventory is
     * parsed but only wired in if/when the User/Inventory APIs exist.
     */
    public ArrayList<User> getUsers() {
        JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray arr   = (JSONArray) data.get("users");

        ArrayList<User> out = new ArrayList<>();
        if (arr == null) return out; // no users section -- empty list

        for (Object o : arr) {
            if (!(o instanceof JSONObject)) continue;
            JSONObject uo = (JSONObject) o;

            // Parse UUID if present and well-formed
            String idStr = uo.get("userID") == null ? null : uo.get("userID").toString();
            java.util.UUID id = null;
            try {
                if (idStr != null && !idStr.isEmpty()) id = java.util.UUID.fromString(idStr);
            } catch (Exception ignore) {
                // Malformed id is ignored; we still create a User
            }

            String username = uo.get("username") == null ? null : uo.get("username").toString();
            String password = uo.get("password") == null ? null : uo.get("password").toString();

            // Our User constructor (UUID, username, password)
            User u = new User(id, username, password);

            // Inventory block is read for future use,
            // but we don’t enforce any Inventory APIs here.
            // Inventory block: construct Inventory(capacity) and populate items
            JSONObject invObj = (JSONObject) uo.get("inventory");
            if (invObj != null) {
                int capacity = toInt(invObj.get("capacity"));
                // If capacity is zero, pick a sensible default based on items length
                JSONArray items = (JSONArray) invObj.get("items");
                if (capacity <= 0) capacity = (items == null) ? 26 : Math.max(26, items.size());
                Inventory inv = new Inventory(capacity);
                if (items != null) {
                    for (Object it : items) {
                        if (it != null) inv.addItem(it.toString());
                    }
                }
                u.setInventory(inv);
            }

            out.add(u);
        }
        return out;
    }
    
    /**
    * Load puzzles for a given room from game.json.
    *
    * <p>Looks up the room by its {@code roomID}. If not found and the id is in the
    * form {@code "roomN"} (e.g., "room1"), it falls back to the N-th room
    * (1-based). If the room or its {@code puzzles[]} is missing, returns an empty list.</p>
    */
    public java.util.List<Puzzle> loadPuzzlesForRoom(String roomId,
                                                    java.util.Map<String,String> textOut) {
        java.util.List<Puzzle> out = new java.util.ArrayList<>();

        org.json.simple.JSONObject root  = readObjectFromCandidates(GAME_CANDIDATES);
        org.json.simple.JSONArray  rooms = (org.json.simple.JSONArray) root.get("rooms");
        if (rooms == null) return out;

        // find the room by id (or fallback to index roomN)
        org.json.simple.JSONObject target = null;
        for (Object rObj : rooms) {
            org.json.simple.JSONObject ro = (org.json.simple.JSONObject) rObj;
            String id = ro.get("roomID") == null ? null : ro.get("roomID").toString();
            if (roomId != null && roomId.equals(id)) { target = ro; break; }
        }
        if (target == null && roomId != null && roomId.startsWith("room")) {
            try {
                int idx = Integer.parseInt(roomId.substring(4)) - 1; // "room1" -> 0
                if (idx >= 0 && idx < rooms.size()) target = (org.json.simple.JSONObject) rooms.get(idx);
            } catch (NumberFormatException ignore) { }
        }
        if (target == null) return out;

        org.json.simple.JSONArray puzzles = (org.json.simple.JSONArray) target.get("puzzles");
        if (puzzles == null) return out;

        for (Object pObj : puzzles) {
            org.json.simple.JSONObject po = (org.json.simple.JSONObject) pObj;

            String id        = po.get("id")        == null ? null : po.get("id").toString();
            String category  = po.get("category")  == null ? ""   : po.get("category").toString();
            String type      = po.get("type")      == null ? ""   : po.get("type").toString();
            String title     = po.get("title")     == null ? ""   : po.get("title").toString();
            String objective = po.get("objective") == null ? ""   : po.get("objective").toString();
            String solution  = po.get("solution")  == null ? ""   : po.get("solution").toString();
            String hint      = po.get("hint")      == null ? ""   : po.get("hint").toString();
            String prompt    = po.get("prompt")    == null ? ""   : po.get("prompt").toString();
            String reward    = po.get("rewardLetter") == null ? "" : po.get("rewardLetter").toString();

            Puzzle p;
            if ("NUMBER".equalsIgnoreCase(category)) {
                p = new NumberPuzzle(id, title, objective, solution, category, type);
            } else if ("AUDIO".equalsIgnoreCase(category)) {
                p = new AudioPuzzle(id, title, objective, solution, category, type);
            } else { // default to WORD for RIDDLE / LETTER_DECIPHER / ARROW_DECIPHER / FINAL_LOCK
                p = new WordPuzzle(id, title, objective, solution, category, type);
            }
            out.add(p);

            // stash text(prompt/hint/reward) if requested
            if (textOut != null && id != null) {
                if (!prompt.isBlank()) textOut.put(id + ":prompt", prompt);
                if (!hint.isBlank())   textOut.put(id + ":hint",   hint);
                if (!reward.isBlank()) textOut.put(id + ":reward", reward);
            }
        }

        return out;
    }


    /**
     * Parse rooms from game.json -- "rooms" array.
     * We only set simple metadata (roomID, difficulty) and an optional list
     * of puzzleIDs; we do not instantiate concrete Puzzle subclasses here.
     */
    public ArrayList<Rooms> getRooms() {
    JSONObject gameRoot = readObjectFromCandidates(GAME_CANDIDATES);
    JSONArray roomsArray = (JSONArray) gameRoot.get("rooms");

    ArrayList<Rooms> rooms = new ArrayList<>();
    if (roomsArray == null) return rooms;

    for (Object roomObj : roomsArray) {
        if (!(roomObj instanceof JSONObject)) continue;
        JSONObject roomJson = (JSONObject) roomObj;

        Rooms room = new Rooms();
        room.setRoomID((String) roomJson.get("roomID"));
        room.setTitle((String) roomJson.get("title"));

        ArrayList<Puzzle> puzzleList = new ArrayList<>();
        JSONArray puzzleArray = (JSONArray) roomJson.get("puzzles");
        if (puzzleArray == null) {
            room.setPuzzles(puzzleList);
            rooms.add(room);
            continue;
        }

        

        for (Object pObj : puzzleArray) {
            if (!(pObj instanceof JSONObject)) continue;
            JSONObject pJson = (JSONObject) pObj;

            String category = (String) pJson.get("category");
            String type = (String) pJson.get("type");
            Puzzle puzzle = null;

            if ("AUDIO".equalsIgnoreCase(category)) {
                puzzle = new AudioPuzzle(
                    (String) pJson.get("id"),
                    (String) pJson.get("title"),
                    (String) pJson.get("objective"),
                    (String) pJson.get("solution"),
                    category, type
                );
            } else if ("NUMBER".equalsIgnoreCase(category)) {
                puzzle = new NumberPuzzle(
                    (String) pJson.get("id"),
                    (String) pJson.get("title"),
                    (String) pJson.get("objective"),
                    (String) pJson.get("solution"),
                    category, type
                );
            } else {
                // Default to word puzzles
                puzzle = new WordPuzzle(
                    (String) pJson.get("id"),
                    (String) pJson.get("title"),
                    (String) pJson.get("objective"),
                    (String) pJson.get("solution"),
                    category, type
                );
            }

            

            if (pJson.get("prompt") != null)
            puzzle.setPrompt((String) pJson.get("prompt"));

            if (pJson.get("hint") != null) 
            {
                String hintText = (String) pJson.get("hint");
                if (hintText != null && !hintText.isEmpty()) {
                    Hints h = new Hints("hint1", 1, false, hintText);
                    puzzle.getHints().add(h);
                }
            }

            if (pJson.get("rewardLetter") != null)
                setIfPresent(puzzle, "setRewardLetter", String.class, (String) pJson.get("rewardLetter"));

            puzzleList.add(puzzle);
        }

        room.setPuzzles(puzzleList);
        rooms.add(room);
    }

    return rooms;
}

/**
 * Loads all story text (intro, room intros, transitions, conclusions, etc.)
 * from game.json and returns a populated StoryElements object.
 */
public StoryElements getStory() {
    JSONObject gameRoot = readObjectFromCandidates(GAME_CANDIDATES);
    JSONObject storyJson = (JSONObject) gameRoot.get("story");

    StoryElements story = new StoryElements();

    if (storyJson == null) {
        System.out.println("Warning: No 'story' section found in JSON.");
        return story;
    }

    story.setIntro((String) storyJson.get("intro"));
    story.setRoomOneIntro((String) storyJson.get("roomOneIntro"));
    story.setRoomOneConc((String) storyJson.get("roomOneConc"));
    story.setRoomTwoIntro((String) storyJson.get("roomTwoIntro"));
    story.setRoomTwoBetween((String) storyJson.get("roomTwoBetween"));
    story.setRoomTwoConc((String) storyJson.get("roomTwoConc"));
    story.setRoomThreeIntro((String) storyJson.get("roomThreeIntro"));
    story.setRoomThreeBetween((String) storyJson.get("roomThreeBetween"));
    story.setRoomThreeConc((String) storyJson.get("roomThreeConc"));
    story.setFinalPuzzle((String) storyJson.get("finalPuzzle"));
    story.setConclusion((String) storyJson.get("conclusion"));

    return story;
}

    /**
     * Choose a single "best" score from playerData.json -- "scores".
     * Here “best” is the minimal timeSeconds (fastest completion).
     */
    public Score getScore() {
        JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray scores  = (JSONArray) data.get("scores");

        if (scores == null || scores.isEmpty()) {
            // If there are no scores, still return a Score object to avoid null checks.
            return newInstance(Score.class);
        }

        // Single pass to find the minimal timeSeconds
        JSONObject best = null;
        long bestTime = Long.MAX_VALUE;
        for (Object o : scores) {
            if (!(o instanceof JSONObject)) continue;
            JSONObject so = (JSONObject) o;
            long t = toLong(so.get("timeSeconds"));
            if (t < bestTime) { bestTime = t; best = so; }
        }
        if (best == null) best = (JSONObject) scores.get(0);

        // Populate a Score object using whichever setters exist
        Score s = newInstance(Score.class);
        setIfPresent(s, "setUsername",    String.class,  str(best.get("username")));
        setIfPresent(s, "setDifficulty",  String.class,  str(best.get("difficulty")));
        setIfPresent(s, "setTimeSeconds", long.class,    toLong(best.get("timeSeconds")));
        setIfPresent(s, "setTimeLeftSec", long.class,    toLong(best.get("timeLeftSec"))); // tolerant alias
        setIfPresent(s, "setHintsUsed",   int.class,     toInt(best.get("hintsUsed")));
        setIfPresent(s, "setPuzzlesSolved", int.class,   toInt(best.get("puzzlesSolved")));
        setIfPresent(s, "setDate",        String.class,  str(best.get("date")));
        setIfPresent(s, "setScore",       long.class,    toLong(best.get("score")));
        return s;
    }

        /**
     * Load saved progress for the given user from playerData.json.
     * Returns null if no saved entry exists.
     */
    public Progress loadProgressForUser(java.util.UUID userId) {
        if (userId == null) return null;

        // Use the same helper you already use elsewhere
        org.json.simple.JSONObject root = readObjectFromCandidates(PLAYERDATA_CANDIDATES);
        if (root == null) return null;

        org.json.simple.JSONArray arr = (org.json.simple.JSONArray) root.get("progress");
        if (arr == null) return null;

        for (Object o : arr) {
            org.json.simple.JSONObject jo = (org.json.simple.JSONObject) o;
            String uid = jo.get("userUUID") == null ? null : jo.get("userUUID").toString();
            if (userId.toString().equals(uid)) {
                String pid = jo.get("progressUUID") == null ? null : jo.get("progressUUID").toString();
                int c        = parseIntSafe(jo.get("c"),        0);
                int answered = parseIntSafe(jo.get("answered"), 0);
                int hints    = parseIntSafe(jo.get("hints"),    0);

                Progress p = new Progress(
                    (pid == null ? java.util.UUID.randomUUID() : java.util.UUID.fromString(pid)),
                    userId
                );
                p.setStoryPos(c);
                p.setQuestionsAnswered(answered);
                p.setHintsUsed(hints);
                return p;
            }
        }
        return null;
    }

    // Small helper
    private static int parseIntSafe(Object v, int deflt) {
        if (v == null) return deflt;
        try {
            if (v instanceof Number) return ((Number) v).intValue();
            return Integer.parseInt(v.toString());
        } catch (Exception e) { return deflt; }
    }



    /**
     * Build a Leaderboard from playerData.json -- "leaderboard".
     * Strategy:
     *  1) Collect entries as Score objects.
     *  2) Prefer a bulk setter (setEntries or setLB).
     *  3) If not available, try addEntry(username, difficulty, timeSeconds, score, date).
     */
    public Leaderboard getLeaderboard() {
        JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray arr   = (JSONArray) data.get("leaderboard");

        Leaderboard lb = newInstance(Leaderboard.class);
        if (arr == null) return lb;

        ArrayList<Score> list = new ArrayList<>();
        for (Object o : arr) {
            if (!(o instanceof JSONObject)) continue;
            JSONObject jo = (JSONObject) o;

            Score s = newInstance(Score.class);
            setIfPresent(s, "setUsername",    String.class, str(jo.get("username")));
            setIfPresent(s, "setDifficulty",  String.class, str(jo.get("difficulty")));
            setIfPresent(s, "setTimeSeconds", long.class,   toLong(jo.get("timeSeconds")));
            setIfPresent(s, "setTimeLeftSec", long.class,   toLong(jo.get("timeLeftSec")));
            setIfPresent(s, "setScore",       long.class,   toLong(jo.get("score")));
            setIfPresent(s, "setDate",        String.class, str(jo.get("date")));
            list.add(s);

            // Fallback: if the Leaderboard exposes a variadic 'addEntry', call it safely
            setIfPresent(
                lb,
                "addEntry",
                new Class<?>[]{ String.class, String.class, long.class, long.class, String.class },
                new Object[]{ sSafe(s,"getUsername"), sSafe(s,"getDifficulty"),
                              nSafe(s,"getTimeSeconds"), nSafe(s,"getScore"), sSafe(s,"getDate") }
            );
        }

        // Prefer a bulk setter; if not present we already tried addEntry above
        boolean ok = setIfPresent(lb, "setEntries", List.class, list);
        if (!ok) setIfPresent(lb, "setLB", List.class, list);
        return lb;
    }

    /* ========================= HELPERS ========================= */

    /**
     * Attempt to read JSON from classpath first, then from each filesystem path.
     * On any failure, returns an empty JSONObject to keep the loader resilient.
     */
    private static JSONObject readObjectFromCandidates(String[] candidates) {
        for (String c : candidates) {
            // Try as classpath resource
            try (var in = GameDataLoader.class.getClassLoader().getResourceAsStream(c)) {
                if (in != null) {
                    var r = new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8);
                    return (JSONObject) new JSONParser().parse(r);
                }
            } catch (Exception ignore) { }

            // Try as direct filesystem path
            try (var fr = new FileReader(c)) {
                return (JSONObject) new JSONParser().parse(fr);
            } catch (Exception ignore) { }
        }
        return new JSONObject();
    }

    private static String str(Object o) { return (o == null ? null : o.toString()); }

    /** Map common difficulty strings to a small integer (if a stub expects an int). */
    private static int mapDifficultyToInt(String diff) {
        if (diff == null) return 0;
        switch (diff.trim().toUpperCase()) {
            case "EASY":   return 0;
            case "MEDIUM": return 1;
            case "HARD":   return 2;
            default:       return 0;
        }
    }

    /** Safe number parsing helpers; never throw, default to 0. */
    private static long toLong(Object n) {
        if (n == null) return 0L;
        if (n instanceof Number) return ((Number) n).longValue();
        try { return Long.parseLong(n.toString()); } catch (NumberFormatException e) { return 0L; }
    }
    private static int toInt(Object n) {
        if (n == null) return 0;
        if (n instanceof Number) return ((Number) n).intValue();
        try { return Integer.parseInt(n.toString()); } catch (NumberFormatException e) { return 0; }
    }

    /**
     * Try to call a single-arg method (e.g., a setter) if it exists on the target.
     * Returns true when successfully invoked, false if the method is absent or fails.
     */
    private static boolean setIfPresent(Object target, String setter, Class<?> param, Object value) {
        if (target == null || setter == null) return false;
        try {
            Method m = target.getClass().getMethod(setter, param);
            m.invoke(target, value);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nsme) {
            return false;
        }
    }

    /** Variant for a fixed signature like addEntry(u, d, t, s, date). */
    private static boolean setIfPresent(Object target, String method, Class<?>[] params, Object[] values) {
        if (target == null || method == null) return false;
        try {
            Method m = target.getClass().getMethod(method, params);
            m.invoke(target, values);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException nsme) {
            return false;
        }
    }

    /**
     * Create an instance using the public no-arg constructor.
     * We throw a clear message if a class cannot be constructed this way.
     */
    private static <T> T newInstance(Class<T> type) {
        try {
            if (Modifier.isAbstract(type.getModifiers()) || type.isInterface()) {
                throw new UnsupportedOperationException("Cannot instantiate abstract/interface: " + type.getName());
            }
            Constructor<T> c = type.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (UnsupportedOperationException uoe) {
            throw uoe;
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(
                "Class " + type.getName() + " needs a public no-arg constructor for the loader.", e);
        }
    }

    /* Safe getter helpers used for addEntry fallback (avoid reflection duplication) */
    private static String sSafe(Object obj, String getterName) {
        try { Object v = obj.getClass().getMethod(getterName).invoke(obj); return v == null ? null : v.toString(); }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) { return null; }
    }
    private static long nSafe(Object obj, String getterName) {
        try { Object v = obj.getClass().getMethod(getterName).invoke(obj); return toLong(v); }
        catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) { return 0L; }
    }
    public static void main(String[] args) {
    GameDataLoader dl = new GameDataLoader();

    var users = dl.getUsers();
    var rooms = dl.getRooms();
    var best  = dl.getScore();
    var lb    = dl.getLeaderboard();

    System.out.println("=== DATA LOADER TESTING ===");
    System.out.println("users=" + users.size());
    System.out.println("rooms=" + rooms.size());
    System.out.println("bestUser=" + safe(best, "getUsername"));
    System.out.println("leaderboardSize=" + getLbSize(lb));
}

// helper for printing without depending on concrete API
private static String safe(Object o, String getter) {
    try { Object v = o.getClass().getMethod(getter).invoke(o); return v == null ? null : v.toString(); }
    catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) { return null; }
}
private static int getLbSize(Leaderboard lb) {
    try {
        var m = lb.getClass().getMethod("getEntries");
        Object v = m.invoke(lb);
        if (v instanceof java.util.List<?>) return ((java.util.List<?>) v).size();
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignore) { }
    try {
        var m = lb.getClass().getMethod("getLB");
        Object v = m.invoke(lb);
        if (v instanceof java.util.List<?>) return ((java.util.List<?>) v).size();
    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignore) { }
    return 0;
}

public static class TextPuzzle extends Puzzle {
    private String hint;
    private String rewardLetter;

    public TextPuzzle() {
        super(null, null, null, null, null, null);
    }

    @Override
    public boolean checkAnswer(String input) {
        return input != null && solution != null && input.trim().equalsIgnoreCase(solution.trim());
    }

    @Override
    public String getSolution() {
        return solution;
    }

    @Override
    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setHint(String hint) { this.hint = hint; }
    @Override
    public String getHint() { return hint != null ? hint : super.getHint(); }

    @Override
    public void setRewardLetter(String rewardLetter) { this.rewardLetter = rewardLetter; }
    @Override
    public String getRewardLetter() { return rewardLetter; }
}


}
