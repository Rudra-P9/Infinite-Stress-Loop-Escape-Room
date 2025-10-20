package com.escape.model;

/**
 * Class that represents the leaderboard for the Escape Room.
 * Stores the list of users and their rankings.
 * 
 * @author Talan Kinard
 * @author Dylan Diaz
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Leaderboard {

    private final ArrayList<User> lb = new ArrayList<>();

    public Leaderboard() { }

    public ArrayList<User> getLB() {
        return new ArrayList<>(lb);
    }

    public void setLB(List<User> users) {
        lb.clear();
        if (users != null) lb.addAll(users);
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
    }

    public boolean removeByUserID(UUID userID) {
        if (userID == null) return false;
        Iterator<User> it = lb.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.userID != null && u.userID.equals(userID)) {
                it.remove();
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
}
