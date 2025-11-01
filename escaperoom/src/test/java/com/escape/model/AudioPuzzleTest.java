package com.escape.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Comprehensive tests for AudioPuzzle class.
 * Tests will expose bugs in answer validation and null handling.
 */
public class AudioPuzzleTest {
    
    private AudioPuzzle puzzle;
    
    @Before
    public void setUp() {
        puzzle = new AudioPuzzle(
            "AP-001",
            "Test Audio Puzzle",
            "Listen and identify",
            "MIRROR",
            "AUDIO",
            "AUDIO_DECIPHER",
            "/audio/test.wav"
        );
    }
    
    @Test
    public void testCheckAnswerValidExactMatch() {
        assertTrue("Should accept exact match", puzzle.checkAnswer("MIRROR"));
    }
    
    @Test
    public void testCheckAnswerValidLowercase() {
        assertTrue("Should be case-insensitive", puzzle.checkAnswer("mirror"));
    }
    
    @Test
    public void testCheckAnswerValidMixedCase() {
        assertTrue("Should accept mixed case", puzzle.checkAnswer("MiRrOr"));
    }
    
    @Test
    public void testCheckAnswerWithSpaces() {
        assertTrue("BUG FOUND: Spaces are removed, 'MIR ROR' matches 'MIRROR' - is this intended?", 
            puzzle.checkAnswer("MIR ROR"));
    }
    
    @Test
    public void testCheckAnswerWithMultipleSpaces() {
        assertTrue("BUG: All spaces removed before comparison", 
            puzzle.checkAnswer("M I R R O R"));
    }
    
    @Test
    public void testCheckAnswerWithTabs() {
        assertTrue("BUG: Tabs are removed too (\\s+ includes all whitespace)", 
            puzzle.checkAnswer("MIR\tROR"));
    }
    
    @Test
    public void testCheckAnswerNull() {
        assertFalse("Should return false for null answer", puzzle.checkAnswer(null));
    }
    
    @Test
    public void testCheckAnswerEmpty() {
        assertFalse("Should return false for empty answer", puzzle.checkAnswer(""));
    }
    
    @Test
    public void testCheckAnswerWhitespaceOnly() {
        assertFalse("Whitespace-only should not match non-empty solution", 
            puzzle.checkAnswer("   "));
    }
    
    @Test
    public void testCheckAnswerWrongAnswer() {
        assertFalse("Should reject wrong answer", puzzle.checkAnswer("ECHO"));
    }
    
    @Test
    public void testCheckAnswerPartialMatch() {
        assertFalse("Should reject partial answer", puzzle.checkAnswer("MIR"));
    }
    
    @Test
    public void testGetSolution() {
        assertEquals("Should return correct solution", "MIRROR", puzzle.getSolution());
    }
    
    @Test
    public void testSetSolution() {
        puzzle.setSolution("ECHO");
        assertEquals("Should update solution", "ECHO", puzzle.getSolution());
        assertTrue("Should accept new solution", puzzle.checkAnswer("ECHO"));
    }
    
    @Test
    public void testSetSolutionNull() {
        puzzle.setSolution(null);
        assertNull("Should allow null solution", puzzle.getSolution());
        assertFalse("Should return false when solution is null", puzzle.checkAnswer("MIRROR"));
    }
    
    @Test
    public void testGetAudioPath() {
        assertEquals("Should return correct audio path", "/audio/test.wav", puzzle.getAudioPath());
    }
    
    @Test
    public void testDefaultConstructorPath() {
        AudioPuzzle defaultPuzzle = new AudioPuzzle(
            "AP-002",
            "Default",
            "Test",
            "TEST",
            "AUDIO",
            "AUDIO_DECIPHER"
        );
        
        assertEquals("Should use default audio path", "audio/varenprojectescapeaudio.wav", defaultPuzzle.getAudioPath());
    }
    
    @Test
    public void testPlayAudioDoesNotCrash() {
        try {
            puzzle.playAudio();
            assertTrue("playAudio should not crash", true);
        } catch (Exception e) {
            fail("playAudio threw exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testCheckAnswerWithNumbers() {
        puzzle.setSolution("TEST123");
        assertTrue("Should handle numbers in solution", puzzle.checkAnswer("test123"));
    }
    
    @Test
    public void testCheckAnswerWithNumbersAndSpaces() {
        puzzle.setSolution("TEST123");
        assertTrue("BUG: Spaces removed even with numbers", puzzle.checkAnswer("TEST 123"));
    }
    
    @Test
    public void testCheckAnswerWithSpecialCharacters() {
        puzzle.setSolution("TEST-ANSWER");
        assertTrue("Should handle hyphens", puzzle.checkAnswer("test-answer"));
        assertFalse("Hyphen is not removed, so should not match without it", 
            puzzle.checkAnswer("testanswer"));
    }
    
    @Test
    public void testCheckAnswerVeryLongString() {
        String longAnswer = "A".repeat(10000);
        assertFalse("Should handle very long strings", 
            puzzle.checkAnswer(longAnswer));
    }
    
    @Test
    public void testSolutionWithSpaces() {
        puzzle.setSolution("WORD PUZZLE");
        assertTrue("BUG: Solution spaces are removed, so 'WORD PUZZLE' matches 'WORDPUZZLE'", 
            puzzle.checkAnswer("WORDPUZZLE"));
    }
    
    @Test
    public void testCheckAnswerLeadingTrailingSpaces() {
        assertTrue("Should trim leading/trailing spaces", puzzle.checkAnswer("  MIRROR  "));
    }
}