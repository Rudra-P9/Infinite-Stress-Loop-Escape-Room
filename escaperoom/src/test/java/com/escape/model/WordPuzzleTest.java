package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WordPuzzleTest {

    private WordPuzzle wordPuzzle;

    @Before
    public void setUp() {
        wordPuzzle = new WordPuzzle(
            "WORD-001",
            "Vocabulary Test",
            "Unscramble the letters: R A E C",
            "RACE",
            "WORD",
            "UNSCRAMBLE"
        );
    }

    @Test
    public void constructor_shouldSetFieldsCorrectly() {
        assertEquals("WORD-001", wordPuzzle.getPuzzleID());
        assertEquals("Vocabulary Test", wordPuzzle.getTitle());
        assertEquals("Unscramble the letters: R A E C", wordPuzzle.getObjective());
        assertEquals("RACE", wordPuzzle.getSolution());
    }

    @Test
    public void checkAnswer_forRiddleType_withExactMatch_shouldReturnTrue() {
        WordPuzzle riddle = new WordPuzzle("ID", "T", "O", "ANSWER", "WORD", "RIDDLE");
        assertTrue(riddle.checkAnswer("ANSWER"));
    }

    @Test
    public void checkAnswer_forLetterDecipherType_withNormalizedMatch_shouldReturnTrue() {
        WordPuzzle decipher = new WordPuzzle("ID", "T", "O", "SECRET", "WORD", "LETTER_DECIPHER");
        assertTrue(decipher.checkAnswer("secret"));
        assertTrue(decipher.checkAnswer("  SECRET  "));
    }

    @Test
    public void checkAnswer_forArrowDecipherType_withNormalizedMatch_shouldReturnTrue() {
        WordPuzzle arrow = new WordPuzzle("ID", "T", "O", "CODE", "WORD", "ARROW_DECIPHER");
        assertTrue(arrow.checkAnswer("code"));
        assertTrue(arrow.checkAnswer("CODE"));
    }

    @Test
    public void checkAnswer_forFinalLockType_withNormalizedMatch_shouldReturnTrue() {
        WordPuzzle finalLock = new WordPuzzle("ID", "T", "O", "FINAL", "WORD", "FINAL_LOCK");
        assertTrue(finalLock.checkAnswer("final"));
        assertTrue(finalLock.checkAnswer("FINAL"));
    }

    @Test
    public void checkAnswer_withIncorrectAnswer_shouldReturnFalse() {
        assertFalse(wordPuzzle.checkAnswer("CARE")); // Different word
        assertFalse(wordPuzzle.checkAnswer("RACES")); // Extra letter
        assertFalse(wordPuzzle.checkAnswer("RAC")); // Missing letter
    }

    @Test
    public void checkAnswer_withNullInput_shouldReturnFalse() {
        assertFalse(wordPuzzle.checkAnswer(null));
    }

    @Test
    public void checkAnswer_withNullSolution_shouldReturnFalse() {
        WordPuzzle nullSolution = new WordPuzzle("ID", "T", "O", null, "WORD", "RIDDLE");
        assertFalse(nullSolution.checkAnswer("ANYTHING"));
    }

    @Test
    public void checkAnswer_forUnknownType_shouldReturnFalse() {
        WordPuzzle unknownType = new WordPuzzle("ID", "T", "O", "SOLUTION", "WORD", "UNKNOWN_TYPE");
        assertFalse(unknownType.checkAnswer("solution"));
    }

    @Test
    public void getSolution_shouldReturnCurrentSolution() {
        assertEquals("RACE", wordPuzzle.getSolution());
    }

    @Test
    public void setSolution_shouldUpdateSolution() {
        wordPuzzle.setSolution("NEWWORD");
        assertEquals("NEWWORD", wordPuzzle.getSolution());
    }

    @Test
    public void setSolution_withNull_shouldSetNull() {
        wordPuzzle.setSolution(null);
        assertNull(wordPuzzle.getSolution());
    }

    @Test
    public void fixMethod_shouldNormalizeStrings() throws Exception {
        // Test private fix method via reflection
        java.lang.reflect.Method fixMethod = WordPuzzle.class.getDeclaredMethod("fix", String.class);
        fixMethod.setAccessible(true);
        
        String result1 = (String) fixMethod.invoke(wordPuzzle, "  Test  String  ");
        assertEquals("TESTSTRING", result1);
        
        String result2 = (String) fixMethod.invoke(wordPuzzle, "Mixed CASE");
        assertEquals("MIXEDCASE", result2);
        
        String result3 = (String) fixMethod.invoke(wordPuzzle, "a b c");
        assertEquals("ABC", result3);
    }
}