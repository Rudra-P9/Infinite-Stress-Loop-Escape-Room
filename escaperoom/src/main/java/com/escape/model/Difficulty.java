package com.escape.model;

/**
 * Enumeration for the difficulty levels of this Escape Room.
 * Difficulty is used to control the time that the player will have to complete the Escape Room.
 * 
 * @author Talan Kinard
 */
public enum Difficulty 
{
    EASY(1800),
    MEDIUM(1200),
    HARD(600);

    private final int timeLimitSec;
    Difficulty(int seconds) {
        this.timeLimitSec = seconds;
    }

    /**
     * Returns the time limit for the given difficulty level in seconds.
     */
    public int getTimeLimitSec() {
        return timeLimitSec;
    }
    
}
