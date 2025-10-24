package com.escape.model;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a player's score for a completed escape room.
 * Used by SavedData and Leaderboard.
 * Interacts with GameDataWriter and GameDataLoader for persistence.
 * 
 * @author Dylan Diaz
 * @author Rudra Patel
 * @version 1.1
 */

public class Score {

    private String username;
    private Difficulty difficulty;
    private long timeLeftSec;
    private Date date;
    private long scoreValue;
    
    /** Required for GameDataLoader reflection. */
    public Score() { }

    /**
     * Constructs a Score with all required fields.
     * @param username the player's username
     * @param difficulty the difficulty level of the room
     * @param timeLeftSec seconds remaining when the room was completed
     * @param date the date the score was recorded
     * @param scoreValue the score value
     */
    public Score(String username, Difficulty difficulty, long timeLeftSec, Date date, long scoreValue) {
        this.username = username;
        this.difficulty = difficulty;
        this.timeLeftSec = timeLeftSec;
        this.date = new Date();
        this.scoreValue = scoreValue; // default until set
    }

    // Getters
    public String getUsername() {
        return username;
    }
    
    /**
     * Returns the difficulty level of the room for which the score was recorded.
     * @return the difficulty level of the room
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the number of seconds remaining when the room was completed.
     * @return the number of seconds remaining when the room was completed
     */
    public long getTimeLeftSec() {
        return timeLeftSec;
    }

    /**
     * Returns the date when the score was recorded.
     * @return the date when the score was recorded
     */  
    public Date getDate() {
        return date;
    }

    /**
     * Returns the score value of the score object.
     * The score value is calculated based on the time left and difficulty.
     * @return the score value of the score object
     */
    public long getScore() {
        return scoreValue;
    }

    // Setters (needed by GameDataLoader)
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the difficulty level of the room for which the score was recorded.
     * If the provided difficulty string is null or not a valid Difficulty, the default
     * difficulty (EASY) will be used.
     * 
     * @param difficulty the difficulty level of the room
     */
    public void setDifficulty(String difficulty) {
        if (difficulty != null) {
            try {
                this.difficulty = Difficulty.valueOf(difficulty.toUpperCase());
            } catch (IllegalArgumentException e) {
                this.difficulty = Difficulty.EASY; // default
            }
        }
    }

    /**
     * Sets the difficulty level of the room for which the score was recorded.
     * This method is used by GameDataLoader to set the difficulty level of the score.
     * 
     * @param difficulty the difficulty level of the room
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Sets the number of seconds remaining when the room was completed.
     * This time is used to calculate the score value of the score object.
     * 
     * @param timeLeftSec the number of seconds remaining when the room was completed
     */
    public void setTimeLeftSec(long timeLeftSec) {
        this.timeLeftSec = timeLeftSec;
    }

    /**
     * Sets the number of seconds remaining when the room was completed.
     * This time is used to calculate the score value of the score object.
     * This method is an alias for setTimeLeftSec(long) and is used for compatibility with the GameDataLoader.
     * 
     * @param timeSeconds the number of seconds remaining when the room was completed
     */
    public void setTimeSeconds(long timeSeconds) {
        this.timeLeftSec = timeSeconds; // alias for loader compatibility
    }

    /**
     * Sets the date of the score object to the current date.
     * Currently, parsing of dateStr is not implemented and will be ignored.
     * 
     * @param dateStr the date string to be parsed (not implemented)
     */
    public void setDate(String dateStr) {
        // For now, just set current date. Can parse dateStr if needed.
        this.date = new Date();
    }

    /**
     * Sets the date of the score object to the given date.
     * 
     * @param date the date to be set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the score value of the score object.
     * This score value is used to track the user's progress.
     * The score value is calculated based on the time left and difficulty.
     * The score value is used to compare the user's progress with other users.
     * 
     * @param score the score value to be set
     */
    public void setScore(long score) {
        this.scoreValue = score;
    }

    /**
     * Calculates score based on time left and difficulty.
     * Formula: timeLeft * multiplier
     * EASY: 1.0x, MEDIUM: 1.5x, HARD: 2.0x
     */
    public static long calculateScore(long timeLeftSec, Difficulty difficulty) {
        double multiplier;
        switch (difficulty) {
            case EASY:
                multiplier = 1.0;
                break;
            case MEDIUM:
                multiplier = 1.5;
                break;
            case HARD:
                multiplier = 2.0;
                break;
            default:
                multiplier = 1.0;
        }
        return (long) (timeLeftSec * multiplier);
    }

    /**
     * Returns a string representation of the score object.
     * The string representation includes the username, difficulty, time left, score, and date.
     * 
     * @return a string representation of the score object
     */
    @Override
    public String toString() {
        return "Score{" +
                "username='" + username + '\'' +
                ", difficulty=" + difficulty +
                ", timeLeft=" + timeLeftSec + "s" +
                ", score=" + scoreValue +
                ", date=" + date +
                '}';
    }

    public UUID getUserId() {
        throw new UnsupportedOperationException("Unimplemented method 'getUserId'");
    }
}