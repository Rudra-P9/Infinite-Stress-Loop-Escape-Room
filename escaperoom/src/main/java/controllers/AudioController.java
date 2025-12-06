package controllers;

import com.escape.model.AudioPlayer;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Centralized audio controller for UI interactions and game sounds.
 * Provides easy-to-use methods for playing sound effects and background music
 * using the existing AudioPlayer infrastructure.
 * 
 * @author Infinite Stress Loop Team
 */
public class AudioController {
    
    private static AudioController instance;
    
    // Volume controls (0.0 to 1.0)
    private float masterVolume = 0.7f;
    private float musicVolume = 0.6f;
    private float sfxVolume = 0.8f;
    
    // State controls
    private boolean musicEnabled = true;
    private boolean sfxEnabled = true;
    
    // Background music
    private Clip backgroundClip;
    private String currentMusicPath;
    
    /**
     * Private constructor for singleton pattern.
     */
    private AudioController() {
        // Singleton instance
    }
    
    /**
     * Gets the singleton instance of AudioController.
     * 
     * @return The AudioController instance
     */
    public static AudioController getInstance() {
        if (instance == null) {
            instance = new AudioController();
        }
        return instance;
    }
    
    /**
     * Plays background music in a loop.
     * This uses a non-blocking approach so the game continues.
     * 
     * @param resourcePath Path to audio file (e.g., "audio/varenprojectescapeaudio.wav")
     */
    public void playBackgroundMusic(String resourcePath) {
        if (!musicEnabled) {
            return;
        }
        
        // Stop current music if playing
        stopBackgroundMusic();
        
        new Thread(() -> {
            try {
                String cleanPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                InputStream fileStream = AudioController.class.getResourceAsStream("/" + cleanPath);
                
                if (fileStream == null) {
                    System.out.println("[AudioController] Music file not found: " + resourcePath);
                    return;
                }
                
                BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioStream);
                
                // Set volume
                setClipVolume(backgroundClip, masterVolume * musicVolume);
                
                // Loop continuously
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                currentMusicPath = resourcePath;
                
                System.out.println("[AudioController] Background music started: " + resourcePath);
            } catch (Exception e) {
                System.out.println("[AudioController] Error playing background music: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * Pauses the background music.
     */
    public void pauseBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            System.out.println("[AudioController] Background music paused");
        }
    }
    
    /**
     * Resumes the background music.
     */
    public void resumeBackgroundMusic() {
        if (backgroundClip != null && !backgroundClip.isRunning() && musicEnabled) {
            backgroundClip.start();
            System.out.println("[AudioController] Background music resumed");
        }
    }
    
    /**
     * Stops the background music completely.
     */
    public void stopBackgroundMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
            currentMusicPath = null;
            System.out.println("[AudioController] Background music stopped");
        }
    }
    
    /**
     * Plays a sound effect without blocking.
     * Perfect for button clicks, hovers, etc.
     * 
     * @param resourcePath Path to audio file (e.g., "audio/click.wav")
     */
    public void playSoundEffect(String resourcePath) {
        if (!sfxEnabled) {
            return;
        }
        
        // Don't try to play if no path provided
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            return;
        }
        
        new Thread(() -> {
            try {
                String cleanPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                InputStream fileStream = AudioController.class.getResourceAsStream("/" + cleanPath);
                
                if (fileStream == null) {
                    System.out.println("[AudioController] Sound effect not found: " + resourcePath);
                    return;
                }
                
                BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                // Set volume
                setClipVolume(clip, masterVolume * sfxVolume);
                
                // Play once and cleanup
                clip.start();
                
                // Wait for completion then cleanup
                clip.addLineListener(event -> {
                    if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                        clip.close();
                        try {
                            audioStream.close();
                            bufferedStream.close();
                        } catch (Exception e) {
                            // Ignore cleanup errors
                        }
                    }
                });
                
            } catch (Exception e) {
                System.out.println("[AudioController] Error playing sound effect: " + e.getMessage());
            }
        }).start();
    }
    
    /**
     * Plays a blocking audio (waits for completion).
     * Uses the original AudioPlayer implementation.
     * 
     * @param resourcePath Path to audio file
     */
    public void playBlocking(String resourcePath) {
        if (!sfxEnabled) {
            return;
        }
        AudioPlayer.play(resourcePath);
    }
    
    /**
     * Sets the volume for a clip.
     * 
     * @param clip The clip to adjust
     * @param volume Volume level (0.0 to 1.0)
     */
    private void setClipVolume(Clip clip, float volume) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(Math.max(0.0001f, volume)) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        } catch (Exception e) {
            // Volume control not available
        }
    }
    
    // ==================== Convenience Methods ====================
    
    /**
     * Plays a button click sound.
     * Uses the available audio file for now.
     */
    public void playButtonClick() {
        playSoundEffect("audio/button-click.wav");
    }
    
    /**
     * Plays a button hover sound.
     * Uses the available audio file for now.
     */
    public void playButtonHover() {
        playSoundEffect("audio/button-click.wav");
    }
    
    /**
     * Plays a puzzle solve success sound.
     */
    public void playPuzzleSolve() {
        playSoundEffect("");
    }
    
    /**
     * Plays a puzzle failure sound.
     */
    public void playPuzzleFail() {
        playSoundEffect("");
    }
    
    /**
     * Plays an item collection sound.
     */
    public void playItemCollect() {
        playSoundEffect("");
    }
    
    /**
     * Plays a door opening sound.
     */
    public void playDoorOpen() {
        playSoundEffect("");
    }
    
    /**
     * Plays a hint sound.
     */
    public void playHint() {
        playSoundEffect("");
    }
    
    /**
     * Plays an error sound.
     */
    public void playError() {
        playSoundEffect("");
    }
    
    // ==================== Settings ====================
    
    /**
     * Sets the master volume (0.0 to 1.0).
     * 
     * @param volume Volume level
     */
    public void setMasterVolume(float volume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, volume));
        updateBackgroundMusicVolume();
    }
    
    /**
     * Sets the music volume (0.0 to 1.0).
     * 
     * @param volume Volume level
     */
    public void setMusicVolume(float volume) {
        this.musicVolume = Math.max(0.0f, Math.min(1.0f, volume));
        updateBackgroundMusicVolume();
    }
    
    /**
     * Sets the sound effects volume (0.0 to 1.0).
     * 
     * @param volume Volume level
     */
    public void setSfxVolume(float volume) {
        this.sfxVolume = Math.max(0.0f, Math.min(1.0f, volume));
    }
    
    /**
     * Updates the volume of currently playing background music.
     */
    private void updateBackgroundMusicVolume() {
        if (backgroundClip != null) {
            setClipVolume(backgroundClip, masterVolume * musicVolume);
        }
    }
    
    /**
     * Toggles music on/off.
     * 
     * @return New state (true if enabled)
     */
    public boolean toggleMusic() {
        musicEnabled = !musicEnabled;
        if (!musicEnabled) {
            pauseBackgroundMusic();
        } else if (currentMusicPath != null) {
            playBackgroundMusic(currentMusicPath);
        }
        return musicEnabled;
    }
    
    /**
     * Toggles sound effects on/off.
     * 
     * @return New state (true if enabled)
     */
    public boolean toggleSfx() {
        sfxEnabled = !sfxEnabled;
        return sfxEnabled;
    }
    
    /**
     * Enables or disables music.
     * 
     * @param enabled Whether music should be enabled
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            pauseBackgroundMusic();
        } else if (currentMusicPath != null) {
            playBackgroundMusic(currentMusicPath);
        }
    }
    
    /**
     * Enables or disables sound effects.
     * 
     * @param enabled Whether sound effects should be enabled
     */
    public void setSfxEnabled(boolean enabled) {
        this.sfxEnabled = enabled;
    }
    
    /**
     * Checks if music is enabled.
     * 
     * @return True if music is enabled
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    
    /**
     * Checks if sound effects are enabled.
     * 
     * @return True if sound effects are enabled
     */
    public boolean isSfxEnabled() {
        return sfxEnabled;
    }
    
    /**
     * Gets current master volume.
     * 
     * @return Master volume (0.0 to 1.0)
     */
    public float getMasterVolume() {
        return masterVolume;
    }
    
    /**
     * Gets current music volume.
     * 
     * @return Music volume (0.0 to 1.0)
     */
    public float getMusicVolume() {
        return musicVolume;
    }
    
    /**
     * Gets current sound effects volume.
     * 
     * @return SFX volume (0.0 to 1.0)
     */
    public float getSfxVolume() {
        return sfxVolume;
    }
    
    /**
     * Cleans up all audio resources.
     * Call this when shutting down.
     */
    public void shutdown() {
        stopBackgroundMusic();
        System.out.println("[AudioController] Shutdown complete");
    }
}
