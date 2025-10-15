package com.escape.model;

import java.util.UUID;

/**
 * Represents a user's saved or ongoing progress in the game.
 * @author Dylan Diaz & Talan Kinard
 */
public class Progress {

    /**
     * Identifier for this progress record.
     * Used to distinguish one saved progress instance from another.
     */
    private UUID progressUUID;

    /**
     * Identifier of the user associated with this progress.
     * Links this progress record to a specific user profile.
     */
    private UUID userUUID;

    /**
     * These will be how we determine progress throughout the story.
     * Each puzzle will be worth 20% of the total progress.
     * Story pos 0-5, 5 meaning you've completed the puzzle, upon completion
     * of a puzzle the value of story pos will go up +1.
     */
    private int storyPos;
    private static final int TOTAL_BEATS = 5;



    /**
     * Constructs a new Progress object with the specified identifiers.
     *
     * @param progressUUID the identifier for this progress record
     * @param userUUID the identifier of the user associated with this progress
     */
    public Progress(UUID progressUUID, UUID userUUID) 
    {
        /*
         * If UUID is null gives random UUID
         */
        if(progressUUID == null){
            System.out.println("progressUUID null, giving random UUID.");
            progressUUID = UUID.randomUUID();
        }
        if(userUUID == null){
            System.out.println("userUUID null, giving random UUID.");
            userUUID = UUID.randomUUID();
        }
        this.progressUUID = progressUUID;
        this.userUUID = userUUID;
        /**
         * Starting position holds a value of zero.
         */
        this.storyPos = 0; 
    }

    /**
     * Returns the unique identifier for this progress record.
     *
     * @return the progress record's unique identifier
     */
    public UUID getProgressUUID() {
        return progressUUID;
    }

    /**
     * Sets the unique identifier for this progress record.
     *
     * @param progressUUID the new unique identifier for this progress
     */
    public void setProgressUUID(UUID progressUUID) {
        this.progressUUID = progressUUID;
    }

    /**
     * Returns the unique identifier of the user associated with this progress.
     *
     * @return the user's unique identifier
     */
    public UUID getUserUUID() {
        return userUUID;
    }

    /**
     * Sets the unique identifier of the user associated with this progress.
     *
     * @param userUUID the new unique identifier for the user
     */
    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public int getStoryPos() {
        return storyPos;
    }

    /**
     * Sets story position
     * 0 -> PUZZLE 1
     * 1 -> PUZZLE 2
     * 2 -> PUZZLE 3
     * 3 -> PUZZLE 4
     * 4 -> PUZZLE 5
     * 5 -> COMPLETION
     * @param pos
     */

    public void setStoryPos(int pos) {

        if(pos<0) {
            pos = 0;
        }

        if(pos > TOTAL_BEATS) {
            pos = TOTAL_BEATS;
        }

        storyPos = pos;
    }

    /**
     * Method moves story position value +1
     * to advance progress.
     */
    public void advanceStory()
    {
        if(storyPos < TOTAL_BEATS) {
            storyPos++;
        }
    }

    /**
     * Represents story completion if the story Pos reaches 5.
     * @return
     */
    public boolean isComplete() {
        return storyPos >= TOTAL_BEATS;
    }

    /**
     * @return a completion percentage 0-100.
     * Each puzzle will be worth 20% of the 
     * overall Escape Room.
     */
    public double getCompletionPercent() {
        return storyPos / (double) TOTAL_BEATS * 100;
    }

    @Override
    public String toString() {
        return "Progress: "+getCompletionPercent()+"% of the Escape Room completed";
    }
    
    
    
}
