package com.escape.model;
import java.util.HashMap;

/**
 * Stores information about a saved game state.
 *
 * Keeps track of the current room, score, hints, and puzzle details.
 * Also includes a map for any extra data that needs to be saved
 * @author Dylan Diaz
 */
public class SavedData {

    /**
     * The room where the game was saved.
     */
    protected String room;

    /**
     * The player’s score when the game was saved.
     */
    protected int score;

    /**
     * Number of hints used so far.
     */
    protected int hints;

    /**
     * The puzzle being worked on at the time of saving.
     */
    protected String puzzle;

    /**
     * Additional saved data in key-value form.
     */
    protected HashMap<String, String>[] saveData;

    //constructors
    public SavedData() {}

    public SavedData(String room, int score, int hints, String puzzle) {
        this.room = room;
        this.score = score;
        this.hints = hints;
        this.puzzle = puzzle;
    }

    //getters
    public String getRoom() { return room; }
    public int getScore() { return score; }
    public int getHints() { return hints; }
    public String getPuzzle() { return puzzle; }
    @SuppressWarnings("unchecked")
    public HashMap<String, String>[] getSaveData() { return saveData; }

    //setters
    public void setRoom(String room) { this.room = room; }
    public void setScore(int score) { this.score = score; }
    public void setHints(int hints) { this.hints = hints; }
    public void setPuzzle(String puzzle) { this.puzzle = puzzle; }
    public void setSaveData(HashMap<String, String>[] saveData) { this.saveData = saveData; }
}