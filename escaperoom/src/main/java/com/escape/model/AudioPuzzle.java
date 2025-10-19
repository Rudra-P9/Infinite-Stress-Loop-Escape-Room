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

    /**
     * Check's player input versus expected solution.
     */
    @Override
    public boolean checkAnswer(String answer) {
        if(answer == null || solution == null) {
            return false;
        }

        String fixedAnswer = fix(answer);
        String fixedSolution = fix(solution);
        return fixedAnswer.equals(fixedSolution);
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    /**
     * Placeholder for audio.
     */
    private void playAudio() {
        System.out.println("TODO ADD AUDIO");
    }

    /**
     * Fixes formatting on Strings to accept any input.
     * @param input of a String to fix
     * @return a fixed version of the String
     */
    private String fix(String input) {
        return input.trim().toUpperCase().replaceAll("\\s+","");
    }
    
}
