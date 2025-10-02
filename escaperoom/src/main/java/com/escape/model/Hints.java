package com.escape.model;

/**
 * Creates Hints for the escaperoom depending on the puzzle or room.
 * Each hint will need to be connected to a puzzle and
 * have a description.
 * 
 * @author Jacob Kinard
 */

public class Hints {

    private String hint;
    public int order;
    public String text;
    public boolean revealed;

    /**
     * Constructs a new Hints object with the specified parameters.
     *
     * @param hint     the internal identifier or content of the hint
     * @param order    the sequence order in which the hint should appear
     * @param revealed whether the hint has been revealed to the player
     * @param text     the actual text displayed for the hint
     */

    public Hints(String hint, int order, boolean revealed, String text){
        this.hint = hint;
        this.order = order;
        this.revealed = revealed;
        this.text = text;
    }

    /**
     * Returns the current value of the hint.
     *
     * @return the hint string
     */

    public String getHint(){
        return hint;
    }

    /**
     * Sets a new value for the hint.
     *
     * @param h the new hint string
     * @return the updated hint string
     */

    public String setHint(String h){
        return hint = h;
    }

    /**
     * Returns a string representation of the hint object.
     *
     * @return a placeholder string
     */

    public String toString(){
        return "a string";
    }
}