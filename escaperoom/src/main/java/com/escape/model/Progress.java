package com.escape.model;

import java.util.UUID;

/**
 * Represents a user's saved or ongoing progress in the game.
 * @author Dylan Diaz & Talan Kinard & Kirtan Patel
 * Version 2
 */
public class Progress {
    private UUID progressUUID;
    private UUID userUUID;

    // 0 -> before puzzle 1, 1..5 -> after solving those puzzles, 6 -> finished
    private int storyPos;
    private static final int TOTAL_BEATS = 6;

    private int questionsAnswered;
    private int hintsUsed;
    private java.util.List<String> hintedPuzzles = new java.util.ArrayList<>();

    public Progress(UUID progressUUID, UUID userUUID) {
        if (progressUUID == null) progressUUID = UUID.randomUUID();
        if (userUUID == null)     userUUID     = UUID.randomUUID();
        this.progressUUID = progressUUID;
        this.userUUID = userUUID;
        this.storyPos = 0;
        this.hintsUsed = 0;
        this.questionsAnswered = 0;
    }

    public UUID getProgressUUID() { return progressUUID; }
    public void setProgressUUID(UUID progressUUID) { this.progressUUID = progressUUID; }

    public UUID getUserUUID() { return userUUID; }
    public void setUserUUID(UUID userUUID) { this.userUUID = userUUID; }

    public int getStoryPos() { return storyPos; }

    /**
     * Clamp story position into 0..TOTAL_BEATS
     */
    public void setStoryPos(int pos) {
        if (pos < 0) pos = 0;
        if (pos > TOTAL_BEATS) pos = TOTAL_BEATS;
        storyPos = pos;
    }

    /**
     * Advance one beat; also increment questionsAnswered.
     */
    public void advanceStory() {
        questionsAnswered++;
        if (storyPos < TOTAL_BEATS) {
            storyPos++;
        }
    }

    public boolean isComplete() { return storyPos >= TOTAL_BEATS; }

    /**
     * Completion percent: storyPos out of TOTAL_BEATS.
     */
    public double getCompletionPercent() {
        return storyPos / (double) TOTAL_BEATS * 100.0;
    }

    

    /** True count of solved puzzles. */
    public int getQuestionsAnswered() {
         return questionsAnswered; 
        }

    /** Setter used when restoring from a save file. */
    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = Math.max(0, questionsAnswered);
    }

    public void useHint() { hintsUsed++; }
    public int getHintsUsed() { return hintsUsed; }
    /** Setter used when restoring from a save file. */
    public void setHintsUsed(int hintsUsed) { this.hintsUsed = Math.max(0, hintsUsed); }

    /**
     * Backward-compat alias: returns questionsAnswered).
     * If other code calls questionsAnswered(), it now does the right thing.
     */
    public int questionsAnswered() { return questionsAnswered(); }
    

        /** Record that a hint was used on a particular puzzle title. */
        public void addHintFor(String puzzleTitle) {
            if (puzzleTitle != null && !puzzleTitle.isBlank() && !hintedPuzzles.contains(puzzleTitle)) {
                hintedPuzzles.add(puzzleTitle);
            }
            hintsUsed++; // keep your counter consistent
        }

        /** Titles of puzzles where a hint was used (persisted). */
        public java.util.List<String> getHintedPuzzles() {
            return new java.util.ArrayList<>(hintedPuzzles);
        }

        /** Used by loader to restore hinted puzzles list. */
        public void setHintedPuzzles(java.util.List<String> titles) {
            this.hintedPuzzles = (titles == null) ? new java.util.ArrayList<>() : new java.util.ArrayList<>(titles);
        }


/**
 * Returns a string representation of the Progress object.
 * Includes the completion percentage, number of hints used, and number of puzzles solved.
 * @return a string representation of the Progress object
 */
    @Override
    public String toString() {
        return "Progress: " + getCompletionPercent() + "% of the Escape Room completed"
             + "\nHints Used: " + getHintsUsed()
             + "\nPuzzles Solved: " + getQuestionsAnswered();
    }

    
    
    
}
