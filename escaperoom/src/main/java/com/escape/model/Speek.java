package com.escape.model;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Speek {
    private static final String VOICE_NAME = "kevin16";
    private static final int MAX_CHARS = 20_000;

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
        if (text == null || text.isEmpty()) return;

        if (text.length() > MAX_CHARS) {
            text = text.substring(0, MAX_CHARS);
        }

        System.setProperty(
            "freetts.voices",
            "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory"
        );

        VoiceManager voiceManager = VoiceManager.getInstance();
        Voice voice = voiceManager.getVoice(VOICE_NAME);
        if (voice == null) {
            System.err.println("Voice not found: " + VOICE_NAME);
            return;
        }

        try {
            voice.allocate();
            String[] chunks = text.split("(?<=[.!?])\\s+");
            for (String chunk : chunks) {
                if (chunk == null || chunk.isEmpty()) continue;
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
            } catch (Throwable ignored) { }
        }
    }
}
