package com.escape.model;

/**
 * Representation of the data model of a hint.
 * Holds every part of a hint such as the hint identifier, order, whether it's revealed
 * and the actual text displayed for the hint.
 * 
 * @author Jacob Kinard & Talan Kinard
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
        this.hint = h;
        return this.hint;
    }

    /**
     * Gets the order/sequence of this hint.
     * @return the order number
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order of this hint.
     * @param order the order number
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Gets the text content of the hint.
     * @return the hint text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content of the hint.
     * @param text the hint text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Checks if this hint has been revealed to the player.
     * @return true if revealed, false otherwise
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Marks this hint as revealed.
     */
    public void reveal() {
        this.revealed = true;
    }

    /**
     * Sets the revealed status of this hint.
     * @param revealed the revealed status
     */
    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    /**
     * Returns a string representation of the hint object.
     */
    @Override 
    public String toString(){
        return  "Hints: hint id = " +hint+" | order = "+order+
                " | text = "+text+" | revealed = "+revealed;
    }
}