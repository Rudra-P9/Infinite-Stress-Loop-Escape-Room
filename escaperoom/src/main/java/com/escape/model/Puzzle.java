package com.escape.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a puzzle for the escaperoom.
 * Will include inheritence for following puzzle subclasses.
 * Each puzzle has a unique ID, title, objective, and solved status.
 * It also includes a hint and basic data for gameplay logic.
 * 
 * @author Jacob Kinard & Talan Kinard
 */
public abstract class Puzzle {

    protected String puzzleID;
    protected String title;
    protected String objective;
    protected boolean solved;
    protected String solution;
    protected String category;
    protected String type;

    //Difficulty enum
    protected Difficulty difficulty;

    /**
     * Hints for the puzzle
     */
    protected final List<Hints> hints = new ArrayList<>();

    /**
     * Constructor to interact with subclasses.
     * @param title puzzle title
     * @param objective objective text displayed to user
     * @param solved solved state
     * @param difficulty difficulty enum
     */
    protected Puzzle(String puzzleID, String title, String objective, String solution, boolean solved, Difficulty difficulty, String category, String type) {
        this.puzzleID = puzzleID;
        this.title = title;
        this.objective = objective;
        this.solved = solved;
    }

    protected Puzzle(String puzzleID, String title, String objective, String solution, String category, String type) {
        this.puzzleID = puzzleID;
        this.title = title;
        this.objective = objective;
        this.solution = solution;
        this.category = category;
        this.type = type;
        this.solved = false;
    }

    /**
     * Checks user input versus the solution
     * @param input player input
     * @return true if correct false otherwise
     */
    public abstract boolean checkAnswer(String input);

    /**
     * Returns the puzzle solution string wise.
     * @return solution as String
     */
    public abstract String getSolution();

    /**
     * Sets solution. 
     * @param solution
     */
    public abstract void setSolution(String solution);

    /**
     * Grabs first hint object
     *
     * @return a hint or either a message saying there isn't a hint.
     */
    public String getHint() {
        if(hints.isEmpty() || hints == null) {
            return "No hint for this puzzle!";
        }
        for(Hints h: hints) {
            if(!h.revealed) {
                return h.text;
            }
        }
        return "No hint for this puzzle!";
    }

    /**
     * List to get populated from JSON.
     * @return LIST OF HINTS
     */
    public List<Hints> getHints() {
        return hints;
    }

    /**
     * Indicates whether the puzzle has been solved.
     *
     * @return true if solved, false otherwise
     */
    public boolean solved() {
        return solved;
    }

    /**
     * Subclasses/Facade to update solved state
     * @param solved
     */
    protected void setSolved(boolean solved) {
        this.solved = solved;
    }

    /**
     * 
     * @return puzzle UUID
     */
    public String getPuzzleID() {
        return puzzleID;
    }

    /**
     * Sets the unique identifier for this puzzle.
     *
     * @param puzzleID the UUID to assign
     */
    public void setPuzzleID(String puzzleID) {
        this.puzzleID = puzzleID;
    }

    /**
     * Returns the title of the puzzle.
     *
     * @return the puzzle title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the puzzle.
     *
     * @param title the puzzle title to assign
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the objective or goal of the puzzle.
     *
     * @return the puzzle objective
     */
    public String getObjective() {
        return objective;
    }

    /**
     * Sets the objective or goal of the puzzle.
     *
     * @param objective the puzzle objective to assign
     */
    public void setObjective(String objective) {
        this.objective = objective;
    }

    /**
     * Returns the difficulty level of the puzzle.
     *
     * @return the difficulty level as an integer
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets difficulty, safety check for null.
     * @param difficulty
     */
    public void setDifficulty(Difficulty difficulty) {
        if(difficulty == null) {
            throw new IllegalArgumentException("difficulty failed.");
        }
        this.difficulty = difficulty;
    }

}