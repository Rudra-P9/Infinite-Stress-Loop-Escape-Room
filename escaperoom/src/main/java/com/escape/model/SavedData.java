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
}
