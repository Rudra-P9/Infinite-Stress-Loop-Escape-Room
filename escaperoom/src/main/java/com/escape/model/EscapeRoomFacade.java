package com.escape.model;

import java.util.ArrayList;

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
     * Ends a game session.
     */
    public void endGame()
    {
        // stop timer and clear current session
        if (timer != null) timer.pause();
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
        // For now load first saved user and first room as a simple restore
        ArrayList<User> users = loader.getUsers();
        if (!users.isEmpty()) currentUser = users.get(0);
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
}
