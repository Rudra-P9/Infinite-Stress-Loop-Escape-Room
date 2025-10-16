package com.escape.model;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Loads small, data from JSON into our model objects.
 * Files used during development:
 * - game.json       (rooms, puzzles metadata, story, etc.)
 * - playerData.json (users, scores, leaderboard, saved progress)
 *
 * Author: Kirtan Patel
 * Version: 2.0
 */
public class GameDataLoader {

    /* ----------search paths (most stable first) ---------- */

    private static final String[] GAME_CANDIDATES   = {
  "json/game.json", "escaperoom/src/main/resources/json/game.json"
};
    private static final String[] PLAYER_CANDIDATES = {
  "json/playerData.json", "escaperoom/src/main/resources/json/playerData.json"
};


    /* ---------------------------- API ---------------------------- */

    /** Loads users from playerData.json - "users". */
    public ArrayList<User> getUsers() {
        JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray arr   = (JSONArray) data.get("users");

        ArrayList<User> out = new ArrayList<>();
        if (arr == null) return out;

        for (Object o : arr) {
            if (!(o instanceof JSONObject)) continue;
            JSONObject uo = (JSONObject) o;

            String idStr = uo.get("userID") == null ? null : uo.get("userID").toString();
            java.util.UUID id = null;
            try {
                if (idStr != null && !idStr.isEmpty()) id = java.util.UUID.fromString(idStr);
            } catch (Exception e) {
                // ignore malformed UUIDs
            }

            String username = uo.get("username") == null ? null : uo.get("username").toString();
            String password = uo.get("password") == null ? null : uo.get("password").toString();

            User u = new User(id, username, password);

            // parse inventory if present
            JSONObject invObj = (JSONObject) uo.get("inventory");
            if (invObj != null) {
                @SuppressWarnings("unchecked")
                ArrayList<String> items = invObj.get("items") instanceof JSONArray
                        ? new ArrayList<>((JSONArray) invObj.get("items"))
                        : new ArrayList<>();
                //Inventory inv = new Inventory();
                // Inventory API is stubbed; if you implement setItems/setCapacity, use them here
                // otherwise we leave inventory handling for later
                // set capacity if possible
            }

            out.add(u);
        }
        return out;
    }

    /** Loads rooms from game.json - "rooms". Only sets puzzleIDs. */
    public ArrayList<Rooms> getRooms() {
    JSONObject game  = readObjectFromCandidates(GAME_CANDIDATES);
        JSONArray rooms = (JSONArray) game.get("rooms");

        ArrayList<Rooms> out = new ArrayList<>();
        if (rooms == null) return out;

        for (Object rObj : rooms) {
            JSONObject ro = (JSONObject) rObj;

            Rooms r = newInstance(Rooms.class);
            setIfPresent(r, "setRoomID", String.class, (String) ro.get("roomID"));

            Object diff = ro.get("difficulty");
            boolean ok = setIfPresent(r, "setDifficulty", String.class, diff == null ? null : diff.toString());
            if (!ok && diff != null) { // if someone uses an int in their stub
                setIfPresent(r, "setDifficulty", int.class, mapDifficultyToInt(diff.toString()));
            }

            JSONArray ids = (JSONArray) ro.get("puzzleIDs");
            if (ids != null) {
                @SuppressWarnings("unchecked")
                ArrayList<String> idList = new ArrayList<>((JSONArray) ids);
                boolean set = setIfPresent(r, "setPuzzleIDs", List.class, idList);
                if (!set) {
                    for (String id : idList) setIfPresent(r, "addPuzzleID", String.class, id);
                }
            }

            if (ro.get("story") != null) {
                setIfPresent(r, "setStory", String.class, ro.get("story").toString());
            }

            out.add(r);
        }
        return out;
    }

    /** Returns one Score. I choose the fastest (smallest timeSeconds) from playerData.json - "scores". */
    public Score getScore() {
    JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray scores  = (JSONArray) data.get("scores");

        if (scores == null || scores.isEmpty()) {
            return newInstance(Score.class);
        }

        JSONObject best = null;
        long bestTime = Long.MAX_VALUE;
        for (Object o : scores) {
            JSONObject so = (JSONObject) o;
            long t = toLong(so.get("timeSeconds"));
            if (t < bestTime) { bestTime = t; best = so; }
        }
        if (best == null) best = (JSONObject) scores.get(0);

        Score s = newInstance(Score.class);
        setIfPresent(s, "setUsername",    String.class,  str(best.get("username")));
        setIfPresent(s, "setDifficulty",  String.class,  str(best.get("difficulty")));
        setIfPresent(s, "setTimeSeconds", long.class,    toLong(best.get("timeSeconds")));
        setIfPresent(s, "setTimeLeftSec", long.class,    toLong(best.get("timeLeftSec"))); // if your class has it
        setIfPresent(s, "setHintsUsed",   int.class,     toInt(best.get("hintsUsed")));
        setIfPresent(s, "setPuzzlesSolved", int.class,   toInt(best.get("puzzlesSolved")));
        setIfPresent(s, "setDate",        String.class,  str(best.get("date")));
        setIfPresent(s, "setScore",       long.class,    toLong(best.get("score")));
        return s;
    }

    /**
     * Builds a Leaderboard from playerData.json - "leaderboard".
     * Tries setEntries(List), then setLB(List), else falls back to addEntry(...).
     */
    public Leaderboard getLeaderboard() {
    JSONObject data = readObjectFromCandidates(PLAYER_CANDIDATES);
        JSONArray arr   = (JSONArray) data.get("leaderboard");

        Leaderboard lb = newInstance(Leaderboard.class);
        if (arr == null) return lb;

        ArrayList<Score> list = new ArrayList<>();
        for (Object o : arr) {
            JSONObject jo = (JSONObject) o;

            Score s = newInstance(Score.class);
            setIfPresent(s, "setUsername",    String.class, str(jo.get("username")));
            setIfPresent(s, "setDifficulty",  String.class, str(jo.get("difficulty")));
            setIfPresent(s, "setTimeSeconds", long.class,   toLong(jo.get("timeSeconds")));
            setIfPresent(s, "setTimeLeftSec", long.class,   toLong(jo.get("timeLeftSec")));
            setIfPresent(s, "setScore",       long.class,   toLong(jo.get("score")));
            setIfPresent(s, "setDate",        String.class, str(jo.get("date")));
            list.add(s);

            // addEntry(username, difficulty, timeSeconds, score, date)
            boolean added = setIfPresent(
                lb,
                "addEntry",
                new Class<?>[]{ String.class, String.class, long.class, long.class, String.class },
                new Object[]{ sSafe(s,"getUsername"), sSafe(s,"getDifficulty"), nSafe(s,"getTimeSeconds"), nSafe(s,"getScore"), sSafe(s,"getDate") }
            );
            if (added) {
                // if addEntry works, we don't need setEntries later
            }
        }

        boolean ok = setIfPresent(lb, "setEntries", List.class, list);
        if (!ok) setIfPresent(lb, "setLB", List.class, list);
        return lb;
    }

    /* ------------------------ helpers ------------------------ */

    private static JSONObject readObjectFromClasspath(String resourcePath) {
        try (var in = GameDataLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                return new JSONObject();
            }
            var r = new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8);
            return (JSONObject) new JSONParser().parse(r);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    /**
     * Try classpath resource first then a list of filesystem candidate paths.
     * Returns empty JSONObject when none found.
     */
    private static JSONObject readObjectFromCandidates(String[] candidates) {
        for (String c : candidates) {
            // try classpath
            try (var in = GameDataLoader.class.getClassLoader().getResourceAsStream(c)) {
                if (in != null) {
                    var r = new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8);
                    return (JSONObject) new JSONParser().parse(r);
                }
            } catch (Exception e) {
                // ignore and try filesystem
            }

            // try filesystem path
            try (var fr = new FileReader(c)) {
                return (JSONObject) new JSONParser().parse(fr);
            } catch (Exception e) {
                // ignore and try next candidate
            }
        }
        return new JSONObject();
    }


    private static String str(Object o) { return (o == null ? null : o.toString()); }

    private static int mapDifficultyToInt(String diff) {
        if (diff == null) return 0;
        switch (diff.trim().toUpperCase()) {
            case "EASY":   return 0;
            case "MEDIUM": return 1;
            case "HARD":   return 2;
            default:       return 0;
        }
    }

    private static long toLong(Object n) {
        if (n == null) return 0L;
        if (n instanceof Number) return ((Number) n).longValue();
        try { return Long.parseLong(n.toString()); } catch (Exception e) { return 0L; }
    }

    private static int toInt(Object n) {
        if (n == null) return 0;
        if (n instanceof Number) return ((Number) n).intValue();
        try { return Integer.parseInt(n.toString()); } catch (Exception e) { return 0; }
    }

    private static boolean setIfPresent(Object target, String setter, Class<?> param, Object value) {
        if (target == null || setter == null) return false;
        try {
            Method m = target.getClass().getMethod(setter, param);
            m.invoke(target, value);
            return true;
        } catch (NoSuchMethodException nsme) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    private static boolean setIfPresent(Object target, String method, Class<?>[] params, Object[] values) {
        if (target == null || method == null) return false;
        try {
            Method m = target.getClass().getMethod(method, params);
            m.invoke(target, values);
            return true;
        } catch (NoSuchMethodException nsme) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

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
        } catch (Exception e) {
            throw new RuntimeException(
                "Class " + type.getName() + " needs a public no-arg constructor for the loader.", e);
        }
    }

    //  getters used only for the leaderboard addEntry fallback
    private static String sSafe(Object obj, String getterName) {
        try { Object v = obj.getClass().getMethod(getterName).invoke(obj); return v == null ? null : v.toString(); }
        catch (Exception e) { return null; }
    }
    private static long nSafe(Object obj, String getterName) {
        try { Object v = obj.getClass().getMethod(getterName).invoke(obj); return toLong(v); }
        catch (Exception e) { return 0L; }
    }
     
    

}
