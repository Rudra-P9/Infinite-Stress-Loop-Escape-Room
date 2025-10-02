package com.escape.model;

import java.util.UUID;

/**
 * Abstract base class representing a puzzle for the escaperoom.
 * Each puzzle has a unique ID, title, objective, and solved status.
 * It also includes a hint and basic data for gameplay logic.
 * 
 * @author Jacob Kinard
 */
public abstract class Puzzle {

    protected UUID puzzleID;
    protected String title;
    protected String objective;
    protected boolean solved;

    Hints hint = new Hints(title, 0, solved, objective);

    /**
     * Returns the hint associated with this puzzle.
     *
     * @return the hint string
     */
    public String getHint() {
        return hint.getHint();
    }

    /**
     * Indicates whether the puzzle has been solved.
     *
     * @return true if solved, false otherwise
     */
    public boolean solved() {
        return true;
    }

    /**
     * Sets the unique identifier for this puzzle.
     *
     * @param puzzleID the UUID to assign
     */
    public void setPuzzleID(UUID puzzleID) {
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
    public int getDifficultyLevel() {
        return 1; //will need to be tweaked to be dynamic
    }

    /**
     * Returns the solution to the puzzle.
     *
     * @return the solution string
     */
    public String getSolution() {
        return "solution"; //same idea as setSolution needed GameDataLoader
    }

    /**
     * Sets the solution for the puzzle.
     *
     * @param solution the solution string to assign
     */
    public void setSolution(String solution) {
        // this.solution = solution; or something. will need GameDataLaoader to load the solutions
    }

    /**
     * Returns the unique identifier of the puzzle.
     *
     * @return the puzzle UUID
     */
    public UUID getPuzzleID() {
        return puzzleID;
    }
}