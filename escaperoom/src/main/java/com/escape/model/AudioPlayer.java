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
    
    public static void play(String resourcePath) {
        try{
            InputStream fileStream = AudioPlayer.class.getResourceAsStream(resourcePath);

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

            System.out.println("The terminal beings to play a robotic audio...");
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
