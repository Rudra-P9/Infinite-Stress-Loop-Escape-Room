package com.escape.model;

import java.util.Date;

/**
 * Represents a player's score for a completed escape room.
 * Used by SavedData and LeaderBoard.
 * Interacts with GameDataWriter and GameDataLoader for persistence.
 * 
 * @author Dylan Diaz
 */
public class Score {

    private String username;
    private Difficulty difficulty;
    private int timeLeftSec;
    private Date date;
    private int scoreValue;

    /**
     * Constructs a Score with all required fields.
     * 
     * @param username the player's username
     * @param difficulty the difficulty level of the room
     * @param timeLeftSec seconds remaining when the room was completed
     * @param date the date the score was recorded
     */
    public Score(String username, Difficulty difficulty, int timeLeftSec, Date date) {
        this.username = username;
        this.difficulty = difficulty;
        this.timeLeftSec = timeLeftSec;
        this.date = date;
        this.scoreValue = 0; // default until set
    }

    /**
     * Returns the score value.
     * 
     * @return the score value
     */
    public int getScore() {
        return scoreValue;
    }

    /**
     * Sets the score value.
     * 
     * @param scoreValue the score to assign
     */
    public void setScore(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    // Getters for associated classes to use
    public String getUsername() {
        return username;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getTimeLeftSec() {
        return timeLeftSec;
    }

    public Date getDate() {
        return date;
    }
}