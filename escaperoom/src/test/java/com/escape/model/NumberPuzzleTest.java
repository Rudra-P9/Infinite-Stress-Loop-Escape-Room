package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberPuzzleTest {

    private NumberPuzzle numberPuzzle;

    @Before
    public void setUp() {
        numberPuzzle = new NumberPuzzle(
            "NUM-001",
            "Math Challenge",
            "Solve the equation: 2 + 2",
            "4.0",
            "NUMBER",
            "MATH"
        );
    }

    @Test
    public void constructor_shouldSetFieldsCorrectly() {
        assertEquals("NUM-001", numberPuzzle.getPuzzleID());
        assertEquals("Math Challenge", numberPuzzle.getTitle());
        assertEquals("Solve the equation: 2 + 2", numberPuzzle.getObjective());
        assertEquals("4.0", numberPuzzle.getSolution());
    }

    @Test
    public void checkAnswer_withExactNumericMatch_shouldReturnTrue() {
        assertTrue(numberPuzzle.checkAnswer("4.0"));
    }

    @Test
    public void checkAnswer_withCloseNumericValueWithinTolerance_shouldReturnTrue() {
        assertTrue(numberPuzzle.checkAnswer("4.00005"));
        assertTrue(numberPuzzle.checkAnswer("3.99995"));
    }

    @Test
    public void checkAnswer_withDifferentNumericValue_shouldReturnFalse() {
        assertFalse(numberPuzzle.checkAnswer("5.0"));
        assertFalse(numberPuzzle.checkAnswer("3.0"));
    }

    @Test
    public void checkAnswer_withNonNumericInput_shouldReturnFalse() {
        assertFalse(numberPuzzle.checkAnswer("four"));
        assertFalse(numberPuzzle.checkAnswer("4.0.0"));
        assertFalse(numberPuzzle.checkAnswer("abc"));
    }

    @Test
    public void checkAnswer_withNullInput_shouldReturnFalse() {
        assertFalse(numberPuzzle.checkAnswer(null));
    }

    @Test
    public void checkAnswer_withWhitespace_shouldParseCorrectly() {
        assertTrue(numberPuzzle.checkAnswer("  4.0  "));
        assertFalse(numberPuzzle.checkAnswer("  5.0  "));
    }

    @Test
    public void checkAnswer_withIntegerSolution_shouldWorkWithDecimalInput() {
        NumberPuzzle intPuzzle = new NumberPuzzle("ID", "T", "O", "42", "NUMBER", "TYPE");
        assertTrue(intPuzzle.checkAnswer("42.0"));
        assertTrue(intPuzzle.checkAnswer("42"));
    }

    @Test
    public void checkAnswer_withDecimalSolution_shouldWorkWithIntegerInput() {
        NumberPuzzle decimalPuzzle = new NumberPuzzle("ID", "T", "O", "3.14", "NUMBER", "TYPE");
        assertTrue(decimalPuzzle.checkAnswer("3.14"));
        assertFalse(decimalPuzzle.checkAnswer("3"));
    }

    @Test
    public void getSolution_shouldReturnCurrentSolution() {
        assertEquals("4.0", numberPuzzle.getSolution());
    }

    @Test
    public void setSolution_shouldUpdateSolution() {
        numberPuzzle.setSolution("99.9");
        assertEquals("99.9", numberPuzzle.getSolution());
    }

    @Test
    public void setSolution_withNull_shouldSetNull() {
        numberPuzzle.setSolution(null);
        assertNull(numberPuzzle.getSolution());
    }

    @Test
    public void setSolution_withWhitespace_shouldStoreAsIs() {
        numberPuzzle.setSolution("  123  ");
        assertEquals("  123  ", numberPuzzle.getSolution());
    }
}