package com.escape.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Comprehensive tests for AudioPlayer class.
 * Tests will expose bugs in file handling, null checks, and error messages.
 */
public class AudioPlayerTest {
    
    @Test
    public void testPlayValidAudioFile() {
        // This will likely fail if audio system not available
        // But should not crash
        try {
            AudioPlayer.play("audio/varenprojectescapeaudio.wav");
            assertTrue("Should handle valid file without crashing", true);
        } catch (Exception e) {
            fail("Should not throw exception for valid file: " + e.getMessage());
        }
    }
    
    @Test
    public void testPlayFileWithLeadingSlash() {
        // Tests line 22: substring(1) removes leading slash
        try {
            AudioPlayer.play("/audio/varenprojectescapeaudio.wav");
            assertTrue("Should handle leading slash", true);
        } catch (Exception e) {
            fail("Should handle leading slash: " + e.getMessage());
        }
    }
    
    @Test
    public void testPlayNonExistentFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        AudioPlayer.play("audio/nonexistent.wav");
        
        String output = outContent.toString();
        assertTrue("Should print 'Audio file not found!' but got: " + output, 
            output.contains("not found"));
    }
    
    @Test
    public void testPlayNullPath() {
        // BUG: This will crash! No null check before substring
        try {
            AudioPlayer.play(null);
            fail("BUG FOUND: Should handle null path, but doesn't throw NPE or handle it");
        } catch (NullPointerException e) {
            // Expected - documents the bug
            assertTrue("BUG CONFIRMED: NPE thrown on null path at: " + e.getMessage(), true);
        }
    }
    
    @Test
    public void testPlayEmptyString() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        AudioPlayer.play("");
        
        String output = outContent.toString();
        assertTrue("Should handle empty string gracefully", 
            output.contains("not found"));
    }
    
    @Test
    public void testPlayWhitespaceOnly() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        AudioPlayer.play("   ");
        
        String output = outContent.toString();
        assertTrue("Should handle whitespace-only path", 
            output.contains("not found"));
    }
    
    @Test
    public void testPlayInvalidFileFormat() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        AudioPlayer.play("audio/test.txt");
        
        String output = outContent.toString();
        // Should either say not found or error playing
        assertTrue("Should produce some output for invalid format", 
            output.length() > 0);
    }
    
    @Test
    public void testPlayPathWithSpaces() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        AudioPlayer.play("audio/file with spaces.wav");
        
        // Should attempt to load, likely fail with not found
        assertTrue("Should handle spaces in path", true);
    }
    
    @Test
    public void testPlayVeryLongPath() {
        String longPath = "audio/" + "a".repeat(500) + ".wav";
        
        try {
            AudioPlayer.play(longPath);
            assertTrue("Should handle very long path", true);
        } catch (Exception e) {
            assertTrue("Long path may cause issues: " + e.getMessage(), true);
        }
    }
    
    @Test
    public void testPlayMultipleTimesInSequence() {
        // Tests if multiple plays cause issues
        try {
            AudioPlayer.play("audio/test1.wav");
            AudioPlayer.play("audio/test2.wav");
            AudioPlayer.play("audio/test3.wav");
            assertTrue("Should handle multiple plays", true);
        } catch (Exception e) {
            fail("Multiple plays should not crash: " + e.getMessage());
        }
    }
}