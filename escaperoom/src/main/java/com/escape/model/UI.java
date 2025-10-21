package com.escape.model;

import java.util.Scanner;


/**
 * Simple hardcoded UI for now as placeholder for improvements later.
 * 
 * @author Talan Kinard
 * @author Jacob kinard
 * @version 1.1
 */
public class UI 
{
    
    private Scanner scanner = new Scanner(System.in);
    private EscapeRoomFacade facade = new EscapeRoomFacade();

    /**
     * Placeholder code just to show it running.
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
                case "1": facade.startGame(); System.out.println("Game started. User: " + facade.getCurrentUsername()); break;
                case "2": facade.loadGame(); System.out.println("Game loaded. Room: " + facade.getCurrentRoomTitle()); break;
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
                case "h": facade.getHint(); break;
                case "s": facade.saveGame(); System.out.println("Saved game."); break;
                case "q": running = false; break;
                default: System.out.println("Unknown option. Use 1,2,3, h (hint), s (save), q (quit).");
            }
        }
    }

    /**
     * Placeholder basic main menu.
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
    }

    // Scenario 1 - makeing an account and starting a game, then saving and exiting.
    public void displayScenarioOne()
    {
        System.out.println("<---Escape Room Scenario one--->");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        System.out.println("3. Login");
        System.out.println("4. Logout");
        System.out.println("5. Create Account");
        System.out.println("q to Exit");
        // menu is displayed and the user needs an account, slect option 5


            
    }

    // Scenario 2 - loading a game with an existing account.
    public void displayScenarioTwo()
    {
        System.out.println("<---Escape Room Scenario Two--->");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        System.out.println("3. Login");
        System.out.println("4. Logout");
        System.out.println("5. Create Account");
        System.out.println("q to Exit");
        // menu is displayed and the user picks option 


            
    }

    // Scenario 3 - playing the game, getting a hint, and solving a puzzle.
    public void displayScenarioThree()
    {
        System.out.println("<---Escape Room Scenario Three--->");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        System.out.println("3. Login");
        System.out.println("4. Logout");
        System.out.println("5. Create Account");
        System.out.println("q to Exit");
        // menu is displayed and the user picks option 1 to start a game
}
}
