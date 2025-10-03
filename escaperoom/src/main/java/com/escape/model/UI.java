package com.escape.model;

import java.util.Scanner;


/**
 * Simple hardcoded UI for now as placeholder for improvements later.
 * 
 * @author Talan Kinard
 */
public class UI 
{
    
    private Scanner scanner = new Scanner(System.in);

    /**
     * Placeholder code just to show it running.
     */
    public void run()
    {
        System.out.println("Running EscapeRoom stub version.");
        displayMainMenu();
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
