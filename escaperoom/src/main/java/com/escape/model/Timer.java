package com.escape.model;

/**
 * Handles the countdown timer for the game.
 * Tracks how much time the player has to finish a room.
 * Supports starting, pausing, resuming, and reducing time.
 * Also checks whether time has expired.
 * 
 * @author Dylan Diaz
 */
public class Timer {

    /**
     * The number of seconds the timer started with.
     */
    private int initialSeconds;

    /**
     * The number of seconds still left on the timer.
     */
    private int remainingSeconds;

    private boolean isRunning;

    /**
     * Constructs a Timer with the specified starting time.
     * 
     * @param initialSeconds the total time in seconds to start with
     */
    public Timer(int initialSeconds) {
        this.initialSeconds = initialSeconds;
        this.remainingSeconds = initialSeconds;
        this.isRunning = false;
    }

    /**
     * Starts the countdown by resetting remaining time.
     */
    public void start() {
        remainingSeconds = initialSeconds;
        isRunning = true;
    }

    /**
     * Pauses the countdown.
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * Resumes the countdown after being paused.
     */
    public void resume() {
        isRunning = true;
    }

    /**
     * Reduces the remaining time by a specified number of seconds.
     * 
     * @param seconds the number of seconds to subtract
     */
    public void reduceBy(int seconds) {
        if (isRunning) {
            remainingSeconds -= seconds;
            if (remainingSeconds < 0) {
                remainingSeconds = 0;
            }
        }
    }

    /**
     * Checks if the timer has reached zero.
     * 
     * @return true if time is up, false otherwise
     */
    public boolean isExpired() {
        return remainingSeconds <= 0;
    }

    /**
     * Returns the number of seconds remaining on the timer.
     * 
     * @return the remaining time in seconds
     */
    public int getRemainingSeconds() {
        return remainingSeconds;
    }
}