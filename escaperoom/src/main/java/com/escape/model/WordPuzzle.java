package com.escape.model;

/**
 * Child class responsible for managing any and all "word" puzzles.
 * Handles riddle, decipher, arrow, and the final puzzles.
 * @author Jacob Kinard & Talan Kinard
 */
public class WordPuzzle extends Puzzle{
   
    /**
     * Contructs wordpuzzle instance.
     * @param puzzleID unique ID for the puzzle
     * @param title title of puzzle displayed 
     * @param objective obj of the puzzle displayed
     * @param solution correct answer to the puzzle
     * @param category category of puzzle
     * @param type the distinct type of puzzle within the category
     */
    public WordPuzzle (String puzzleID, String title, String objective, String solution,
                     String category, String type) {
                        super(puzzleID, title, objective, solution, category, type);
                     }
    
    /**
     * Player input compared to expected solution.
     * Input/Solution both trimmed and uppercased and no spaces for easy input checks.
     */
    @Override
    public boolean checkAnswer(String answer) {
        if(answer == null || solution == null) {
            return false;
        }

        String fixedAnswer = fix(answer);
        String fixedSolution = fix(solution);

        switch(type.toUpperCase()) {
            case "RIDDLE":
            case "LETTER_DECIPHER":
            case "ARROW_DECIPHER":
            case "FINAL_LOCK":
                return fixedAnswer.equals(fixedSolution);
            default:
                System.out.println("Error in puzzle type.");
                return false;

        }
    }

    /**
     * Gets the correct solution.
     */
    @Override
    public String getSolution() {
        return solution;
    }

    /**
     * Sets the correct solution.
     */
    @Override
    public void setSolution(String solution) {
        this.solution = solution;
    }

    /**
     * Method to fix the inputs, example trim, uppercase, and remove spaces.
     * @param input
     * @return a fixed version of the string.
     */
    private String fix(String input) {
        return input.trim().toUpperCase().replaceAll("\\s+","");
    }
    
}
