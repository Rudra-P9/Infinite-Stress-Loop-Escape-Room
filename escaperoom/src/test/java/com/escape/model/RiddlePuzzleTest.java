package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RiddlePuzzleTest {

    private RiddlePuzzle riddlePuzzle;

    @Before
    public void setUp() {
        riddlePuzzle = new RiddlePuzzle(
            "RIDDLE-001",
            "Ancient Riddle",
            "What has keys but can't open locks?",
            "PIANO",
            Difficulty.MEDIUM
        );
    }

    @Test
    public void explicitConstructor_shouldSetFieldsCorrectly() {
        assertEquals("RIDDLE-001", riddlePuzzle.getPuzzleID());
        assertEquals("Ancient Riddle", riddlePuzzle.getTitle());
        assertEquals("What has keys but can't open locks?", riddlePuzzle.getObjective());
        assertEquals("PIANO", riddlePuzzle.getSolution());
        assertEquals(Difficulty.MEDIUM, riddlePuzzle.getDifficulty());
    }

    @Test
    public void defaultConstructor_shouldCreateValidInstance() {
        RiddlePuzzle defaultRiddle = new RiddlePuzzle();
        assertNotNull(defaultRiddle);
        assertNotNull(defaultRiddle.getPuzzleID()); // Should have generated ID
    }

    @Test
    public void checkAnswer_withExactMatch_shouldReturnTrueAndMarkSolved() {
        assertTrue(riddlePuzzle.checkAnswer("PIANO"));
        assertTrue(riddlePuzzle.solved());
    }

    @Test
    public void checkAnswer_caseInsensitive_shouldReturnTrue() {
        assertTrue(riddlePuzzle.checkAnswer("piano"));
        assertTrue(riddlePuzzle.checkAnswer("Piano"));
        assertTrue(riddlePuzzle.checkAnswer("PiAnO"));
    }

    @Test
    public void checkAnswer_withWhitespace_shouldTrimAndReturnTrue() {
        assertTrue(riddlePuzzle.checkAnswer("  PIANO  "));
        assertTrue(riddlePuzzle.checkAnswer("PIANO "));
        assertTrue(riddlePuzzle.checkAnswer(" PIANO"));
    }

    @Test
    public void checkAnswer_withIncorrectAnswer_shouldReturnFalseAndNotMarkSolved() {
        assertFalse(riddlePuzzle.checkAnswer("KEYBOARD"));
        assertFalse(riddlePuzzle.solved());
    }

    @Test
    public void checkAnswer_withNullInput_shouldReturnFalse() {
        assertFalse(riddlePuzzle.checkAnswer(null));
    }

    @Test
    public void checkAnswer_withNullSolution_shouldReturnFalse() {
        RiddlePuzzle nullSolutionPuzzle = new RiddlePuzzle("ID", "T", "O", null, Difficulty.EASY);
        assertFalse(nullSolutionPuzzle.checkAnswer("ANYTHING"));
    }

    @Test
    public void getSolution_shouldReturnCurrentSolution() {
        assertEquals("PIANO", riddlePuzzle.getSolution());
    }

    @Test
    public void setSolution_shouldUpdateSolution() {
        riddlePuzzle.setSolution("KEYBOARD");
        assertEquals("KEYBOARD", riddlePuzzle.getSolution());
    }

    @Test
    public void setSolution_withNull_shouldSetEmptyString() {
        riddlePuzzle.setSolution(null);
        assertEquals("", riddlePuzzle.getSolution());
    }

    @Test
    public void setSolution_withWhitespace_shouldTrim() {
        riddlePuzzle.setSolution("  TEST  ");
        assertEquals("TEST", riddlePuzzle.getSolution());
    }

    @Test
    public void toString_shouldContainRelevantInformation() {
        String stringRepresentation = riddlePuzzle.toString();
        assertTrue(stringRepresentation.contains("Ancient Riddle"));
        assertTrue(stringRepresentation.contains("PIANO"));
        assertTrue(stringRepresentation.contains("solved=false"));
    }

    @Test
    public void toString_afterSolving_shouldShowSolvedStatus() {
        riddlePuzzle.checkAnswer("PIANO");
        String stringRepresentation = riddlePuzzle.toString();
        assertTrue(stringRepresentation.contains("solved=true"));
    }
}