package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class PuzzleTest {

    private WordPuzzle wordPuzzle; // Using concrete subclass for testing

    @Before
    public void setUp() {
        wordPuzzle = new WordPuzzle(
            "WORD-001",
            "Word Challenge",
            "Solve the word puzzle",
            "SOLUTION",
            "WORD",
            "RIDDLE"
        );
    }

    @Test
    public void getPuzzleID_shouldReturnConstructorValue() {
        assertEquals("WORD-001", wordPuzzle.getPuzzleID());
    }

    @Test
    public void setPuzzleID_shouldUpdateID() {
        wordPuzzle.setPuzzleID("NEW-ID");
        assertEquals("NEW-ID", wordPuzzle.getPuzzleID());
    }

    @Test
    public void getTitle_shouldReturnConstructorValue() {
        assertEquals("Word Challenge", wordPuzzle.getTitle());
    }

    @Test
    public void setTitle_shouldUpdateTitle() {
        wordPuzzle.setTitle("New Title");
        assertEquals("New Title", wordPuzzle.getTitle());
    }

    @Test
    public void getObjective_shouldReturnConstructorValue() {
        assertEquals("Solve the word puzzle", wordPuzzle.getObjective());
    }

    @Test
    public void setObjective_shouldUpdateObjective() {
        wordPuzzle.setObjective("New Objective");
        assertEquals("New Objective", wordPuzzle.getObjective());
    }

    @Test
    public void solved_initiallyFalse_shouldReturnFalse() {
        assertFalse(wordPuzzle.solved());
    }

    @Test
    public void setSolved_shouldUpdateSolvedStatus() {
        wordPuzzle.setSolved(true);
        assertTrue(wordPuzzle.solved());
        
        wordPuzzle.setSolved(false);
        assertFalse(wordPuzzle.solved());
    }

    @Test
    public void getDifficulty_defaultNull_shouldReturnNull() {
        assertNull(wordPuzzle.getDifficulty());
    }

    @Test
    public void setDifficulty_shouldUpdateDifficulty() {
        wordPuzzle.setDifficulty(Difficulty.HARD);
        assertEquals(Difficulty.HARD, wordPuzzle.getDifficulty());
    }

    @Test
    public void setDifficulty_withNull_shouldThrowException() {
        try {
            wordPuzzle.setDifficulty(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected behavior
        }
    }

    @Test
    public void getPrompt_defaultNull_shouldReturnNull() {
        assertNull(wordPuzzle.getPrompt());
    }

    @Test
    public void setPrompt_shouldUpdatePrompt() {
        wordPuzzle.setPrompt("Enter your answer:");
        assertEquals("Enter your answer:", wordPuzzle.getPrompt());
    }

    @Test
    public void getRewardLetter_defaultNull_shouldReturnNull() {
        assertNull(wordPuzzle.getRewardLetter());
    }

    @Test
    public void setRewardLetter_shouldUpdateRewardLetter() {
        wordPuzzle.setRewardLetter("A");
        assertEquals("A", wordPuzzle.getRewardLetter());
    }

    @Test
    public void getHint_withNoHints_shouldReturnDefaultMessage() {
        assertEquals("No hint for this puzzle!", wordPuzzle.getHint());
    }

    @Test
    public void getHint_withSimpleHint_shouldReturnHintText() {
        wordPuzzle.setSimpleHint("Think about the first letter");
        assertEquals("Think about the first letter", wordPuzzle.getHint());
    }

    @Test
    public void peekHint_withInvalidIndex_shouldReturnNull() {
        assertNull(wordPuzzle.peekHint(-1));
        assertNull(wordPuzzle.peekHint(0));
        assertNull(wordPuzzle.peekHint(100));
    }

    @Test
    public void getHintCount_withNoHints_shouldReturnZero() {
        assertEquals(0, wordPuzzle.getHintCount());
    }

    @Test
    public void getRevealedHintCount_withNoHints_shouldReturnZero() {
        assertEquals(0, wordPuzzle.getRevealedHintCount());
    }

    @Test
    public void hasHintsRemaining_withNoHints_shouldCheckSimpleHint() {
        wordPuzzle.setSimpleHint("Simple hint");
        assertTrue(wordPuzzle.hasHintsRemaining());
        
        WordPuzzle noHintPuzzle = new WordPuzzle("ID", "T", "O", "S", "WORD", "TYPE");
        assertFalse(noHintPuzzle.hasHintsRemaining());
    }

    @Test
    public void addHint_shouldAddToHintsList() {
        Hints hint = new Hints("hint1", 1, false, "This is a hint");
        wordPuzzle.addHint(hint);
        
        List<Hints> hints = wordPuzzle.getHints();
        assertEquals(1, hints.size());
        assertEquals(hint, hints.get(0));
    }

    @Test
    public void addHint_withNull_shouldNotAddToHints() {
        wordPuzzle.addHint(null);
        assertEquals(0, wordPuzzle.getHintCount());
    }

    @Test
    public void resetHints_shouldSetAllHintsUnrevealed() {
        Hints hint1 = new Hints("h1", 1, true, "Hint 1");
        Hints hint2 = new Hints("h2", 2, true, "Hint 2");
        wordPuzzle.addHint(hint1);
        wordPuzzle.addHint(hint2);
        
        wordPuzzle.reserHints();
        
        for (Hints hint : wordPuzzle.getHints()) {
            assertFalse(hint.isRevealed());
        }
    }

    @Test
    public void getHintStatus_shouldReturnFormattedString() {
        String status = wordPuzzle.getHintStatus();
        assertTrue(status.contains("Math Challenge"));
        assertTrue(status.contains("Hints available: 0"));
    }
}