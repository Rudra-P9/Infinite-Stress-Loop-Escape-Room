package com.escape.model;

import java.util.List;
/**
 * Rooms of the EscapeRoom
 * Each room has an ID, puzzles, and a difficulty level.
 * 
 * @author Talan Kinard
 */

public class Rooms {

    /**
     * Unique identifier for the room.
     */
    private String roomID;
    private String title;
    private List<Puzzle> puzzles;

    /**
     * Default constructor
     */
    public Rooms() {

    }

    /**
     * Constructs a room with id, title, and puzzles
     * @param roomID the id for the room
     * @param title name of the room
     * @param puzzles the puzzles the room contains
     */
    public Rooms(String roomID, String title, List<Puzzle> puzzles) {
        this.roomID = roomID;
        this.title = title;
        this.puzzles = puzzles;
    }

    /**
     * Returns the list of puzzles associated with the room.
     * @return puzzles
     */
    public List<Puzzle> getPuzzles()
    {
        return puzzles;
    }

    /**
     * Sets the list of puzzles for this room.
     * @param puzzles
     */
    public void setPuzzles(List<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }

    /**
     * Returns the id of the room.
     * @return
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * Sets the identifier for this room.
     * @param roomID
     */
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    /**
     * Returns the title of the room.
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the room.
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Provides a String description for the room and avoids a NullPointer with an incorrect load.
     */
    @Override
    public String toString() {
        return "Room: "+title+" ("+roomID+"), Puzzles: "+(puzzles != null ? puzzles.size() : 0);
    }

}
