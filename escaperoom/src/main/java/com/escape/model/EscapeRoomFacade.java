package com.escape.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Facade class to organize the Escape Room.
 * Interface that provides game functions to start, end, pause, resuming, save, and loading.
 * 
 * @author Talan Kinard
 * @author Jacob kinard
 * @author Rudra Patel
 * @version 1.2
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
     * Checks if a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    private boolean isLoggedIn() { return currentUser != null; }

    /**
     * Ensures that core components like loader, writer, and accounts are initialized.
     */
    private void ensureCore() {
        if (loader == null)   loader = new GameDataLoader();
        if (writer == null)   writer = new GameDataWriter();
        if (accounts == null) accounts = Accounts.getInstance();
    }

        // Ensure 'progress' exists for the current user
    private void ensureProgressExists() {
        if (progress == null && currentUser != null) {
            progress = new Progress(java.util.UUID.randomUUID(), currentUser.userID);
        }
    }

    // Persist a snapshot of progress to playerData.json
    private void saveProgressSnapshot() {
        if (writer == null) writer = new GameDataWriter();
        if (progress != null) writer.saveProgress(progress);
    }


    /**
     * Starts a game session with the default difficulty level.
     */
    public void startGame() {
        if (currentDifficulty == null) {
            currentDifficulty = Difficulty.EASY;
        }
        startGame(currentDifficulty);
    }

    /**
     * Starts a game session with the specified difficulty level.
     * Requires a logged-in user.
     * @param difficulty the difficulty level of the game
     */
    public void startGame(Difficulty difficulty) {
        ensureCore();


        if (!isLoggedIn()) {
            System.out.println("ERROR: No user logged in. Please log in before starting the game.");
            return;
        }
                // If progress wasn’t set by login (e.g., direct start), restore or create now
        if (progress == null && currentUser != null) {
            Progress restored = loader.loadProgressForUser(currentUser.userID);
            progress = (restored != null) ? restored
                                        : new Progress(java.util.UUID.randomUUID(), currentUser.userID);
        }


        // Set difficulty and reset collected letters for this session
        this.currentDifficulty = (difficulty == null) ? Difficulty.EASY : difficulty;
        this.collectedLetters  = new ArrayList<>();

        // Initialize or reset the timer
        int seconds = getSecondsForDifficulty(
            currentDifficulty != null ? currentDifficulty : Difficulty.EASY);

        if (timer == null) {
            timer = new Timer(seconds);
        } else {
            timer = new Timer(seconds);
        }
        timer.start();

        // Launch the game
        new Rooms().startGame(this);
    }

    /**
     * Marks the first unsolved puzzle in the current room as solved.
     * Used for scenarios or tests.
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
                p.setSolved(true);
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

    // in EscapeRoomFacade
    public void saveProgressForCurrentUser(Progress p) {
        ensureCore();
        if (p != null) writer.saveProgress(p);
    }

    public void restoreProgressForCurrentUser() {
        ensureCore();
        if (currentUser == null) return;
        Progress p = loader.loadProgressForUser(currentUser.userID);
        if (p != null) this.progress = p;
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
        ensureCore();
    if (!isLoggedIn()) {
        System.out.println("ERROR: No user logged in. Nothing to end.");
        return;
    }
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
            lb.addOrReplace(score);
            writer.saveLeaderboard(lb);
            
            System.out.println("Game ended. Final score: " + finalScore);
            saveProgressSnapshot();
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
        ensureCore();
    if (!isLoggedIn()) {
        System.out.println("ERROR: No user logged in. Cannot save.");
        return;
    }
        // persist minimal saved data via writer
        if (writer == null) writer = new GameDataWriter();
        SavedData sd = new SavedData();
        sd.room = (currentRoom == null ? null : currentRoom.getRoomID());
        sd.score = (score == null ? 0 : (int) score.getScore());
        sd.hints = 0; // not tracked centrally yet
        sd.puzzle = null;
        writer.saveSavedData(sd);
        saveProgressSnapshot();


    }

    /**
 * Loads a previously saved game for the current user (requires login).
 * Minimal restore: user (refresh), rooms list, currentRoom = first or room1,
 * timer based on difficulty and puzzles for the room.
 */
public void loadGame() {
    ensureCore();

    if (!isLoggedIn()) {
        System.out.println("ERROR: No user logged in. Please log in before loading the game.");
        return;
    }

    // Refresh this user from disk by username (if present)
    ArrayList<User> users = loader.getUsers();
    if (users != null) {
        for (User u : users) {
            if (u.getUsername() != null && u.getUsername().equals(currentUser.getUsername())) {
                currentUser = u;
                break;
            }
        }
    }

    // Load rooms
    allRooms = loader.getRooms();
    if (allRooms == null || allRooms.isEmpty()) {
        System.out.println("ERROR: No rooms found to load.");
        return;
    }

    // Pick room1 or the first as a simple “resume”
    currentRoom = null;
    for (Rooms r : allRooms) {
        if ("room1".equalsIgnoreCase(r.getRoomID())) { currentRoom = r; break; }
    }
    if (currentRoom == null) currentRoom = allRooms.get(0);

    // Load puzzles into the room (so UI can run them)
    var textMap = new java.util.HashMap<String,String>();
    var puzzles = loader.loadPuzzlesForRoom(currentRoom.getRoomID(), textMap);
    currentRoom.setPuzzles(new ArrayList<>(puzzles));

    // If timer/progress/score are null (first load), initialize them
    if (currentDifficulty == null) currentDifficulty = Difficulty.EASY;
    if (timer == null)  timer  = new Timer(getSecondsForDifficulty(currentDifficulty));
    if (progress == null) progress = new Progress(UUID.randomUUID(), currentUser.userID);
    if (score == null)    score    = new Score(currentUser.getUsername(), currentDifficulty, 0, new java.util.Date(), 0);

    System.out.println("Game loaded for " + currentUser.getUsername()
        + ". Room: " + (currentRoom.getTitle() == null ? currentRoom.getRoomID() : currentRoom.getTitle()));
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

    /**
     * Returns the title of the current room, or null if no room is selected.
     */
    public String getCurrentRoomTitle() {
        return currentRoom == null ? null : currentRoom.getTitle();
    }

    /**
     * Returns the number of seconds remaining for the player to complete the current room.
     * If no timer is set (i.e. the game is not running), returns 0.
     * @return the number of seconds remaining for the player to complete the current room
     */
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
        if (username == null || password == null) {
            System.out.println("ERROR: Username and password cannot be null.");
            return;
        }
        String uNorm = username.trim();

        // Check persisted users
        if (loader == null) loader = new GameDataLoader();
        for (User u : loader.getUsers()) {
            if (u.getUsername() != null && u.getPassword() != null
                && u.getUsername().equalsIgnoreCase(uNorm)
                && u.getPassword().equals(password)) {
            currentUser = u;
                // After we have a currentUser, try to restore progress
            if (currentUser != null) {
                if (loader == null) loader = new GameDataLoader();
                Progress restored = loader.loadProgressForUser(currentUser.userID);
                if (restored != null) {
                    this.progress = restored;
                    System.out.println("Restored progress for " + currentUser.getUsername()
                        + " (pos=" + progress.getStoryPos()
                        + ", hints=" + progress.getHintsUsed()
                        + ", solved=" + progress.getQuestionsAnswered() + ")");
                } else {
                    this.progress = new Progress(java.util.UUID.randomUUID(), currentUser.userID);
                }
            }

            return;
            }
        }

        // Check in-memory accounts
        if (accounts == null) accounts = Accounts.getInstance();
        User u = accounts.getUserCaseInsensitive(uNorm); // or scan accounts list and equalsIgnoreCase
        if (u != null && password.equals(u.getPassword()))
         currentUser = u;
                // After we have a currentUser, try to restore progress
        if (currentUser != null) {
            if (loader == null) loader = new GameDataLoader();
            Progress restored = loader.loadProgressForUser(currentUser.userID);
            if (restored != null) {
                this.progress = restored;
                System.out.println("Restored progress for " + currentUser.getUsername()
                    + " (pos=" + progress.getStoryPos()
                    + ", hints=" + progress.getHintsUsed()
                    + ", solved=" + progress.getQuestionsAnswered() + ")");
            } else {
                this.progress = new Progress(java.util.UUID.randomUUID(), currentUser.userID);
            }
        }

}


    /** Log out current user. */
    public void logout() {
        currentUser = null;
    }

    private Difficulty currentDifficulty;
    private ArrayList<String> collectedLetters;
    private ArrayList<Rooms> allRooms;

    
    /**
     * Returns the number of seconds remaining for the current room based on the difficulty.
     * Used to initialize the timer for a room.
     * 
     * @param diff the difficulty level of the room
     * @return the number of seconds remaining for the room
     */
    private int getSecondsForDifficulty(Difficulty diff) {
        switch (diff) {
            case EASY: return 1800;
            case MEDIUM: return 1500;
            case HARD: return 1200;
            default: return 1800;
        }
    }

    /**
     * Solve a puzzle in the current room.
     * 
     * @param answer the user's answer to the puzzle
     * @return true if the puzzle is solved, false otherwise
     */
    public boolean solvePuzzle(String answer) {
        if (!isLoggedIn()) {
        System.out.println("No user logged in");
        return false;
    }
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
                    ensureProgressExists();
                    progress.advanceStory();
                    saveProgressSnapshot();

                    
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

    /**
     * Reveals the hint of an unsolved puzzle in the current room.
     * Returns "No current room" if there is no current room,
     * "No puzzles in room" if there are no puzzles in the current room,
     * or "All puzzles solved in this room" if all puzzles are solved.
     * Otherwise, returns the hint of the first unsolved puzzle found.
     * @return the hint of an unsolved puzzle or a message indicating the state of the room
     */
    public String useHint() {
        if (currentRoom == null) return "No current room";
        
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return "No puzzles in room";
        
        for (Puzzle p : puzzles) {
            if (!p.solved()) {
                ensureProgressExists();
                progress.useHint();
                saveProgressSnapshot();
                return p.getHint();
            }
        }
        
        return "All puzzles solved in this room";
    }

    /**
     * Move to a room specified by its roomID.
     * 
     * @param roomID the roomID of the room to move to
     * @return true if the room is found and moved to, false otherwise
     */
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

    /**
     * Returns true if all puzzles in the current room are solved, false otherwise.
     * If there is no current room or no puzzles in the current room, returns true.
     * @return true if all puzzles in the current room are solved, false otherwise
     */
    public boolean isCurrentRoomComplete() {
        if (currentRoom == null) return false;
        ArrayList<Puzzle> puzzles = currentRoom.getPuzzles();
        if (puzzles == null || puzzles.isEmpty()) return true;
        
        for (Puzzle p : puzzles) {
            if (!p.solved()) return false;
        }
        return true;
    }

    /**
     * Calculates the final score based on the remaining time and hints used.
     * The formula is: baseScore = timeLeft * difficultyMultiplier, penalty = hintsUsed * 50, finalScore = max(0, baseScore - penalty).
     * If the score object is not null, its score value will be updated with the final score.
     * @return the final score
     */
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

    /**
     * Returns a copy of the collected letters from the Escape Room.
     * 
     * @return a copy of the collected letters
     */
    public ArrayList<String> getCollectedLetters() {
        return new ArrayList<>(collectedLetters);
    }

    /**
     * Returns the current difficulty level of the game.
     * @return the current difficulty level of the game
     */
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * Updates the game's difficulty and resets the timer accordingly.
     * Called by the UI when the player changes difficulty.
     */
    public void setCurrentDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            System.out.println("Invalid difficulty. Defaulting to EASY.");
            difficulty = Difficulty.EASY;
        }

        this.currentDifficulty = difficulty;

        int seconds = getSecondsForDifficulty(difficulty);
        this.timer = new Timer(seconds);

        System.out.println("Difficulty set to " + difficulty +
            " (" + seconds + " seconds). Timer reset.");
    }

    /**
     * Returns the current room, or null if no room is selected.
     * @return the current room
     */
    public Rooms getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the currently logged in user, or null if no user is logged in.
     * 
     * @return the currently logged in user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the current score, or null if no score is recorded.
     * This score is updated by the calculateFinalScore() method.
     * @return the current score, or null if no score is recorded
     */
    public Score getCurrentScore() {
        return score;
    }
}
