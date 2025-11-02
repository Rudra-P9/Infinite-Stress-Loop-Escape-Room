package com.escape.model;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AudioPuzzleTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private AudioPuzzle puzzle;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        puzzle = new AudioPuzzle("A1", "Listen", "Hear the sound", "echo", "audio", "decipher");
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void constructorShouldSetDefaultAudioPathWhenNull() {
        AudioPuzzle p = new AudioPuzzle("A2", "Test", "Obj", "sol", "cat", "type");
        assertEquals("audio/varenprojectescapeaudio.wav", p.getAudioPath());
    }

    @Test
    public void constructorShouldSetProvidedAudioPath() {
        assertEquals("audio/varenprojectescapeaudio.wav", puzzle.getAudioPath());
    }

    @Test
    public void checkAnswerShouldIgnoreCaseAndSpaces() {
        puzzle.setSolution("ECHO");
        assertTrue(puzzle.checkAnswer("echo"));
        assertTrue(puzzle.checkAnswer(" e c h o "));
    }

    @Test
    public void checkAnswerShouldReturnFalseForIncorrectAnswer() {
        puzzle.setSolution("echo");
        assertFalse(puzzle.checkAnswer("wrong"));
    }

    @Test
    public void checkAnswerShouldReturnFalseForNullAnswer() {
        puzzle.setSolution("echo");
        assertFalse(puzzle.checkAnswer(null));
    }

    @Test
    public void checkAnswerShouldReturnFalseWhenSolutionIsNull() {
        puzzle.setSolution(null);
        assertFalse(puzzle.checkAnswer("echo"));
    }

    @Test
    public void playAudioShouldPrintStartAndStopMessages() {
        puzzle.playAudio();
        String output = outContent.toString();
        assertTrue(output.contains("The terminal begins to play a robotic audio..."));
        assertTrue(output.contains("The terminal audio stops."));
    }
}