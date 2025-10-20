package com.escape.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RiddlePuzzle - self-contained riddle loader + puzzle.
 *
 * @author Rudra Patel
 */
public class RiddlePuzzle extends Puzzle {

    private static final String RESOURCE_PATH = "/riddles.txt";
    private static final List<RiddleEntry> CACHE = new ArrayList<>();
    private static boolean cacheLoaded = false;
    private static final Random RNG = new Random();

    // store solution locally to satisfy abstract getters/setters
    private String solution = "";

    // simple holder for riddle + answer
    private static class RiddleEntry {
        final String riddle;
        final String answer;
        RiddleEntry(String riddle, String answer) { this.riddle = riddle; this.answer = answer; }
    }

    /** Default constructor: picks a random riddle from the cached resource. */
    public RiddlePuzzle() {
        super();
        ensureCacheLoaded();
        if (!CACHE.isEmpty()) {
            RiddleEntry e = CACHE.get(RNG.nextInt(CACHE.size()));
            this.setPuzzleID("riddle-" + System.currentTimeMillis());
            this.setTitle("Random Riddle");
            this.setObjective(e.riddle);
            this.setSolution(e.answer);
            this.setSolved(false);
        } else {
            // fallback stub riddle
            this.setPuzzleID("riddle-none");
            this.setTitle("Riddle (none)");
            this.setObjective("No riddle available.");
            this.setSolution("");
            this.setSolved(false);
        }
    }

    /** Explicit constructor (useful for JSON-defined riddles or tests). */
    public RiddlePuzzle(String id, String title, String objective, String solution) {
        super();
        this.setPuzzleID(id);
        this.setTitle(title);
        this.setObjective(objective);
        this.setSolution(solution == null ? "" : solution);
        this.setSolved(false);
    }

    /* -----------------------
       Implementation of abstract Puzzle API
       ----------------------- */

    /**
     * Set the solution for this puzzle.
     * This implements Puzzle.setSolution(String).
     */
    @Override
    public void setSolution(String solution) {
        this.solution = solution == null ? "" : solution;
    }

    /**
     * Return the solution (as String).
     * This implements Puzzle.getSolution().
     */
    @Override
    public String getSolution() {
        return this.solution;
    }

    /**
     * Check the provided answer against the solution.
     * This implements Puzzle.checkAnswer(String).
     *
     * We treat solutions case-insensitively and trim whitespace.
     * If correct, mark puzzle solved and return true.
     */
    @Override
    public boolean checkAnswer(String answer) {
        if (answer == null) return false;
        String correct = (this.getSolution() == null) ? "" : this.getSolution().trim().toLowerCase();
        String attempt = answer.trim().toLowerCase();
        boolean ok = !correct.isEmpty() && correct.equals(attempt);
        if (ok) this.setSolved(true);
        return ok;
    }

    /* -----------------------
       Backwards-compatible helper (attempt)
       ----------------------- */

    /**
     * Backwards-compatible attempt method (calls checkAnswer).
     */
    public boolean attempt(String answer) {
        return checkAnswer(answer);
    }

    @Override
    public String toString() {
        return "RiddlePuzzle{" +
                "id=" + this.getPuzzleID() +
                ", objective=" + this.getObjective() +
                ", solved=" + this.isSolved() +
                "}";
    }

    /* ---------------------------
       Static: load & parse resource
       --------------------------- */
    private static synchronized void ensureCacheLoaded() {
        if (cacheLoaded) return;
        cacheLoaded = true; // mark true even on error to avoid retry loops
        try (InputStream in = RiddlePuzzle.class.getResourceAsStream(RESOURCE_PATH)) {
            if (in == null) {
                System.err.println("[RiddlePuzzle] Resource not found: " + RESOURCE_PATH);
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    String[] parts = line.split("::");
                    if (parts.length >= 2) {
                        String riddle = parts[0].trim();
                        String answer = parts[1].trim();
                        // join any extra :: parts into answer (if present)
                        for (int i = 2; i < parts.length; i++) answer += "::" + parts[i].trim();
                        CACHE.add(new RiddleEntry(riddle, answer));
                    } else {
                        // If no delimiter, store riddle with empty answer (won't be solvable automatically)
                        CACHE.add(new RiddleEntry(line, ""));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[RiddlePuzzle] Failed to load riddles: " + e.getMessage());
        }
    }
}
