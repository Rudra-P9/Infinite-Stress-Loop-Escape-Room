package com.escape.model;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speek {
    private static final String VOICE_NAME = "kevin16";
    private static final int MAX_CHARS = 20_000;

    // Track the current voice instance for cancellation
    private static Voice currentVoice = null;
    private static volatile boolean shouldStop = false;

    /**
     * Stop any ongoing speech immediately.
     */
    public static void stopSpeaking() {
        shouldStop = true;
        if (currentVoice != null) {
            try {
                // Cancel any ongoing speech
                currentVoice.getAudioPlayer().cancel();
                // Also deallocate to fully stop
                currentVoice.deallocate();
                currentVoice = null;
            } catch (Throwable t) {
                System.err.println("Error stopping speech: " + t.getMessage());
            }
        }
    }

    /**
     * Reset the stop flag to allow new speech to play.
     * Call this before starting a new speech sequence.
     */
    public static void resetStopFlag() {
        shouldStop = false;
    }

    /**
     * Speak the given text with the voice defined by VOICE_NAME.
     *
     * Handles null/empty input gracefully, caps extremely long input,
     * splits into sentence-like chunks to avoid tokenizer OOM, and
     * ensures proper allocation/deallocation even on exceptions.
     *
     * @param text the text to speak
     */
    public static void speak(String text) {
        if (text == null || text.isEmpty())
            return;

        // Early check if we should stop (don't reset the flag here!)
        if (shouldStop) {
            System.out.println("Speak() called but shouldStop is true - skipping");
            return;
        }

        if (text.length() > MAX_CHARS) {
            text = text.substring(0, MAX_CHARS);
        }

        System.setProperty(
                "freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(VOICE_NAME);
        if (voice == null) {
            System.err.println("Voice not found: " + VOICE_NAME);
            return;
        }

        try {
            voice.allocate();
            currentVoice = voice; // Track for cancellation
            String[] chunks = text.split("(?<=[.!?])\\s+");
            for (String chunk : chunks) {
                if (shouldStop) {
                    System.out.println("Speech interrupted by stopSpeaking()");
                    break;
                }
                if (chunk == null || chunk.isEmpty())
                    continue;
                try {
                    voice.speak(chunk);
                } catch (Throwable t) {
                    // swallow chunk-level failures to keep overall call graceful
                    System.err.println("Speek: chunk speak failed: " + t.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("Speek.speak failed but was handled: " + t.getMessage());
        } finally {
            try {
                voice.deallocate();
                currentVoice = null; // Clear tracking
            } catch (Throwable ignored) {
            }
        }
    }
}
