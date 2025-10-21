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
            this.riddleSolution = chosen.answer;
            this.setSolution(chosen.answer);
        } else {
            this.setObjective("No riddles available!");
            this.solution = "";
        }
    }

    /**
     * Explicit constructor for manual creation.
     */
    public RiddlePuzzle(String id, String title, String objective, String solution, Difficulty difficulty) {
        super(id, title, objective, solution, false, difficulty, "Riddle", "RiddlePuzzle");
        this.solution = solution;
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

                    String[] parts = line.split("::");
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

    @Override
    public String getSolution() {
        return riddleSolution;
    }

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

    @Override
    public String toString() {
        return "RiddlePuzzle{" +
                "objective='" + getObjective() + '\'' +
                ", solved=" + solved() +
                ", solution='" + riddleSolution + '\'' +
                '}';
    }
}
