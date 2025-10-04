package com.escape.model;
import java.util.UUID;

/**
 * Child class responsible for managing any and all "word" puzzles.
 * @author Jacob Kinard
 * 
 */
public class WordPuzzle extends Puzzle{
    /**
     *  
     *
     * @param title - The title of the puzzle
     * @param objective - Is the objective of the puzzle and is a description of what the user is trying to solve.
     * @param solved - holds the vlaue if the puzzle is solved or not
     * @param solution - holds the valid solution to the puzzle
     * 
     * 
     */
    public UUID puzzleID;
    private String title;
    private String objective;
    private boolean solved;
    private String solution;

    public WordPuzzle(String title, String objective, String solution, boolean solved){
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
