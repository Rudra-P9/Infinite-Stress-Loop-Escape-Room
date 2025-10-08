package com.escape.model;

import java.util.ArrayList;

/**
 * Class for representation of the game leaderboard.
 * Stores user entries and contains methods for retrieving.
 * 
 * @author Talan Kinard
 */

public class Leaderboard
{
    /**
     * List of users currently on the leaderboard.
     */
    public ArrayList<User> entries;

    /**
     * 
     * @param d the difficulty level.
     * @return a list of scores in relation with the difficulty level.
     */
    public ArrayList<Score> topByDifficulty(Difficulty d)
    {
        return null;
    }

    /**
     * Gets leaderboard entries
     * @return current leaderboard entries
     */
    public ArrayList<User> getLB()
    {
        return null;
    }

    /**
     * Sets the leaderboard entries
     * @param entries a list of users to the leaderboard.
     */
    public void setLB(ArrayList<User> entries)
    {

    }

    /**
     * Prints the leaderboard.
     */
    public void printLB()
    {
        System.out.println("Printing LeaderBoard..."); //hardcode stub for now
    }
}
