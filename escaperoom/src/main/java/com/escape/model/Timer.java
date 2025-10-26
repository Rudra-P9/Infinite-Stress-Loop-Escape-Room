package com.escape.model;

/**
 * Handles the countdown timer for the game.
 * Tracks how much time the player has to finish a room.
 * Supports starting, pausing, resuming, and reducing time.
 * Also checks whether time has expired.
 * 
 * @author Dylan Diaz
 * @author Kirtan Patel
 */
public class Timer {
    private final int initialSeconds;

    private long startedAtMs = 0L;        // when the game first started
    private long pausedAtMs  = 0L;        // last time we paused
    private long pausedTotalMs = 0L;      // total paused time accumulated
    private boolean running = false;

    public Timer(int initialSeconds) {
        this.initialSeconds = Math.max(0, initialSeconds);
    }

    // Start or resume the timer. Safe to call multiple times. 
    public synchronized void start() {
        if (startedAtMs == 0L) {
            startedAtMs = System.currentTimeMillis();
            running = true;
            return;
        }
        if (!running) { // resume after pause
            pausedTotalMs += System.currentTimeMillis() - pausedAtMs;
            running = true;
        }
    }

    // Pause countdown (does not reset).
    public synchronized void pause() {
        if (!running) return;
        pausedAtMs = System.currentTimeMillis();
        running = false;
    }

    // Alias to start() for readability
    public synchronized void resume() { start(); }

    // Seconds left, computed from wall clock (never negative)
    public synchronized int getRemainingSeconds() {
        if (startedAtMs == 0L) return initialSeconds; // not started yet

        long now = System.currentTimeMillis();
        long effectiveElapsedMs;

        if (running) {
            effectiveElapsedMs = now - startedAtMs - pausedTotalMs;
        } else {
            effectiveElapsedMs = pausedAtMs - startedAtMs - pausedTotalMs;
        }

        long remaining = (long) initialSeconds - (effectiveElapsedMs / 1000L);
        return (int) Math.max(0L, remaining);
    }

    //for certificate / debugging. 
    public int getInitialSeconds() { return initialSeconds; }
}
