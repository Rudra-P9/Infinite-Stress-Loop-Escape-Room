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
    }
}
