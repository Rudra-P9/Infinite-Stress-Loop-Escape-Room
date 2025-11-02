package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(10); // 10 seconds
    }

    @Test
    public void getRemainingSecondsShouldReturnInitialWhenNotStarted() {
        assertEquals(10, timer.getRemainingSeconds());
    }

    @Test
    public void startShouldBeginCountdown() throws InterruptedException {
        timer.start();
        Thread.sleep(1100);
        assertTrue(timer.getRemainingSeconds() <= 9);
    }

    @Test
    public void pauseShouldFreezeTime() throws InterruptedException {
        timer.start();
        Thread.sleep(500);
        timer.pause();
        int paused = timer.getRemainingSeconds();
        Thread.sleep(1000);
        assertEquals(paused, timer.getRemainingSeconds());
    }

    @Test
    public void resumeShouldContinueCountdown() throws InterruptedException {
        timer.start();
        Thread.sleep(500);
        timer.pause();
        Thread.sleep(500);
        timer.resume();
        Thread.sleep(600);
        assertTrue(timer.getRemainingSeconds() <= 8);
    }

    @Test
    public void getRemainingSecondsShouldNeverGoBelowZero() throws InterruptedException {
        timer = new Timer(1);
        timer.start();
        Thread.sleep(1500);
        assertEquals(0, timer.getRemainingSeconds());
    }

    @Test
    public void multipleStartCallsShouldNotReset() throws InterruptedException {
        timer.start();
        Thread.sleep(500);
        int first = timer.getRemainingSeconds();
        timer.start();
        Thread.sleep(500);
        assertTrue(timer.getRemainingSeconds() < first);
    }
}