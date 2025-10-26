package com.escape.model;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class to help play the AudioPuzzle audio.
 * 
 * @author Talan Kinard
 */
public class AudioPlayer {
    
    /**
     * Plays an audio file stored in the resources folder.
     * The filename should be relative to the resources folder.
     * The filename should not include the leading slash.
     * If the filename starts with a slash, it will be removed.
     * If the file is not found, a message will be printed to the console.
     * 
     * @param resourcePath the path to the audio file to play
     */
    public static void play(String resourcePath) {
        try{
            /**
             * Flexible across classes.
             */
            String cleanPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
            InputStream fileStream = AudioPlayer.class
                .getResourceAsStream("/" + cleanPath);

            /**
             * Quick path and confirmation for audio grab.
             */
            if(fileStream == null) {
                System.out.println("Audio file not found!"+resourcePath);
                return;
            }

            BufferedInputStream bufferedStream = new BufferedInputStream(fileStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            System.out.println("The terminal begins to play a robotic audio...");
            clip.start();
            System.out.println("");

            long clipDuration = clip.getMicrosecondLength() / 1000; // milliseconds
            Thread.sleep(clipDuration);

            clip.close();
            audioStream.close();
            bufferedStream.close();
            System.out.println("The terminal audio stops.");
        } catch(Exception e) {
            System.out.println("Error playing audio.");
            e.printStackTrace();
        }
    }
}
