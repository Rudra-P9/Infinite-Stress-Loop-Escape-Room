package com.escape.model;

/**
 * Child class responsible for managing any and all "number" puzzles.
 * @author Jacob Kinard & Talan Kinard
 * 
 */
public class NumberPuzzle extends Puzzle {

     /**
     * Contructs numberpuzzle instance.
     * @param puzzleID unique ID for the puzzle
     * @param title title of puzzle displayed 
     * @param objective obj of the puzzle displayed
     * @param solution correct answer to the puzzle
     * @param category category of puzzle
     * @param type the distinct type of puzzle within the category
     */
    public NumberPuzzle (String puzzleID, String title, String objective, String solution,
                     String category, String type) {
                        super(puzzleID, title, objective, solution, category, type);
                     }
    
    /**
     * Checks player answer versus correct solution.
     */
    @Override
    public boolean checkAnswer(String answer) {
        if(answer == null || solution == null) {
            return false;
        }

        try {
            double playerInput = Double.parseDouble(answer.trim());
            double correctInput = Double.parseDouble(solution.trim());
            return Math.abs(playerInput - correctInput) < 0.0001; //parsing succeeds and returns
        } catch (NumberFormatException e) {
            System.out.println("Number is invalid for puzzle type: "+type);
            return false;
        }
    }

    @Override
    public String getSolution() {
        return solution;
    }

    @Override
    public void setSolution(String solution) {
        this.solution = solution;
    }

}
