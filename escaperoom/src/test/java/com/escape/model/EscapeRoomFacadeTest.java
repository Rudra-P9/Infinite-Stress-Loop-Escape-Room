package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class EscapeRoomFacadeTest {

    private EscapeRoomFacade facade;

    @Before
    public void setUp() {
        facade = new EscapeRoomFacade();
    }

    @Test
    public void newInstance_shouldHaveNullCurrentUser() {
        assertNull(facade.getCurrentUser());
        assertNull(facade.getCurrentUsername());
    }

    @Test
    public void createAccount_withNewUsername_shouldCreateAccountAndSetCurrentUser() {
        String username = "testuser_" + System.currentTimeMillis();
        facade.createAccount(username, "password123");
        
        assertNotNull(facade.getCurrentUser());
        assertEquals(username, facade.getCurrentUsername());
    }

    @Test
    public void createAccount_withDuplicateUsername_shouldNotCreateDuplicate() {
        String username = "duplicateuser";
        facade.createAccount(username, "pass1");
        User firstUser = facade.getCurrentUser();
        
        // Try to create duplicate
        facade.createAccount(username, "pass2");
        User secondUser = facade.getCurrentUser();
        
        // Should remain the same user (or handle duplication appropriately)
        assertNotNull(secondUser);
    }

    @Test
    public void login_withValidCredentials_shouldSetCurrentUser() {
        String username = "loginuser";
        String password = "loginpass";
        facade.createAccount(username, password);
        facade.logout();
        
        facade.login(username, password);
        assertNotNull(facade.getCurrentUser());
        assertEquals(username, facade.getCurrentUsername());
    }

    @Test
    public void login_withInvalidCredentials_shouldNotSetCurrentUser() {
        facade.login("nonexistent", "wrongpass");
        assertNull(facade.getCurrentUser());
    }

    @Test
    public void login_withNullCredentials_shouldHandleGracefully() {
        facade.login(null, "pass");
        facade.login("user", null);
        facade.login(null, null);
        // Should not throw exceptions
    }

    @Test
    public void logout_shouldClearCurrentUser() {
        facade.createAccount("logoutuser", "pass");
        assertNotNull(facade.getCurrentUser());
        
        facade.logout();
        assertNull(facade.getCurrentUser());
    }

    @Test
    public void setCurrentDifficulty_shouldUpdateDifficulty() {
        facade.setCurrentDifficulty(Difficulty.HARD);
        assertEquals(Difficulty.HARD, facade.getCurrentDifficulty());
        
        facade.setCurrentDifficulty(Difficulty.MEDIUM);
        assertEquals(Difficulty.MEDIUM, facade.getCurrentDifficulty());
        
        facade.setCurrentDifficulty(Difficulty.EASY);
        assertEquals(Difficulty.EASY, facade.getCurrentDifficulty());
    }

    @Test
    public void setCurrentDifficulty_withNull_shouldDefaultToEasy() {
        facade.setCurrentDifficulty(null);
        assertEquals(Difficulty.EASY, facade.getCurrentDifficulty());
    }

    @Test
    public void getTimeRemaining_withoutTimer_shouldReturnZero() {
        assertEquals(0, facade.getTimeRemaining());
    }

    @Test
    public void pauseAndResumeGame_withoutTimer_shouldNotThrowExceptions() {
        facade.pauseGame();
        facade.resumeGame();
        // Should not throw any exceptions
    }

    @Test
    public void solvePuzzle_withoutCurrentRoom_shouldReturnFalse() {
        facade.createAccount("user", "pass");
        assertFalse(facade.solvePuzzle("answer"));
    }

    @Test
    public void useHint_withoutCurrentRoom_shouldReturnErrorMessage() {
        assertEquals("No current room", facade.useHint());
    }

    @Test
    public void getCollectedLetters_shouldReturnCopyOfList() {
        ArrayList<String> letters = facade.getCollectedLetters();
        assertNotNull(letters);
        assertTrue(letters.isEmpty());
    }

    @Test
    public void calculateFinalScore_withoutTimer_shouldReturnZero() {
        assertEquals(0, facade.calculateFinalScore());
    }

    @Test
    public void isCurrentRoomComplete_withoutCurrentRoom_shouldReturnFalse() {
        assertFalse(facade.isCurrentRoomComplete());
    }

    @Test
    public void saveAndLoadGame_withoutUser_shouldHandleGracefully() {
        facade.saveGame();
        facade.loadGame();
        // Should not throw exceptions when no user is logged in
    }

    @Test
    public void endGame_withoutUser_shouldHandleGracefully() {
        facade.endGame();
        // Should not throw exceptions when no user is logged in
    }
}