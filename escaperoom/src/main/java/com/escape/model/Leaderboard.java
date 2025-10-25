package com.escape.model;

/**
 * Class that represents the leaderboard for the Escape Room.
 * Stores the list of entries (Score objects) and maintains them sorted
 * from highest score to lowest score.
 * 
 * @author Talan Kinard
 * @author Dylan Diaz
 */
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Leaderboard {

    // Primary storage: Score objects (best → worst)
    private final ArrayList<Score> entries = new ArrayList<>();

    public Leaderboard() { }

    /**
     * Return a copy of the entries (Score objects), ordered best → worst.
     */
    public ArrayList<Score> getLB() {
        return new ArrayList<>(entries);
    }

    /**
     * Compatibility: accept a List of Score objects and replace internal entries.
     * GameDataLoader uses reflection and may call setLB with a List&lt;Score&gt;.
     */
    public void setLB(List<?> scores) {
        entries.clear();
        if (scores == null) return;
        for (Object o : scores) {
            if (o instanceof Score) entries.add((Score) o);
            else {
                // If loader passed a List of Maps or raw JSON objects, try best-effort mapping:
                // ignore other types for now.
            }
        }
        sortByScoreDescending();
    }

    /**
     * New bulk setter using a typed list.
     */
    public void setEntries(List<Score> scores) {
        entries.clear();
        if (scores != null) entries.addAll(scores);
        sortByScoreDescending();
    }

    /**
     * Add a Score to the leaderboard, or replace an existing entry for the same username.
     * Keeps the list sorted (best → worst).
     */
    public void addOrReplace(Score s) {
        if (s == null) return;
        String uname = safeGetUsername(s);
        boolean replaced = false;
        for (int i = 0; i < entries.size(); i++) {
            Score existing = entries.get(i);
            String eName = safeGetUsername(existing);
            if (eName != null && uname != null && eName.equals(uname)) {
                entries.set(i, s);
                replaced = true;
                break;
            }
        }
        if (!replaced) entries.add(s);
        sortByScoreDescending();
    }

    /**
     * Convenience: add a Score (same as addOrReplace to preserve most-recent score).
     */
    public void addScore(Score s) {
        addOrReplace(s);
    }

    /**
     * Remove leaderboard entry by username.
     */
    public boolean removeByUsername(String username) {
        if (username == null) return false;
        Iterator<Score> it = entries.iterator();
        while (it.hasNext()) {
            Score s = it.next();
            String uname = safeGetUsername(s);
            if (username.equals(uname)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Try to remove by UUID-like id discovered inside Score (if present).
     * This method attempts a few likely getter/field names (getUserID, userID, id).
     */
    public boolean removeByUserID(UUID userID) {
        if (userID == null) return false;
        String idStr = userID.toString();
        Iterator<Score> it = entries.iterator();
        while (it.hasNext()) {
            Score s = it.next();
            String found = tryExtractIdString(s);
            if (found != null && found.equals(idStr)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Return top N Score entries (best → worst).
     */
    public ArrayList<Score> topN(int n) {
        if (n <= 0) return new ArrayList<>();
        int max = Math.min(n, entries.size());
        ArrayList<Score> out = new ArrayList<>();
        for (int i = 0; i < max; i++) out.add(entries.get(i));
        return out;
    }

    public void clear() {
        entries.clear();
    }

    public int size() {
        return entries.size();
    }

    @Override
    public String toString() {
        return "Leaderboard{entries=" + entries.size() + "}";
    }

    /* ----------------- Helpers ----------------- */

    private void sortByScoreDescending() {
        Collections.sort(entries, new Comparator<Score>() {
            @Override
            public int compare(Score a, Score b) {
                long av = (a == null ? Long.MIN_VALUE : a.getScore());
                long bv = (b == null ? Long.MIN_VALUE : b.getScore());
                // larger first
                return Long.compare(bv, av);
            }
        });
    }

    private static String safeGetUsername(Score s) {
        if (s == null) return null;
        try {
            Method m = s.getClass().getMethod("getUsername");
            Object v = m.invoke(s);
            return v == null ? null : v.toString();
        } catch (Exception t) {
            return null;
        }
    }

    /**
     * Try to extract an ID from Score (UUID or string) and return it as a string.
     * Returns null if not found.
     */
    private static String tryExtractIdString(Score s) {
        if (s == null) return null;
        String[] methodNames = {"getUserID", "getUserId", "getUser"};
        for (String mn : methodNames) {
            try {
                Method m = s.getClass().getMethod(mn);
                Object v = m.invoke(s);
                if (v != null) return v.toString();
            } catch (Exception ignored) {}
        }
        String[] fieldNames = {"userID", "userId", "id"};
        for (String fn : fieldNames) {
            try {
                Field f = s.getClass().getDeclaredField(fn);
                f.setAccessible(true);
                Object v = f.get(s);
                if (v != null) return v.toString();
            } catch (Exception ignored) {}
        }
        return null;
    }

    //console test

    public static void main(String[] args) {
        Leaderboard lb = new Leaderboard();

        Score s1 = new Score("Alice", Difficulty.EASY, 250, new java.util.Date(), Score.calculateScore(250, Difficulty.EASY));
        Score s2 = new Score("Bob", Difficulty.HARD, 30, new java.util.Date(), Score.calculateScore(30, Difficulty.HARD));
        Score s3 = new Score("Charlie", Difficulty.MEDIUM, 60, new java.util.Date(), Score.calculateScore(60, Difficulty.MEDIUM));

        lb.addScore(s1);
        lb.addScore(s2);
        lb.addScore(s3);

        System.out.println("=== Leaderboard (expected: Bob, Charlie, Alice) ===");
        for (Score s : lb.getLB()) System.out.println(s.getUsername() + " -> " + s.getScore());

        s1.setScore(1000);
        lb.addOrReplace(s1);

        System.out.println("\n=== After updating Alice (expected: Alice, Bob, Charlie) ===");
        for (Score s : lb.getLB()) System.out.println(s.getUsername() + " -> " + s.getScore());
    }
}
