package com.escape.model;
/**
 * Handles the countdown timer for the game.
 *
 * Keeps track of how much time the player has to finish a room.
 * Can start, pause, resume, or reduce time as the game runs.
 * Checks when time has fully run out.
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

    /**
     * Starts the countdown.
     */
    public void start() {
        // stub method
    }

    /**
     * Pauses the countdown.
     */
    public void pause() {
        // stub method
    }

    /**
     * Resumes the countdown after being paused.
     */
    public void resume() {
        // stub method
    }

    /**
     * Reduces the remaining time by a certain number of seconds.
     *
     * @param seconds number of seconds to subtract
     */
    public void reduceBy(int seconds) {
        // stub method
    }

    /**
     * Checks if the timer has reached zero.
     *
     * @return true if time is up, false otherwise
     */
    public boolean isExpired() {
        // stub method
        return false;
    }
}
