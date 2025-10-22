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
                case "sc1": runScenarioOne(); break;
                case "sc2": runScenarioTwo(); break;
                case "sc3": runScenarioThree(); break;
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
                case "checkroom":
                    System.out.println(facade.checkRoom());
                    break;
                case "checkuser":
                    System.out.println(facade.checkUser());
                    break;
                case "checkpuzzle":
                    System.out.println(facade.checkPuzzles());
                    break;
                case "checkprogress":
                    System.out.println(facade.checkProgress());
                    break;
                case "checkall":
                    System.out.println(facade.checkAll());
                    break;
                case "7": // S1
                    runScenarioOne();
                    System.out.println("Starting Scenario One...");
                    break;
                case "8": // S2
                    runScenarioTwo();
                    System.out.println("Starting Scenario Two...");
                    break;
                case "9": // S3
                    runScenarioThree();
                    System.out.println("Starting Scenario Three...");
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
        System.out.println();
        System.out.println("Scenario runners (shortcuts):");
        System.out.println("sc1 - Run Scenario 1 (create, start, save)");
        System.out.println("sc2 - Run Scenario 2 (create, start, save, load)");
        System.out.println("sc3 - Run Scenario 3 (create, start, hint, solve, save)");
        System.out.println();
        System.out.println("Other commands: h (hint), s (save), q (quit), checkall (validation)");
    }


    /* ---------------------
       Scenario runners (automated flows)
       --------------------- */
    public void runScenarioOne() {
        System.out.println("\n--- SCENARIO 1: create account, start, save ---");
        String username = "scenario1_" + System.currentTimeMillis();
        facade.createAccount(username, "pw1");
        facade.startGame();
        System.out.println("Started game for: " + username + ", time remaining: " + facade.getTimeRemaining());
        facade.saveGame();
        System.out.println("Scenario 1 complete.\n");
    }

    public void runScenarioTwo() {
        System.out.println("\n--- SCENARIO 2: create account, start, save, load ---");
        String username = "scenario2_" + System.currentTimeMillis();
        facade.createAccount(username, "pw2");
        facade.startGame();
        facade.saveGame();
        facade.loadGame();
        System.out.println("Loaded user: " + facade.getCurrentUsername() + ", Room: " + facade.getCurrentRoomTitle());
        System.out.println("Scenario 2 complete.\n");
    }

    public void runScenarioThree() {
        System.out.println("\n--- SCENARIO 3: create account, start, hint, solve puzzle, save ---");
        String username = "scenario3_" + System.currentTimeMillis();
        facade.createAccount(username, "pw3");
        facade.startGame();
        System.out.println("Requesting a hint:");
        facade.getHint();
        System.out.println("Simulating solving a puzzle...");
        facade.solveCurrentPuzzle();
        facade.saveGame();
        System.out.println("Scenario 3 complete.\n");
    }
}
