package com.escape.model;

import org.junit.Test;

public class AudioPlayerTest {

    @Test
    public void play_withValidAudioFile_shouldExecuteWithoutException() {
        // This test verifies that playing a valid audio file doesn't throw exceptions
        // Note: This may fail if the audio file doesn't exist, but the method should handle it gracefully
        AudioPlayer.play("audio/varenprojectescapeaudio.wav");
        // No assertion needed - test passes if no exception is thrown
    }

    @Test
    public void play_withNonExistentFile_shouldHandleGracefullyWithoutException() {
        // Test that non-existent files don't crash the application
        AudioPlayer.play("nonexistent/file.wav");
        // Should print error message but not throw exception
    }

    @Test
    public void play_withNullPath_shouldHandleGracefully() {
        // Test null path handling
        AudioPlayer.play(null);
        // Should handle null without throwing NPE
    }

    @Test
    public void play_withEmptyPath_shouldHandleGracefully() {
        // Test empty path handling
        AudioPlayer.play("");
        // Should handle empty string without throwing exception
    }

    @Test
    public void play_withPathStartingWithSlash_shouldProcessCorrectly() {
        // Test path normalization with leading slash
        AudioPlayer.play("/audio/varenprojectescapeaudio.wav");
        // Should remove leading slash and proceed
    }
}