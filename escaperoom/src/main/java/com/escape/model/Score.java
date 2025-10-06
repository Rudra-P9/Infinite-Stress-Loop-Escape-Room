package com.escape.model;

import java.util.Date;

/**
 * Class for representation of the game leaderboard.
 * Stores user entries and contains methods for retrieving.
 * 
 * @author Rudra Patel
 */
public class Score {
    private String username;
    private Difficulty difficulty;
    private int timeLeftSec;
    private Date date;
    private int score;

    public Score() {}

    public Score(String username, Difficulty difficulty, int timeLeftSec, int score) {
        this.username = username;
        this.difficulty = difficulty;
        this.timeLeftSec = timeLeftSec;
        this.score = score;
        this.date = new Date();
    }

    // getters / setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public int getTimeLeftSec() { return timeLeftSec; }
    public void setTimeLeftSec(int timeLeftSec) { this.timeLeftSec = timeLeftSec; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
