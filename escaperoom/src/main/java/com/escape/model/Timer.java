package com.escape.model;

/**
 * Handles the countdown timer for the game.
 * Tracks how much time the player has to finish a room.
 * Supports starting, pausing, resuming, and reducing time.
 *
 * Uses a monotonic clock and an explicit accumulated-elapsed strategy
 * so pause/resume arithmetic is simpler and deterministic.
 */
public class Timer {
    private final int initialSeconds;

    // monotonic nanosecond times
    private long startedAtNs = 0L; // when counting began (first start)
    private long pausedAtNs = 0L; // when last paused (if paused)
    private long accumulatedElapsedNs = 0L; // total elapsed time captured during pauses
    private boolean running = false;

    public Timer(int initialSeconds) {
        this.initialSeconds = Math.max(0, initialSeconds);
    }

    // Start or resume the timer. Safe to call multiple times.
    public synchronized void start() {
        long now = System.nanoTime();
        if (startedAtNs == 0L) {
            // first start: record base start time
            startedAtNs = now;
            running = true;
            pausedAtNs = 0L;
            accumulatedElapsedNs = 0L;
            return;
        }
        if (!running) {
            // resume: we already saved elapsed into accumulatedElapsedNs at pause,
            // so just flip running on and clear pausedAt.
            pausedAtNs = 0L;
            running = true;
            startedAtNs = now;
        }
        // if already running, do nothing
    }

    // Pause countdown (does not reset).
    public synchronized void pause() {
        if (!running)
            return;
        // snapshot elapsed since startedAt into accumulatedElapsed and mark paused
        long now = System.nanoTime();
        accumulatedElapsedNs += now - startedAtNs;
        pausedAtNs = now;
        running = false;
    }

    // Alias to start() for readability
    public synchronized void resume() {
        start();
    }

    // Seconds left, computed from monotonic clock (never negative)
    public synchronized int getRemainingSeconds() {
        if (startedAtNs == 0L)
            return initialSeconds; // not started yet

        long now = System.nanoTime();
        long elapsedNs;

        if (running) {
            // elapsed = accumulated from earlier pauses + time since initial start (or last
            // resumed)
            elapsedNs = accumulatedElapsedNs + (now - startedAtNs);
        } else {
            // when paused we already included the last running interval into
            // accumulatedElapsedNs,
            // so elapsed is exactly accumulatedElapsedNs
            elapsedNs = accumulatedElapsedNs;
        }

        if (elapsedNs < 0)
            elapsedNs = 0L; // defensive

        long elapsedSeconds = elapsedNs / 1_000_000_000L;
        long remaining = (long) initialSeconds - elapsedSeconds;
        return (int) Math.max(0L, remaining);
    }

    // for certificate / debugging.
    public int getInitialSeconds() {
        return initialSeconds;
    }

    // helper useful in tests / external code
    public synchronized boolean isRunning() {
        return running;
    }

    /**
     * Sets the remaining time directly (used when restoring from save).
     * Calculates the elapsed time and updates internal state accordingly.
     * @param seconds the number of seconds remaining
     */
    public synchronized void setRemainingSeconds(long seconds) {
        if (seconds < 0) seconds = 0;
        if (seconds > initialSeconds) seconds = initialSeconds;
        
        // Calculate elapsed time from remaining time
        long elapsedSeconds = initialSeconds - seconds;
        this.accumulatedElapsedNs = elapsedSeconds * 1_000_000_000L;
        
        // Reset the start time if running
        if (running) {
            this.startedAtNs = System.nanoTime();
        }
    }

    public synchronized void reduceTime(int seconds) {
        if (seconds <= 0)
            return;
        accumulatedElapsedNs += (long) seconds * 1_000_000_000L;
    }
}