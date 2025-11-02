package com.escape.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * RiddlePuzzle - self-contained riddle loader + puzzle.
 * Loads riddles from riddles.txt where each line is:
 * riddle text :: answer
 *
 * @author Rudra Patel
 */
public class RiddlePuzzle extends Puzzle {

    private static final String RIDDLE_FILE = "/riddles.txt";
    private static final List<Riddle> riddles = new ArrayList<>();
    private static boolean riddlesLoaded = false;
    private static final Random random = new Random();

    private String riddleSolution; // implements abstract get/setSolution

    // Inner class for riddle storage
    private static class Riddle {
        String question;
        String answer;

        Riddle(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    /**
     * Default constructor — randomly selects a riddle from the text file.
     */
    public RiddlePuzzle() {
        super(
            "riddle-" + System.currentTimeMillis(),
            "Random Riddle",
            "Solve the riddle below.",
            "",
            false,
            Difficulty.EASY,
            "Riddle",
            "RiddlePuzzle"
        );

        loadRiddlesIfNeeded();
        if (!riddles.isEmpty()) {
            Riddle chosen = riddles.get(random.nextInt(riddles.size()));
            this.setObjective(chosen.question);
            // use setSolution to ensure trimming/null-safety
            this.setSolution(chosen.answer);
        } else {
            this.setObjective("No riddles available!");
            // ensure riddleSolution is initialized consistently
            setSolution("");
        }
    }

    /**
     * Explicit constructor for manual creation.
     */
    public RiddlePuzzle(String id, String title, String objective, String solution, Difficulty difficulty) {
        super(id, title, objective, solution, false, difficulty, "Riddle", "RiddlePuzzle");
        // ensure riddleSolution is initialized consistently
        setSolution(solution);
    }

    /**
     * Loads riddles from the text file once and caches them.
     */
    private static synchronized void loadRiddlesIfNeeded() {
        if (riddlesLoaded) return;

        try (InputStream in = RiddlePuzzle.class.getResourceAsStream(RIDDLE_FILE)) {
            if (in == null) {
                System.err.println("[RiddlePuzzle] File not found: " + RIDDLE_FILE);
                riddlesLoaded = true;
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;

                    // safer split: literal "::" and limit to 2 parts
                    String[] parts = line.split(java.util.regex.Pattern.quote("::"), 2);
                    if (parts.length >= 2) {
                        String question = parts[0].trim();
                        String answer = parts[1].trim();
                        riddles.add(new Riddle(question, answer));
                    }
                }
            }
            riddlesLoaded = true;
        } catch (Exception e) {
            System.err.println("[RiddlePuzzle] Error loading riddles: " + e.getMessage());
            riddlesLoaded = true;
        }
    }

    /**
     * Returns the solution to the riddle as a string.
     * @return the solution as a string
     */
    @Override
    public String getSolution() {
        return riddleSolution;
    }

    /**
     * Sets the solution to the riddle.
     * If the solution is null, it is set to an empty string.
     * Otherwise, it is trimmed to remove leading and trailing whitespace.
     * @param solution the solution to set
     */
    @Override
    public void setSolution(String solution) {
        this.riddleSolution = (solution == null) ? "" : solution.trim();
    }

    /**
     * Checks the player's answer against the solution.
     * Case-insensitive; trims whitespace.
     */
    @Override
    public boolean checkAnswer(String input) {
        if (input == null || riddleSolution == null) return false;
        boolean correct = riddleSolution.trim().equalsIgnoreCase(input.trim());
        if (correct) setSolved(true);
        return correct;
    }

    /**
     * Returns a string representation of the RiddlePuzzle object.
     * The string representation includes the objective, solved status, and solution.
     * 
     * @return a string representation of the RiddlePuzzle object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append("{");

        // Always include objective and solved state (we know these exist)
        sb.append("objective='").append(getObjective()).append('\'');
        sb.append(", solved=").append(solved());

        // Try to include other common getters if they exist on the superclass.
        String[] optionalGetters = {"getTitle", "getSolution", "getDifficulty", "getId"};
        for (String name : optionalGetters) {
            try {
                java.lang.reflect.Method m = this.getClass().getMethod(name);
                Object val = m.invoke(this);
                sb.append(", ").append(name).append("='").append(val).append('\'');
            } catch (NoSuchMethodException nsme) {
                // getter not present — ignore
            } catch (Exception e) {
                // if invoking fails for any reason, include a placeholder rather than throwing
                sb.append(", ").append(name).append("='<error>'");
            }
        }

        // Always include the internal riddleSolution as a final fallback
        sb.append(", riddleSolution='").append(riddleSolution).append('\'');
        sb.append('}');
        return sb.toString();
    }
}