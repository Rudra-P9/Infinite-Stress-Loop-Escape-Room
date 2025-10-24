package com.escape.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Class that represents the leaderboard for the Escape Room.
 * Stores the list of users and their rankings.
 * 
 * @author Talan Kinard
 * @author Dylan Diaz
 * @author Rudra Patel
 * @version 1.2
 */
public class Leaderboard {

    private final ArrayList<Score> scores = new ArrayList<>();

    public Leaderboard() { }

    public ArrayList<Score> getScores() {
        return new ArrayList<>(scores);
    }

    public void setScores(List<Score> scoreList) {
        scores.clear();
        if (scoreList != null) scores.addAll(scoreList);
        sortByScoreDescending();
    }

    /**
     * Adds a score to the leaderboard and sorts the leaderboard in descending order by score value.
     *
     * @param score the score to be added
     */
    public void addScore(Score score) {
        scores.add(score);
        sortByScoreDescending();
    }

    /**
     * Gets top N scores.
     */
    public ArrayList<Score> topN(int n) {
        if (n <= 0) return new ArrayList<>();
        int max = Math.min(n, scores.size());
        ArrayList<Score> out = new ArrayList<>();
        for (int i = 0; i < max; i++) out.add(scores.get(i));
        return out;
    }

    public void addOrReplace(Score newScore) {
        if (newScore == null) return;

        String newUsername = safeGetUsername(newScore);
        if (newUsername == null || newUsername.isEmpty()) {
            // no username to match — just append
            scores.add(newScore);
            sortByScoreDescending();
            return;
        }
        
        boolean replaced = false;
        for (int i = 0; i < scores.size(); i++) {
            Score existing = scores.get(i);
            String existingUsername = safeGetUsername(existing);                    
            if (existingUsername != null && existingUsername.equals(newUsername)) {
            scores.set(i, newScore);
            replaced = true;
            break;
            }
        }
        if (!replaced) scores.add(newScore);
            sortByScoreDescending();
        }
        
    private static String safeGetUsername(Score s) {
        if (s == null) return null;
        try {
            Method m = s.getClass().getMethod("getUsername");
            Object v = m.invoke(s);
            return v == null ? null : v.toString();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException t) {
            return null;
        }
    }
        
    public boolean removeByUserID(UUID userID) throws IllegalArgumentException, IllegalAccessException {
        if (userID == null) return false;
        String idStr = userID.toString();
        Iterator<Score> it = scores.iterator();
        while (it.hasNext()) {
            Score s = it.next();
            String found = tryExtractIdString(s);
            if (found != null && found.equals(idStr)) {
                it.remove();
                sortByScoreDescending();
                return true;
            }
        }
        return false;
    }

    public boolean removeByUsername(String username) {
        if (username == null) return false;
        Iterator<Score> it = scores.iterator();
        while (it.hasNext()) {
            Score s = it.next();
            String uname = safeGetUsername(s);
            if (username.equals(uname)) {
                it.remove();
                sortByScoreDescending();
                return true;
            }
        }
        return false;
    }

    public void clear() {
        scores.clear();
    }

    public int size() {
        return scores.size();
    }

    @Override
    public String toString() {
        return "Leaderboard{entries=" + scores.size() + "}";
    }

    /** Helper method: sorts leaderboard so highest scores come first. */
    private void sortByScoreDescending() {
        scores.sort((a, b) -> Long.compare(b.getScore(), a.getScore()));
    }

    /**
     * Displays the leaderboard in a formatted table.
     */
    public void display() {
        System.out.println("\n" + "=".repeat(75));
        System.out.println("                         LEADERBOARD");
        System.out.println("=".repeat(75));
        System.out.printf("%-5s %-20s %-12s %-15s %-10s%n", 
            "Rank", "Username", "Difficulty", "Time Left", "Score");
        System.out.println("-".repeat(75));

        if (scores.isEmpty()) {
            System.out.println("No entries yet. Be the first to complete the game!");
        } else {
            int rank = 1;
            for (Score s : scores) {
                System.out.printf("%-5d %-20s %-12s %-15s %-10d%n",
                    rank++,
                    truncate(s.getUsername(), 20),
                    s.getDifficulty(),
                    formatTime(s.getTimeLeftSec()),
                    s.getScore());
            }
        }
        System.out.println("=".repeat(75));
    }

    /**
     * Formats seconds into MM:SS.
     */
    private String formatTime(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }

    private String truncate(String str, int maxLen) {
        if (str == null) return "";
        if (str.length() <= maxLen) return str;
        return str.substring(0, Math.max(0,maxLen - 3)) + "...";
    }

    /**
     * Try to extract an ID from Score (UUID or string) and return it as a string.
     * Returns null if not found.
          * @throws IllegalAccessException 
          * @throws IllegalArgumentException 
          */
         private static String tryExtractIdString(Score s) throws IllegalArgumentException, IllegalAccessException {
        if (s == null) return null;
        String[] methodNames = {"getUserID", "getUserId", "getUser"};
        for (String mn : methodNames) {
            try {
                Method m = s.getClass().getMethod(mn);
                Object v = m.invoke(s);
                if (v != null) return v.toString();
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}
        }
        String[] fieldNames = {"userID", "userId", "id"};
        for (String fn : fieldNames) {
            try {
                Field f = s.getClass().getDeclaredField(fn);
                f.setAccessible(true);
                Object v = f.get(s);
                if (v != null) return v.toString();
            } catch (NoSuchFieldException ignored) {}
        }
        return null;
    }




    // Testing the Leaderboard
    public static void main(String[] args) {
        Leaderboard lb = new Leaderboard();

        // Create sample Score objects using your Score constructor
        // (username, difficulty, timeLeftSec, date, scoreValue)
        Score s1 = new Score("Alice", Difficulty.EASY, 250, new java.util.Date(), Score.calculateScore(250, Difficulty.EASY));
        Score s2 = new Score("Bob", Difficulty.HARD, 30, new java.util.Date(), Score.calculateScore(30, Difficulty.HARD));
        Score s3 = new Score("Charlie", Difficulty.MEDIUM, 60, new java.util.Date(), Score.calculateScore(60, Difficulty.MEDIUM));

        // Add them to the leaderboard
        lb.addScore(s1);
        lb.addScore(s2);
        lb.addScore(s3);

        System.out.println("=== Leaderboard (expected: Bob, Charlie, Alice) ===");
        lb.display();

        // Update Alice’s score and test resorting:
        // modify scoreValue via setScore and call addOrReplace (matching by username)
        s1.setScore(1000);
        lb.addOrReplace(s1);

        System.out.println("\n=== After updating Alice (expected: Alice, Bob, Charlie) ===");
        lb.display();
    }

}
