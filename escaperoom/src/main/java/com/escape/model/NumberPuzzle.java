package com.escape.model;
import java.util.UUID;

/**
 * Child class responsible for managing any and all "number" puzzles.
 * @author Jacob Kinard
 * 
 */
public class NumberPuzzle extends Puzzle{
    /**
     *  
     *
     * @param title - The title of the puzzle
     * @param objective - Is the objective of the puzzle and is a description of what the user is trying to solve.
     * @param solved - holds the vlaue if the puzzle is solved or not
     * @param solution - holds the valid number solution to the puzzle
     * @param puzzleID - holds a uniqe identifier address for puzzle object
     * 
     */
    public UUID puzzleID;
    private String title;
    private String objective;
    private boolean solved;
    private int solution;

    public NumberPuzzle(String title, String objective, int solution, boolean solved){
        this.title = title;
        this.objective = objective;
        this.puzzleID = puzzleID;
        this.solved = solved;
        this.solution = solution;
    }

    /**
     *  
     *@return returns a vlaue if the puzzle is solved.
     */
    public boolean isSolved(){
        return true;

    }
    
}
