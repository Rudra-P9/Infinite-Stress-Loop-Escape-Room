package com.escape.model;

import java.util.Scanner;


/**
 * Simple hardcoded UI for now as placeholder for improvements later.
 * 
 * @author Talan Kinard
 * @author Jacob kinard
 * @version 1.2
 */
public class UI 
{
    
    private Scanner scanner = new Scanner(System.in);
    private EscapeRoomFacade facade = new EscapeRoomFacade();

    /**
     * Running the game UI.
     */
    public void run()
    {
        System.out.println("Running EscapeRoom (NOTE, not final version: UI shouldnt use console or promp user)");
        displayMainMenu();
        // very small loop to accept simple commands for the stub
        boolean running = true;
        while (running) {
            System.out.print("Enter choice (or 'q' to quit): ");
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": if (facade.getCurrentUser() == null) {
                        System.out.println("ERROR: Please log in before starting the game.");
                        break;
                    }
                    facade.startGame();
                    System.out.println("Game started. User: " + facade.getCurrentUsername());
                    // If we want to immediately run puzzles for the current room, call runner here.
                    // startGameUI(); method we use to loop puzzles
                    break;
                case "2": if (facade.getCurrentUser() == null) {
                        System.out.println("ERROR: Please log in before loading the game.");
                        break;
                    }
                        facade.loadGame();
                        System.out.println("Game loaded. Room: " + facade.getCurrentRoomTitle());
                        break;
                case "3": System.out.println("Exiting UI."); running = false; break;
                case "4": // create account
                    System.out.print("New username: ");
                    String nu = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String np = scanner.nextLine().trim();
                    facade.createAccount(nu, np);
                    System.out.println("Requested account creation for: " + nu);
                    break;
                case "5": // login
                    System.out.print("Username: ");
                    String lu = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String lp = scanner.nextLine().trim();
                    facade.login(lu, lp);
                    System.out.println("Login attempted for: " + lu + " (current user: " + facade.getCurrentUsername() + ")");
                    break;
                case "6": // logout
                    facade.logout();
                    System.out.println("Logged out.");
                    break;
                case "7": // display leaderboard
                    displayLeaderboard();
                    break;
                case "s": facade.saveGame(); System.out.println("Saved game."); break;
                case "q": running = false; break;
                default: System.out.println("Unknown option. Use 1,2,3, h (hint), s (save), q (quit).");
            }
            
        }
        System.exit(0); // exit in place to stop inifinate loops in puzzles
    }

    /**
     * Display the leaderboard.
     */
    private void displayLeaderboard() {
        Leaderboard leaderboard = new Leaderboard(); // Assuming Leaderboard is initialized elsewhere or can be instantiated here
        System.out.println("=== Leaderboard ===");
        int rank = 1;
        for (Score score : leaderboard.getLB()) {
            System.out.println(rank + ". " + score.getUsername() + " - " + score.getScore());
            rank++;
        }
        System.out.println("===================");
    }

    /**
     * main menu.
     */
    public void displayMainMenu()
    {
        System.out.println("<---Escape Room!!!!--->");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        System.out.println("3. Exit");
        System.out.println("4. Create Account");
        System.out.println("5. Login");
        System.out.println("6. Logout");
        System.out.println("7. Display Leaderboard");
        System.out.println();
        System.out.println("Other commands: s (save), q (quit)");
    }


    
}
