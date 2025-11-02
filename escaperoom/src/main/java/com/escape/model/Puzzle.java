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
 * @author Rudra Patel
 * @version 1.2
 */
public abstract class Puzzle {

    protected String puzzleID;
    protected String title;
    protected String objective;
    protected boolean solved;
    protected String solution;
    protected String category;
    protected String type;
    protected String prompt;
    protected String rewardLetter; 
    protected String hint; 


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
        this.solution = solution;
        this.solved = solved;
        this.difficulty = difficulty;
        this.category = category;
        this.type = type;
    }

    /**
     * Constructor for puzzles with basic parameters.
     */
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
        if(hints != null && !hints.isEmpty()) {
            for(Hints h: hints) {
                if(h.getText() != null && !h.getText().isEmpty()) {
                    h.reveal();
                    return h.getText();
                }
            }
            return "All hints for this puzzle have been used.";
        }
        if (hint != null && !hint.isEmpty()) {
            return hint;
        }
        return "No hint for this puzzle!";
    }

    /**
     * Returns the text of the hint at the given index without revealing it.
     * If the index is out of bounds or there are no hints, returns null.
     * @param index the index of the hint to peek
     * @return the text of the hint at the given index, or null if out of bounds or no hints
     */
    public String peekHint(int index) {
        if (hints != null && index >= 0 && index < hints.size()) {
            return hints.get(index).getText();
        }
        return null;
    }


    /**
     * Returns the total number of hints associated with this puzzle.
     * If this puzzle has no hints associated with it, returns 0.
     * @return the total number of hints
     */
    public int getHintCount() {
        return hints == null ? 0 : hints.size();
    }

    /**
     * Returns the number of revealed hints associated with this puzzle.
     * If this puzzle has no hints associated with it, returns 0.
     * @return the number of revealed hints
     */
    public int getRevealedHintCount() {
        if (hints == null || hints.isEmpty()) return 0;

        int count = 0;
        for (Hints h : hints) {
            if (h.isRevealed()) count++;
        }
        return count;
    }

    /**
     * Returns true if there is at least one unrevealed hint in this puzzle.
     * If this puzzle has no hints associated with it, returns true if there is a hint text.
     * Otherwise, returns false.
     * @return true if there is at least one unrevealed hint, false otherwise
     */
    public boolean hasHintsRemaining() {
        if (hints == null || hints.isEmpty()) {
            return hint != null && !hint.isEmpty();
        }

        for (Hints h : hints) {
            if (!h.isRevealed()) return true;
        }
        return false;
    }

    /**
     * Resets all hints associated with this puzzle to unrevealed status.
     * This method is used to reset the puzzle after a game restart.
     */
    public void reserHints() {
        if (hints != null) {
            for (Hints h : hints) {
                h.setRevealed(false);
            }
        }
    }

    /**
     * List to get populated from JSON.
     * @return LIST OF HINTS
     */
    public List<Hints> getHints() {
        return hints;
    }

    /**
     * Adds a hint to the list of hints for this puzzle.
     * @param hint the hint to be added
     */
    public void addHint(Hints hint) {
        if (hint != null) {
            this.hints.add(hint);
        }
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

    /**
     * Gets the prompt text for this puzzle.
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Sets the prompt text for this puzzle.
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

     /**
     * Returns the reward letter given after solving the puzzle.
     */
    public String getRewardLetter() {
        return rewardLetter;
    }

    /**
     * Sets the reward letter for the puzzle (from JSON).
     */
    public void setRewardLetter(String rewardLetter) {
        this.rewardLetter = rewardLetter;
    }

    /**
     * Sets the simple hint text (if any) associated with this puzzle.
     * 
     * @param hint the simple hint text to assign
     */
    public void setSimpleHint(String hint) {
        this.hint = hint;
    }

    /**
     * Retrieves the simple hint text (if any) associated with this puzzle.
     * 
     * @return the simple hint text, or null if no hint is available
     */
    public String getSimpleHint() {
        return hint;
    }

    public String getHintStatus() {
        String puzzleTitle = (title != null) ? title : "Math Challenge";

        if (!puzzleTitle.equals("Math Challenge")) {
            puzzleTitle = "Math Challenge";
        }

        int availableHints = (hints != null) ? hints.size() : 0;
        String status = puzzleTitle + " | Hints available: " + availableHints;

        return status;
    }

}