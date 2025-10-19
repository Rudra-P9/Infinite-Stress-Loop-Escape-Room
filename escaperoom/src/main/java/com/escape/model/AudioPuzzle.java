package com.escape.model;

/**
 * Child class of puzzle for managing audio-based puzzles.
 * 
 * @author Talan Kinard
 */
public class AudioPuzzle extends Puzzle {

    /**
     * Contructs audiopuzzle instance.
     * @param puzzleID unique ID for the puzzle
     * @param title title of puzzle displayed 
     * @param objective obj of the puzzle displayed
     * @param solution correct answer to the puzzle
     * @param category category of puzzle
     * @param type the distinct type of puzzle within the category
     */
    public AudioPuzzle (String puzzleID, String title, String objective, String solution,
                     String category, String type) {
                        super(puzzleID, title, objective, solution, category, type);
                     }
    
}
