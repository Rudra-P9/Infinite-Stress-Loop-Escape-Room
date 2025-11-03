package com.escape.model;

import static org.junit.Assert.*;
import org.junit.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Unit tests for {@link GameDataWriter}.
 *
 * <p>These tests create/overwrite JSON fixtures at the same relative paths the writer
 * uses so we can exercise real file I/O without changing production code.</p>
 *
 * <p>Fixture roots (created per test):</p>
 * <ul>
 *   <li>escaperoom/src/main/resources/json/playerData.json</li>
 *   <li>escaperoom/src/main/resources/json/game.json</li>
 * </ul>
 *
 * <p>We verify these writer guarantees:</p>
 * <ul>
 *   <li>saveUser/saveUsers: append or replace (idempotent), preserve inventory</li>
 *   <li>saveScore: appends immutable history entries with required fields</li>
 *   <li>saveProgress: upsert by userUUID (one entry per user)</li>
 *   <li>saveLeaderboard: serializes current leaderboard entries</li>
 *   <li>saveRooms: emits minimal but coherent game.json structure</li>
 * </ul>
 */
public class GameDataWriterTest {

    private static final Path JSON_DIR =
            Path.of("escaperoom", "src", "main", "resources", "json");
    private static final Path PLAYER_JSON = JSON_DIR.resolve("playerData.json");
    private static final Path GAME_JSON   = JSON_DIR.resolve("game.json");

    private final JSONParser parser = new JSONParser();
    private GameDataWriter writer;

    // Build a clean JSON sandbox before each test. 
    @Before
    public void setUp() throws Exception {
        writer = new GameDataWriter();

        Files.createDirectories(JSON_DIR);

        // Minimal, empty playerData.json so writer can mutate/merge
        JSONObject player = new JSONObject();
        player.put("users",       new JSONArray());
        player.put("scores",      new JSONArray());
        player.put("leaderboard", new JSONArray());
        player.put("savedData",   new JSONArray());
        player.put("progress",    new JSONArray());
        try (FileWriter fw = new FileWriter(PLAYER_JSON.toFile())) {
            fw.write(player.toJSONString());
        }

        // Minimal game.json (will be overwritten by saveRooms as needed)
        JSONObject game = new JSONObject();
        game.put("rooms", new JSONArray());
        try (FileWriter fw = new FileWriter(GAME_JSON.toFile())) {
            fw.write(game.toJSONString());
        }
    }

    // Clean up generated files (kept shallow so git status stays clean). 
    @After
    public void tearDown() throws Exception {
        // inspect the JSON after a run
        Files.deleteIfExists(PLAYER_JSON);
        Files.deleteIfExists(GAME_JSON);
        // Do not remove directories; they’re harmless and simplify reruns.
    }

    // helpers

    private JSONObject readPlayerJson() throws Exception {
        try (FileReader fr = new FileReader(PLAYER_JSON.toFile())) {
            return (JSONObject) parser.parse(fr);
        }
    }

    private JSONObject readGameJson() throws Exception {
        try (FileReader fr = new FileReader(GAME_JSON.toFile())) {
            return (JSONObject) parser.parse(fr);
        }
    }

    private static User makeUser(UUID id, String name, String pw, String... items) {
        User u = new User(id, name, pw);
        Inventory inv = new Inventory(26);
        for (String it : items) inv.addItem(it);
        u.setInventory(inv);
        return u;
    }

    private static Score makeScore(String user, Difficulty diff, long timeLeft, long score) {
        return new Score(user, diff, timeLeft, new Date(0L), score);
    }

    // Users

    // saveUser should append a new user and persist inventory. 
    @Test
    public void saveUser_appendsAndPersistsInventory() throws Exception {
        User u = makeUser(UUID.randomUUID(), "alice", "pw1", "A", "B", "C");
        writer.saveUser(u);

        JSONObject root = readPlayerJson();
        JSONArray users = (JSONArray) root.get("users");
        assertEquals(1, users.size());

        JSONObject one = (JSONObject) users.get(0);
        assertEquals("alice", one.get("username"));
        assertEquals("pw1",   one.get("password"));

        JSONObject inv = (JSONObject) one.get("inventory");
        JSONArray items = (JSONArray) inv.get("items");
        assertEquals(3, items.size());
        assertTrue(items.contains("A"));
        assertTrue(((Number)inv.get("capacity")).intValue() >= 3);
    }

    // saveUser twice with same username should replace
    @Test
    public void saveUser_replacesByUsername_noDuplication() throws Exception {
        User u1 = makeUser(null, "bob", "pw1");
        writer.saveUser(u1);

        // change password -> should replace same entry, not append
        User u2 = makeUser(null, "bob", "pw2", "Z");
        writer.saveUser(u2);

        JSONObject root = readPlayerJson();
        JSONArray users = (JSONArray) root.get("users");
        assertEquals(1, users.size());
        JSONObject one = (JSONObject) users.get(0);
        assertEquals("bob", one.get("username"));
        assertEquals("pw2", one.get("password"));
    }

    // saveUsers should merge list by userID OR username. 
    @Test
    public void saveUsers_mergesByIdOrName() throws Exception {
        UUID id = UUID.randomUUID();
        User byId   = makeUser(id, "charlie", "pw1");
        User byName = makeUser(null, "charlie", "pw2"); // same username, no id
        ArrayList<User> list = new ArrayList<>();
        list.add(byId);
        list.add(byName);

        writer.saveUsers(list);

        JSONObject root = readPlayerJson();
        JSONArray users = (JSONArray) root.get("users");
        assertEquals("Expected merge without duplication", 1, users.size());
        JSONObject one = (JSONObject) users.get(0);
        assertEquals("charlie", one.get("username"));
        // Either pw1 or pw2 is fine (writer prefers last write), but must be one of them.
        assertTrue(Set.of("pw1","pw2").contains(one.get("password")));
        // Ensure a userID exists after merge (id preserved if present)
        assertNotNull(one.get("userID"));
    }

    // Scores

    // saveScore should append a historical record with required fields. 
    @Test
    public void saveScore_appendsHistoricalEntry() throws Exception {
        Score s = makeScore("dora", Difficulty.MEDIUM, 321L, 999L);
        writer.saveScore(s);

        JSONObject root = readPlayerJson();
        JSONArray scores = (JSONArray) root.get("scores");
        assertEquals(1, scores.size());

        JSONObject entry = (JSONObject) scores.get(0);
        assertEquals("dora", entry.get("username"));
        assertEquals("MEDIUM", entry.get("difficulty"));
        assertEquals(321L, ((Number) entry.get("timeSeconds")).longValue());
        assertEquals(999L, ((Number) entry.get("score")).longValue());
        assertNotNull("date should be serialized", entry.get("date"));
    }

    // Progress

    // saveProgress should upsert by userUUID (single entry updated on repeated saves) 
    @Test
    public void saveProgress_upsertsByUserUUID() throws Exception {
        UUID user = UUID.randomUUID();
        Progress p = new Progress(UUID.randomUUID(), user);
        p.setStoryPos(2);
        p.setQuestionsAnswered(2);
        p.setHintsUsed(1);

        writer.saveProgress(p);

        // Update and save again - should replace, not append
        p.setStoryPos(4);
        p.setQuestionsAnswered(4);
        p.setHintsUsed(3);
        writer.saveProgress(p);

        JSONObject root = readPlayerJson();
        JSONArray arr = (JSONArray) root.get("progress");
        assertEquals(1, arr.size());

        JSONObject only = (JSONObject) arr.get(0);
        assertEquals(user.toString(), only.get("userUUID"));
        assertEquals(4, ((Number) only.get("c")).intValue());
        assertEquals(4, ((Number) only.get("answered")).intValue());
        assertEquals(3, ((Number) only.get("hints")).intValue());
    }

    // Leaderboard

    // saveLeaderboard should serialize the current LB entries into playerData.json. 
    @Test
    public void saveLeaderboard_serializesEntries() throws Exception {
        Leaderboard lb = new Leaderboard();
        lb.addScore(makeScore("ella", Difficulty.HARD,   25,  500));
        lb.addScore(makeScore("fred", Difficulty.EASY,  600, 1200));
        writer.saveLeaderboard(lb);

        JSONObject root = readPlayerJson();
        JSONArray arr = (JSONArray) root.get("leaderboard");
        assertEquals(2, arr.size());

        // Collect usernames present
        Set<String> names = new HashSet<>();
        for (Object o : arr) {
            JSONObject e = (JSONObject) o;
            names.add((String) e.get("username"));
            assertNotNull(e.get("score"));
            assertNotNull(e.get("timeLeftSec")); // writer uses timeLeftSec for leaderboard
        }
        assertTrue(names.containsAll(Arrays.asList("ella","fred")));
    }

    // Rooms / game.json

    // saveRooms should emit minimal, coherent game.json structure with our single room. 
    @Test
    public void saveRooms_writesCoherentGameJson() throws Exception {
        Rooms r = new Rooms();
        r.setPuzzles(new ArrayList<>());  // writer only reads puzzles list
        ArrayList<Rooms> rooms = new ArrayList<>();
        rooms.add(r);

        writer.saveRooms(rooms);

        JSONObject game = readGameJson();
        assertTrue(game.containsKey("difficulties"));
        assertTrue(game.containsKey("timer"));
        assertTrue(game.containsKey("story"));
        assertTrue(game.containsKey("rooms"));

        JSONArray outRooms = (JSONArray) game.get("rooms");
        assertEquals(1, outRooms.size());
    }

    // SavedData

    // saveSavedData should append snapshots to savedData array. 
    @Test
    public void saveSavedData_appends() throws Exception {
        SavedData sd = new SavedData();
        sd.room = "room1";
        sd.score = 777;
        sd.hints = 2;
        sd.puzzle = "p1";

        writer.saveSavedData(sd);

        JSONObject root = readPlayerJson();
        JSONArray arr = (JSONArray) root.get("savedData");
        assertEquals(1, arr.size());

        JSONObject only = (JSONObject) arr.get(0);
        assertEquals("room1", only.get("room"));
        assertEquals(777, ((Number) only.get("score")).intValue());
        assertEquals(2,   ((Number) only.get("hints")).intValue());
        assertEquals("p1", only.get("puzzle"));
    }
}
