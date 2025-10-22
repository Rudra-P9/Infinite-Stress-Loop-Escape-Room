package com.escape.model;

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
 */
public class Leaderboard {

    private final ArrayList<User> lb = new ArrayList<>();

    public Leaderboard() { }

    public ArrayList<User> getLB() {
        return new ArrayList<>(lb);
    }

    public void setLB(List<User> users) {
        lb.clear();
        if (users != null) lb.addAll(users);
        sortByScoreDescending();
    }

    public void addOrReplace(User user) {
        if (user == null) return;
        boolean replaced = false;
        int index = 0;
        for (User existing : lb) {
            try {
                if (existing.userID != null && user.userID != null &&
                        existing.userID.toString().equals(user.userID.toString())) {
                    lb.set(index, user);
                    replaced = true;
                    break;
                }
            } catch (Exception ignore) { }
            if (existing.getUsername() != null && user.getUsername() != null &&
                    existing.getUsername().equals(user.getUsername())) {
                lb.set(index, user);
                replaced = true;
                break;
            }
            index++;
        }
        if (!replaced) lb.add(user);
        sortByScoreDescending();
    }

    public boolean removeByUserID(UUID userID) {
        if (userID == null) return false;
        Iterator<User> it = lb.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.userID != null && u.userID.equals(userID)) {
                it.remove();
                sortByScoreDescending();
                return true;
            }
        }
        return false;
    }

    public boolean removeByUsername(String username) {
        if (username == null) return false;
        Iterator<User> it = lb.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (username.equals(u.getUsername())) {
                it.remove();
                sortByScoreDescending();
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> topN(int n) {
        if (n <= 0) return new ArrayList<>();
        int max = Math.min(n, lb.size());
        ArrayList<User> out = new ArrayList<>();
        for (int i = 0; i < max; i++) out.add(lb.get(i));
        return out;
    }

    public void clear() {
        lb.clear();
    }

    public int size() {
        return lb.size();
    }

    @Override
    public String toString() {
        return "Leaderboard{entries=" + lb.size() + "}";
    }

    /** Helper method: sorts leaderboard so highest scores come first. */
    private void sortByScoreDescending() {
        lb.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
    }




    //Testing the Leaderboard

    public static void main(String[] args) {
    Leaderboard lb = new Leaderboard();

    User u1 = new User(java.util.UUID.randomUUID(), "Alice", "pw");
    User u2 = new User(java.util.UUID.randomUUID(), "Bob", "pw");
    User u3 = new User(java.util.UUID.randomUUID(), "Charlie", "pw");

    u1.setScore(250);
    u2.setScore(900);
    u3.setScore(600);

    lb.addOrReplace(u1);
    lb.addOrReplace(u2);
    lb.addOrReplace(u3);

    System.out.println("=== Leaderboard (expected: Bob, Charlie, Alice) ===");
    for (User u : lb.getLB()) {
        System.out.println(u.getUsername() + " - " + u.getScore());
    }

    // Update Alice’s score and test resorting
    u1.setScore(1000);
    lb.addOrReplace(u1);

    System.out.println("\n=== After updating Alice (expected: Alice, Bob, Charlie) ===");
    for (User u : lb.getLB()) {
        System.out.println(u.getUsername() + " - " + u.getScore());
    }
}








}
