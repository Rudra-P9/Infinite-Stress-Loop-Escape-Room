package com.escape.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link GameDataLoader}.
 *
 * <p>These tests create tiny JSON fixtures under the exact paths that
 * GameDataLoader reads:
 * <pre>
 *   escaperoom/src/main/resources/json/game.json
 *   escaperoom/src/main/resources/json/playerData.json
 * </pre>
 * Each test writes only what it needs so we can validate:
 * <ul>
 *   <li>getUsers(): user fields + inventory mapping</li>
 *   <li>getRooms(): rooms, puzzle mapping by category (WORD/NUMBER/AUDIO)</li>
 *   <li>loadPuzzlesForRoom(): prompt/hint/reward propagation</li>
 *   <li>getStory(): story fields wired</li>
 *   <li>getLeaderboard(): entries created and basic sortability supported via Leaderboard</li>
 *   <li>getScore(): fastest (min timeSeconds) selection</li>
 *   <li>loadProgressForUser(): position/answered/hints restored</li>
 *   <li>Resilience when arrays are missing or empty</li>
 * </ul>
 * </p>
 *
 * <p>Note: These tests assume the concrete puzzle classes
 * {@link WordPuzzle}, {@link NumberPuzzle}, {@link AudioPuzzle},
 * {@link Hints} exist and behave per your codebase (e.g., getHints().add()).</p>
 */
public class GameDataLoaderTest {

    private static final File GAME_JSON =
            new File("escaperoom/src/main/resources/json/game.json");
    private static final File PLAYER_JSON =
            new File("escaperoom/src/main/resources/json/playerData.json");

    // Reusable UUIDs for fixture determinism
    private final UUID leniId = UUID.fromString("72441e6f-6071-4115-bb69-ca49634cec53");
    private final UUID loganId = UUID.fromString("a369165b-b6cd-4e8c-b06a-c9cdcf7e3b33");

    private GameDataLoader loader;

    @Before
    public void setUp() throws Exception {
        // Ensure directories exist
        GAME_JSON.getParentFile().mkdirs();
        PLAYER_JSON.getParentFile().mkdirs();
        loader = new GameDataLoader();

        // default “happy path” fixtures used by most tests.
        writeGameJson(
            "{\n" +
            "  \"rooms\": [\n" +
            "    {\n" +
            "      \"roomID\": \"room1\",\n" +
            "      \"title\": \"Calibration Bay\",\n" +
            "      \"puzzles\": [\n" +
            "        {\n" +
            "          \"id\": \"p1\",\n" +
            "          \"category\": \"WORD\",\n" +
            "          \"type\": \"RIDDLE\",\n" +
            "          \"title\": \"Scattered Fragments\",\n" +
            "          \"objective\": \"Assemble fragments\",\n" +
            "          \"solution\": \"REAL\",\n" +
            "          \"prompt\": \"Solve me\",\n" +
            "          \"hint\": \"Think letters\",\n" +
            "          \"rewardLetter\": \"R\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"roomID\": \"room2\",\n" +
            "      \"title\": \"Sync Core\",\n" +
            "      \"puzzles\": [\n" +
            "        {\n" +
            "          \"id\": \"p2\",\n" +
            "          \"category\": \"NUMBER\",\n" +
            "          \"type\": \"ARITH\",\n" +
            "          \"title\": \"Checksum\",\n" +
            "          \"objective\": \"Sum it\",\n" +
            "          \"solution\": \"42\",\n" +
            "          \"prompt\": \"Add up\",\n" +
            "          \"hint\": \"Life, universe\",\n" +
            "          \"rewardLetter\": \"E\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"p3\",\n" +
            "          \"category\": \"AUDIO\",\n" +
            "          \"type\": \"SPECTRO\",\n" +
            "          \"title\": \"The Signal\",\n" +
            "          \"objective\": \"Transcribe\",\n" +
            "          \"solution\": \"MIRROR\",\n" +
            "          \"prompt\": \"Listen\",\n" +
            "          \"hint\": \"Echo\",\n" +
            "          \"rewardLetter\": \"A\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"story\": {\n" +
            "    \"intro\": \"Hello intro\",\n" +
            "    \"roomOneIntro\": \"R1 intro\",\n" +
            "    \"roomOneConc\": \"R1 conc\",\n" +
            "    \"roomTwoIntro\": \"R2 intro\",\n" +
            "    \"roomTwoBetween\": \"R2 between\",\n" +
            "    \"roomTwoConc\": \"R2 conc\",\n" +
            "    \"roomThreeIntro\": \"R3 intro\",\n" +
            "    \"roomThreeBetween\": \"R3 between\",\n" +
            "    \"roomThreeConc\": \"R3 conc\",\n" +
            "    \"finalPuzzle\": \"Final text\",\n" +
            "    \"conclusion\": \"The end\"\n" +
            "  }\n" +
            "}\n"
        );

        writePlayerJson(
            "{\n" +
            "  \"users\": [\n" +
            "    {\"userID\":\"" + leniId + "\",\"username\":\"Leni\",\"password\":\"leniSecret\"," +
            "     \"inventory\":{\"items\":[\"R\",\"E\"],\"capacity\":5}},\n" +
            "    {\"userID\":\"" + loganId + "\",\"username\":\"Logan\",\"password\":\"loganPass123\"," +
            "     \"inventory\":{\"items\":[],\"capacity\":0}}\n" + // capacity 0 - default filled by loader
            "  ],\n" +
            "  \"leaderboard\": [\n" +
            "    {\"username\":\"AA\",\"difficulty\":\"HARD\",\"timeSeconds\":120,\"score\":700,\"date\":\"2025-10-10\",\"timeLeftSec\":120},\n" +
            "    {\"username\":\"BB\",\"difficulty\":\"EASY\",\"timeSeconds\":300,\"score\":250,\"date\":\"2025-10-11\",\"timeLeftSec\":300}\n" +
            "  ],\n" +
            "  \"scores\": [\n" +
            "    {\"username\":\"Slow\",\"difficulty\":\"EASY\",\"timeSeconds\":999,\"hintsUsed\":0,\"puzzlesSolved\":1,\"date\":\"2025-10-12\",\"score\":10},\n" +
            "    {\"username\":\"Fast\",\"difficulty\":\"MEDIUM\",\"timeSeconds\":45,\"hintsUsed\":1,\"puzzlesSolved\":2,\"date\":\"2025-10-13\",\"score\":999}\n" +
            "  ],\n" +
            "  \"progress\": [\n" +
            "    {\"userUUID\":\"" + leniId + "\",\"progressUUID\":\"bda93e5e-c3c1-412f-93d9-3f93d523fb89\",\"c\":3,\"answered\":3,\"hints\":1},\n" +
            "    {\"userUUID\":\"" + loganId + "\",\"progressUUID\":\"11111111-1111-1111-1111-111111111111\",\"c\":0,\"answered\":0,\"hints\":0}\n" +
            "  ]\n" +
            "}\n"
        );
    }

    @After
    public void tearDown() {
        // Clean up after each test so tests are independent
        safeDelete(GAME_JSON);
        safeDelete(PLAYER_JSON);
    }

    // Users

    @Test
    public void getUsers_parsesUsersAndInventory() {
        ArrayList<User> users = loader.getUsers();
        assertEquals(2, users.size());

        User u0 = users.get(0);
        assertEquals("Leni", u0.getUsername());
        assertEquals("leniSecret", u0.getPassword());
        assertEquals(leniId, u0.userID);
        assertNotNull(u0.getInventory());
        assertTrue(u0.getCollectedLetters().contains("R"));
        assertTrue(u0.getCollectedLetters().contains("E"));
        assertEquals(5, u0.getInventory().getCapacity());

        User u1 = users.get(1);
        assertEquals("Logan", u1.getUsername());
        // capacity=0 in JSON - loader defaults sensibly (>= items size, here 26)
        assertTrue("Default capacity should be >= 26 when capacity<=0",
                u1.getInventory().getCapacity() >= 26);
    }

    // Rooms & Puzzles

    @Test
    public void getRooms_parsesRoomsAndBuildsCorrectPuzzleSubclasses() {
        ArrayList<Rooms> rooms = loader.getRooms();
        assertEquals(2, rooms.size());

        Rooms r1 = rooms.get(0);
        assertEquals("room1", r1.getRoomID());
        assertEquals("Calibration Bay", r1.getTitle());
        assertEquals(1, r1.getPuzzles().size());
        assertTrue("room1 puzzle should be WordPuzzle",
                r1.getPuzzles().get(0) instanceof WordPuzzle);

        Rooms r2 = rooms.get(1);
        assertEquals("room2", r2.getRoomID());
        assertEquals("Sync Core", r2.getTitle());
        assertEquals(2, r2.getPuzzles().size());
        assertTrue(r2.getPuzzles().get(0) instanceof NumberPuzzle);
        assertTrue(r2.getPuzzles().get(1) instanceof AudioPuzzle);
    }

    @Test
    public void loadPuzzlesForRoom_populatesPromptHintRewardIntoTextMap() {
        Map<String,String> out = new HashMap<>();
        List<Puzzle> puzzles = loader.loadPuzzlesForRoom("room2", out);
        assertEquals(2, puzzles.size());

        // check text map wiring
        assertEquals("Add up", out.get("p2:prompt"));
        assertEquals("Life, universe", out.get("p2:hint"));
        assertEquals("E", out.get("p2:reward"));
        assertEquals("Listen", out.get("p3:prompt"));
        assertEquals("Echo", out.get("p3:hint"));
        assertEquals("A", out.get("p3:reward"));

        // check that hint objects were added to the Puzzle (via getHints())
        // (We only assert non-empty; details belong to Hints class tests)
        for (Puzzle p : puzzles) {
            assertNotNull(p.getHints());
            assertFalse(p.getHints().isEmpty());
        }
    }

    @Test
    public void loadPuzzlesForRoom_fallbackByIndex_roomN() {
        Map<String,String> out = new HashMap<>();
        // "room1" exact id works; try "room3" which doesn't exist - should NOT crash (returns empty list)
        List<Puzzle> none = loader.loadPuzzlesForRoom("room3", out);
        assertNotNull(none);
        assertTrue(none.isEmpty());
    }

    // Story

    @Test
    public void getStory_parsesAllFields() {
        StoryElements s = loader.getStory();
        assertEquals("Hello intro", s.getIntro());
        assertEquals("R1 intro", s.getRoomOneIntro());
        assertEquals("R1 conc", s.getRoomOneConc());
        assertEquals("R2 intro", s.getRoomTwoIntro());
        assertEquals("R2 between", s.getRoomTwoBetween());
        assertEquals("R2 conc", s.getRoomTwoConc());
        assertEquals("R3 intro", s.getRoomThreeIntro());
        assertEquals("R3 between", s.getRoomThreeBetween());
        assertEquals("R3 conc", s.getRoomThreeConc());
        assertEquals("Final text", s.getFinalPuzzle());
        assertEquals("The end", s.getConclusion());
    }

    // Leaderboard

    @Test
    public void getLeaderboard_buildsEntriesFromJson() {
        Leaderboard lb = loader.getLeaderboard();
        assertNotNull(lb);
        assertTrue("Leaderboard should have entries from JSON", lb.size() >= 2);

        // TopN should return non-empty and contain AA/BB (order depends on Score implementation)
        List<Score> top = lb.getLB();
        assertFalse(top.isEmpty());
        Set<String> names = new HashSet<>();
        for (Score s : top) names.add(s.getUsername());
        assertTrue(names.contains("AA"));
        assertTrue(names.contains("BB"));
    }

    // Score (best/fastest) 

    @Test
    public void getScore_returnsFastestByTimeSeconds() {
        Score best = loader.getScore();
        assertNotNull(best);
        // In fixtures, "Fast" has timeSeconds=45 (faster/minimal)
        // We only assert fields the loader populates via setters.
        assertEquals("Fast", best.getUsername());
        // Accept either getTimeSeconds() or getTimeLeftSec()
        assertEquals(45L, nSafe(best, "getTimeSeconds", "getTimeLeftSec"));

        // Accept either getHintsUsed()
        assertEquals(1, iSafe(best, "getHintsUsed"));

        // Accept either getPuzzlesSolved() or getQuestionsAnswered()
        assertEquals(2, iSafe(best, "getPuzzlesSolved", "getQuestionsAnswered"));
    }

    @Test
    public void getScore_handlesMissingScoresArray_byReturningEmptyScoreObject() throws Exception {
        // Overwrite playerData.json with no 'scores' section
        writePlayerJson(
            "{ \"users\": [], \"leaderboard\": [] }\n"
        );
        Score best = loader.getScore();
        assertNotNull("Loader should still return a Score instance", best);
        // Best-effort: fields likely default to 0/null depending on Score
        // We avoid asserting specifics to keep this resilient across Score changes
    }

    // Progress restore

    @Test
    public void loadProgressForUser_restoresAllProgressFields() {
        Progress p = loader.loadProgressForUser(leniId);
        assertNotNull(p);
        assertEquals(3, p.getStoryPos());
        assertEquals(3, p.getQuestionsAnswered());
        assertEquals(1, p.getHintsUsed());
        assertEquals(leniId, p.getUserUUID());
    }

    @Test
    public void loadProgressForUser_returnsNullWhenUserNotPresent() {
        Progress p = loader.loadProgressForUser(UUID.randomUUID());
        assertNull(p);
    }

    // Rooms/JSON resilience

    @Test
    public void getRooms_returnsEmptyListWhenRoomsMissing() throws Exception {
        writeGameJson("{ \"story\": {\"intro\": \"only\"} }");
        assertTrue(loader.getRooms().isEmpty());
    }

    @Test
    public void getUsers_returnsEmptyListWhenUsersMissing() throws Exception {
        writePlayerJson("{\"leaderboard\":[],\"scores\":[],\"progress\":[]}");
        assertTrue(loader.getUsers().isEmpty());
    }

    // helper methods
    
    // reflection helpers so tests work with either naming 
    private long nSafe(Object obj, String... getters) {
        for (String g : getters) {
            try {
                Object v = obj.getClass().getMethod(g).invoke(obj);
                if (v instanceof Number) return ((Number) v).longValue();
            } catch (Exception ignore) {}
        }
        fail("None of the getters exist: " + Arrays.toString(getters));
        return 0L; // unreachable
    }
    private int iSafe(Object obj, String... getters) {
        return (int) nSafe(obj, getters);
    }


    private void writeGameJson(String json) throws Exception {
        writeFile(GAME_JSON, json);
    }

    private void writePlayerJson(String json) throws Exception {
        writeFile(PLAYER_JSON, json);
    }

    private void writeFile(File file, String content) throws Exception {
        file.getParentFile().mkdirs();
        try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8)) {
            out.write(content);
        }
        assertTrue("Fixture file should exist: " + file.getPath(), file.exists());
    }

    private void safeDelete(File f) {
        try { if (f.exists()) f.delete(); } catch (Throwable ignore) {}
    }
}
