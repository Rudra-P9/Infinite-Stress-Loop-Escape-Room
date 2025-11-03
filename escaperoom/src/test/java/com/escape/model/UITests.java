package com.escape.model;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UITests {
    
    private UI ui;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        ui = new UI();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Tests that UI object can be instantiated.
     */
    @Test
    public void constructor_shouldCreateUIInstance() {
        UI testUI = new UI();
        assertNotNull(testUI);
    }

    /**
     * Tests that displayMainMenu displays the game title.
     */
    @Test
    public void displayMainMenu_shouldDisplayGameTitle() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Escape Room : The Varen Project"));
    }

    /**
     * Tests that displayMainMenu displays the Start Game option.
     */
    @Test
    public void displayMainMenu_shouldDisplayStartGameOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("1. Start Game"));
    }

    /**
     * Tests that displayMainMenu displays the Load Game option.
     */
    @Test
    public void displayMainMenu_shouldDisplayLoadGameOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("2. Load Game"));
    }

    /**
     * Tests that displayMainMenu displays the Exit option.
     */
    @Test
    public void displayMainMenu_shouldDisplayExitOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("3. Exit"));
    }

    /**
     * Tests that displayMainMenu displays the Create Account option.
     */
    @Test
    public void displayMainMenu_shouldDisplayCreateAccountOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("4. Create Account"));
    }

    /**
     * Tests that displayMainMenu displays the Login option.
     */
    @Test
    public void displayMainMenu_shouldDisplayLoginOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("5. Login"));
    }

    /**
     * Tests that displayMainMenu displays the Logout option.
     */
    @Test
    public void displayMainMenu_shouldDisplayLogoutOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("6. Logout"));
    }

    /**
     * Tests that displayMainMenu displays the Display Leaderboard option.
     */
    @Test
    public void displayMainMenu_shouldDisplayLeaderboardOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("7. Display Leaderboard"));
    }

    /**
     * Tests that displayMainMenu displays the Change Difficulty option.
     */
    @Test
    public void displayMainMenu_shouldDisplayChangeDifficultyOption() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("8. Change Difficulty"));
    }

    /**
     * Tests that displayMainMenu displays other command options.
     */
    @Test
    public void displayMainMenu_shouldDisplayOtherCommands() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertTrue(output.contains("Other commands"));
        assertTrue(output.contains("s (save)"));
        assertTrue(output.contains("q (quit)"));
    }

    /**
     * Tests that displayMainMenu displays all menu options together.
     */
    @Test
    public void displayMainMenu_shouldDisplayAllOptions() {
        ui.displayMainMenu();
        String output = outContent.toString();
        
        assertTrue(output.contains("1. Start Game"));
        assertTrue(output.contains("2. Load Game"));
        assertTrue(output.contains("3. Exit"));
        assertTrue(output.contains("4. Create Account"));
        assertTrue(output.contains("5. Login"));
        assertTrue(output.contains("6. Logout"));
        assertTrue(output.contains("7. Display Leaderboard"));
        assertTrue(output.contains("8. Change Difficulty"));
    }

    /**
     * Tests that displayMainMenu can be called multiple times without errors.
     */
    @Test
    public void displayMainMenu_shouldAllowMultipleCalls() {
        ui.displayMainMenu();
        ui.displayMainMenu();
        ui.displayMainMenu();
        
        String output = outContent.toString();
        // Should contain the title at least 3 times
        int count = 0;
        int index = 0;
        String searchString = "Escape Room : The Varen Project";
        while ((index = output.indexOf(searchString, index)) != -1) {
            count++;
            index += searchString.length();
        }
        assertTrue(count >= 3);
    }

    /**
     * Tests that UI instance has non-null components after instantiation.
     */
    @Test
    public void constructor_shouldInitializeComponents() {
        UI testUI = new UI();
        assertNotNull(testUI);
        // The UI should be ready to use
    }

    /**
     * Tests that displayMainMenu output is not empty.
     */
    @Test
    public void displayMainMenu_shouldProduceNonEmptyOutput() {
        ui.displayMainMenu();
        String output = outContent.toString();
        assertFalse(output.isEmpty());
        assertTrue(output.length() > 0);
    }

    /**
     * Tests that displayMainMenu output contains proper formatting.
     */
    @Test
    public void displayMainMenu_shouldContainProperFormatting() {
        ui.displayMainMenu();
        String output = outContent.toString();
        
        // Check for menu separator markers
        assertTrue(output.contains("<---"));
        assertTrue(output.contains("--->"));
    }

    /**
     * Tests that displayMainMenu lists options in correct order.
     */
    @Test
    public void displayMainMenu_shouldListOptionsInOrder() {
        ui.displayMainMenu();
        String output = outContent.toString();
        
        int startPos = output.indexOf("1. Start Game");
        int loadPos = output.indexOf("2. Load Game");
        int exitPos = output.indexOf("3. Exit");
        int createPos = output.indexOf("4. Create Account");
        
        assertTrue(startPos < loadPos);
        assertTrue(loadPos < exitPos);
        assertTrue(exitPos < createPos);
    }

    /**
     * Tests that multiple UI instances can be created independently.
     */
    @Test
    public void multipleInstances_shouldBeIndependent() {
        UI ui1 = new UI();
        UI ui2 = new UI();
        UI ui3 = new UI();
        
        assertNotNull(ui1);
        assertNotNull(ui2);
        assertNotNull(ui3);
        
        assertNotSame(ui1, ui2);
        assertNotSame(ui1, ui3);
        assertNotSame(ui2, ui3);
    }
}

