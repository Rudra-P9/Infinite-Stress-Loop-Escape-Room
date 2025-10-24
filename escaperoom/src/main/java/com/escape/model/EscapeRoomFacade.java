package com.escape.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Facade class to organize the Escape Room.
 * Interface that provides game functions to start, end, pause, resuming, save, and loading.
 * 
 * @author Talan Kinard
 * @author Jacob kinard
 * @version 1.1
 */

public class EscapeRoomFacade 
{

    private User currentUser;
    private Rooms currentRoom;
    private Timer timer;
    private GameDataLoader loader;
    private GameDataWriter writer;
    private Accounts accounts;
    private Score score;
    private Progress progress;

    /**
     * Starts a game session.
     */
    public void startGame()
    {
        // initialize components used during a game
        if (loader == null) loader = new GameDataLoader();
        if (writer == null) writer = new GameDataWriter();
        if (accounts == null) accounts = Accounts.getInstance();
        if (timer == null) timer = new Timer(1800); // default seconds, may be overwritten by room difficulty
        // choose a default room if none set
        if (currentRoom == null) {
            ArrayList<Rooms> rooms = loader.getRooms();
            if (!rooms.isEmpty()) currentRoom = rooms.get(0);
        }

    }

    /**
     * Starts a game session with the given difficulty.
     * If no difficulty is provided, the game defaults to EASY difficulty.
     * 
     * @param difficulty the difficulty level of the game
     */
    public void startGame(Difficulty difficulty) {
        if (currentUser == null) {
            System.out.println("ERROR: No user logged in. Cannot start game.");
            return;
        }
        
        this.currentDifficulty = (difficulty == null) ? Difficulty.EASY : difficulty;
        this.collectedLetters = new ArrayList<>();
        
        // Load all rooms from JSON
        allRooms = loader.getRooms();
        if (allRooms.isEmpty()) {
            System.out.println("ERROR: No rooms found in game.json");
            return;
        }
        
        // Start with room1
        for (Rooms room : allRooms) {
            if ("room1".equals(room.getRoomID())) {
                currentRoom = room;
                break;
            }
        }
        
        if (currentRoom == null) currentRoom = allRooms.get(0);
        
        // Initialize timer based on difficulty
        int seconds = getSecondsForDifficulty(currentDifficulty);
        timer = new Timer(seconds);
        timer.start();
        
        // Initialize progress
        progress = new Progress(UUID.randomUUID(), currentUser.userID);
        
        // Initialize score
        score = new Score(currentUser.getUsername(), currentDifficulty, 0, new java.util.Date(), 0);
        
        System.out.println("Game started for " + currentUser.getUsername() + 
                         " on " + currentDifficulty + " difficulty (" + seconds + "s)");
    }

    /**
     * Helper used by scenarios/tests: mark the first unsolved puzzle in the current room as solved.
     */
    public void solveCurrentPuzzle() {
        if (currentRoom == null) {
            System.out.println("No current room to solve puzzles in.");
            return;
        }
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) {
            System.out.println("No puzzles found in current room.");
            return;
        }

        for (Puzzle p : puzzles) {
            if (!p.solved()) {
                // setSolved is protected in Puzzle; same package allows access
                p.setSolved(true);
                // advance or create progress
                if (progress == null) {
                    UUID userId = (currentUser == null) ? null : currentUser.userID;
                    progress = new Progress(UUID.randomUUID(), userId);
                }
                progress.advanceStory();
                System.out.println("Solved puzzle: " + p.getTitle());
                return;
            }
        }
        System.out.println("All puzzles already solved.");
    }

    public Progress getProgress() { return progress; }

    /**
     * Return a short summary about the current room for validation.
     */
    public String checkRoom() {
        if (currentRoom == null) return "No current room";
        StringBuilder sb = new StringBuilder();
        sb.append("Room: ").append(currentRoom.getTitle()).append(" (ID=").append(currentRoom.getRoomID()).append(")\n");
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) sb.append("  no puzzles\n");
        else {
            sb.append("  puzzles:\n");
            for (Puzzle p : puzzles) {
                sb.append("    - ").append(p.getTitle()).append(" [solved=").append(p.solved()).append("]\n");
            }
        }
        return sb.toString();
    }

    /**
     * Return a short summary about the current user for validation.
     */
    public String checkUser() {
        if (currentUser == null) return "No user logged in";
        return "User: " + currentUser.getUsername() + " (id=" + currentUser.userID + ")";
    }

    /**
     * Return a short summary about puzzles in the current room.
     */
    public String checkPuzzles() {
        if (currentRoom == null) return "No current room";
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return "No puzzles in current room";
        StringBuilder sb = new StringBuilder();
        for (Puzzle p : puzzles) {
            sb.append(p.getTitle()).append(" -> solved=").append(p.solved()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Return progress summary if available.
     */
    public String checkProgress() {
        if (progress == null) return "No progress recorded";
        return progress.toString();
    }

    /**
     * Return a combined summary of user, room, puzzles, and progress for quick validation.
     */
    public String checkAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== CHECKALL ===\n");
        sb.append(checkUser()).append("\n");
        sb.append(checkRoom()).append("\n");
        sb.append("Puzzles:\n");
        sb.append(checkPuzzles()).append("\n");
        sb.append("Progress: ").append(checkProgress()).append("\n");
        sb.append("=== END CHECKALL ===");
        return sb.toString();
    }

    /**
     * Ends a game session.
     */
    public void endGame()
    {
        // stop timer and clear current session
        if (timer != null) timer.pause();

        // calculate final score
        long finalScore = calculateFinalScore();
    
        // save score and update leaderboard
        if (currentUser != null && score != null) {
            score.setTimeLeftSec(timer == null ? 0 : timer.getRemainingSeconds());
            score.setScore(finalScore);
            currentUser.setScore((int) finalScore);
            
            writer.saveScore(score);
            
            Leaderboard lb = loader.getLeaderboard();
            lb.addOrReplace(currentUser);
            writer.saveLeaderboard(lb);
            
            System.out.println("Game ended. Final score: " + finalScore);
        }

        // cleanup
        currentUser = null;
        currentRoom = null;

    }

    /**
     * Pauses a game session.
     */
    public void pauseGame()
    {
        if (timer != null) timer.pause();

    }

    /**
     * Resumes a game session.
     */
    public void resumeGame()
    {
        if (timer != null) timer.resume();
        
    }

    /**
     * Saves the current game.
     */
    public void saveGame()
    {
        // persist minimal saved data via writer
        if (writer == null) writer = new GameDataWriter();
        SavedData sd = new SavedData();
        sd.room = (currentRoom == null ? null : currentRoom.getRoomID());
        sd.score = (score == null ? 0 : (int) score.getScore());
        sd.hints = 0; // not tracked centrally yet
        sd.puzzle = null;
        writer.saveSavedData(sd);

    }

    /**
     * Loads a previously saved game.
     */
    public void loadGame()
    {
        if (loader == null) loader = new GameDataLoader();
        // Load persisted users and rooms. If a currentUser already exists (e.g. just created
        // or logged in), prefer refreshing it from persisted users (match by username).
        ArrayList<User> users = loader.getUsers();
        if (currentUser == null) {
            if (!users.isEmpty()) currentUser = users.get(0);
        } else {
            if (users != null) {
                for (User u : users) {
                    if (u.getUsername() != null && u.getUsername().equals(currentUser.getUsername())) {
                        currentUser = u; // refresh to persisted copy
                        break;
                    }
                }
            }
        }

        ArrayList<Rooms> rooms = loader.getRooms();
        if (!rooms.isEmpty()) currentRoom = rooms.get(0);

    }

    /**
     * Displays a hint to the player.
     */
    public void getHint()
    {
        // Expose hint retrieval via currentRoom's puzzles if present
        if (currentRoom == null) return;
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return;
        Puzzle p = puzzles.get(0);
        System.out.println("Hint: " + p.getHint());
    }

    /**
     * Retrieves info about the user
     */
    public void getUser()
    {

    }

    /**
     * Gets the current room.
     */
    public void getRoom()
    {

    }

    // Print basic user info to stdout for console UI
    public void printUserInfo() {
        if (currentUser == null) System.out.println("No user logged in");
        else System.out.println("User: " + currentUser.getUsername());
    }

    // Print basic room info
    public void printRoomInfo() {
        if (currentRoom == null) System.out.println("No room selected");
        else System.out.println("Room: " + currentRoom.toString());
    }

    /* Additional simple accessors used by the UI */
    public String getCurrentUsername() {
        return currentUser == null ? null : currentUser.getUsername();
    }

    public String getCurrentRoomTitle() {
        return currentRoom == null ? null : currentRoom.getTitle();
    }

    public int getTimeRemaining() {
        return timer == null ? 0 : timer.getRemainingSeconds();
    }

    /* -----------------------
       Account-related facade wrappers (void delegations to Accounts)
       ----------------------- */

    /** Create an account via Accounts singleton and persist it. */
    public void createAccount(String username, String password) {
        if (accounts == null) accounts = Accounts.getInstance();
        accounts.createAccount(username, password);
        // persist accounts to disk if writer available
        if (writer == null) writer = new GameDataWriter();
        writer.saveAccounts(accounts);
        // Make the newly created account the active user so subsequent save/load operate on it.
        try {
            User created = accounts.getUser(username);
            if (created != null) {
                this.currentUser = created;
            }
        } catch (Exception ignore) {
            // defensive: don't break account creation on minor errors
        }
    }

    /** Delete an account via Accounts singleton and persist changes. */
    public void deleteAccount(String username) {
        if (accounts == null) accounts = Accounts.getInstance();
        accounts.deleteAccount(username);
        if (writer == null) writer = new GameDataWriter();
        writer.saveAccounts(accounts);
    }

    /** Attempt to log in a user. This is a void wrapper; UI can call getCurrentUsername() to check result. */
    public void login(String username, String password) {
        if (username == null || password == null) return;
        // try persisted users first
        if (loader == null) loader = new GameDataLoader();
        for (User u : loader.getUsers()) {
            if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                currentUser = u;
                return;
            }
        }
        // try in-memory accounts
        if (accounts == null) accounts = Accounts.getInstance();
        User u = accounts.getUser(username);
        if (u != null) {
            // The Accounts class currently doesn't verify password; use User.getPassword if available
            if (password.equals(u.getPassword())) currentUser = u;
        }
    }

    /** Log out current user. */
    public void logout() {
        currentUser = null;
    }

    private Difficulty currentDifficulty;
    private ArrayList<String> collectedLetters;
    private ArrayList<Rooms> allRooms;

    
    private int getSecondsForDifficulty(Difficulty diff) {
        switch (diff) {
            case EASY: return 1800;
            case MEDIUM: return 1500;
            case HARD: return 1200;
            default: return 1800;
        }
    }

    public boolean solvePuzzle(String answer) {
        if (currentRoom == null) {
            System.out.println("No current room");
            return false;
        }
        
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) {
            System.out.println("No puzzles in room");
            return false;
        }
        
        for (Puzzle p : puzzles) {
            if (!p.solved()) {
                boolean correct = p.checkAnswer(answer);
                if (correct) {
                    p.setSolved(true);
                    progress.advanceStory();
                    
                    String rewardLetter = p.getRewardLetter();
                    if (rewardLetter != null && !rewardLetter.isEmpty() 
                        && !collectedLetters.contains(rewardLetter)) {
                        collectedLetters.add(rewardLetter);
                        System.out.println("Collected letter: " + rewardLetter);
                    }
                    
                    System.out.println("Puzzle solved: " + p.getTitle());
                    return true;
                } else {
                    System.out.println("Incorrect answer");
                    return false;
                }
            }
        }
        
        System.out.println("All puzzles in this room are already solved");
        return false;
    }

    public String useHint() {
        if (currentRoom == null) return "No current room";
        
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return "No puzzles in room";
        
        for (Puzzle p : puzzles) {
            if (!p.solved()) {
                progress.useHint();
                return p.getHint();
            }
        }
        
        return "All puzzles solved in this room";
    }

    public boolean moveToRoom(String roomID) {
        if (allRooms == null) {
            allRooms = loader.getRooms();
        }
        
        for (Rooms room : allRooms) {
            if (roomID.equals(room.getRoomID())) {
                currentRoom = room;
                System.out.println("Moved to room: " + room.getTitle());
                return true;
            }
        }
        
        System.out.println("Room not found: " + roomID);
        return false;
    }

    public boolean isCurrentRoomComplete() {
        if (currentRoom == null) return false;
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return true;
        
        for (Puzzle p : puzzles) {
            if (!p.solved()) return false;
        }
        return true;
    }

    public long calculateFinalScore() {
        if (timer == null || currentDifficulty == null) return 0;
        
        long timeLeft = timer.getRemainingSeconds();
        int hints = progress == null ? 0 : progress.getHintsUsed();
        
        double multiplier;
        switch (currentDifficulty) {
            case EASY: multiplier = 1.0; break;
            case MEDIUM: multiplier = 1.5; break;
            case HARD: multiplier = 2.0; break;
            default: multiplier = 1.0;
        }
        
        long baseScore = (long) (timeLeft * multiplier);
        long penalty = hints * 50;
        long finalScore = Math.max(0, baseScore - penalty);
        
        if (score != null) {
            score.setScore(finalScore);
        }
        
        return finalScore;
    }

    public ArrayList<String> getCollectedLetters() {
        return new ArrayList<>(collectedLetters);
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public Rooms getCurrentRoom() {
        return currentRoom;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Score getCurrentScore() {
        return score;
    }
}
