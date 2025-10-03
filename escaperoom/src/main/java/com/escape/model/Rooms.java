package com.escape.model;

/**
 * Rooms of the EscapeRoom
 * Each room has an ID, puzzles, and a difficulty level.
 * 
 * @author Talan Kinard
 */

public class Rooms 
{
    /**
     * List of rooms.
     */
    private ArrayList<Room> rooms;

    /**
     * Unique identifier for the room.
     */
    private String roomID;
    private ArrayList<Puzzle> puzzles;
    private int difficulty;

    /**
     * Adds a puzzle to the room.
     * @param p the puzzle to be added.
     */
    public void addPuzzle(Puzzle p)
    {

    }

    /**
     * Returns the list of puzzles associated with the room.
     * @return
     */
    public ArrayList<Puzzle> getPuzzles()
    {
        return null;
    }

    /**
     * Gets the story related to the room/puzzle.
     * @return the story.
     */
    protected String getStory()
    {
        return null;
    }

    /**
     * Adjusts the difficulty level.
     * @param level the new difficulty level.
     */
    public void adjustDifficulty(Difficulty level)
    {

    }

}
