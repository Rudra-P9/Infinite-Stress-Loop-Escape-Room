package com.escape.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class DifficultyTest {

    @Test
    public void easyDifficulty_shouldHaveCorrectTimeLimit() {
        assertEquals(1800, Difficulty.EASY.getTimeLimitSec());
    }

    @Test
    public void mediumDifficulty_shouldHaveCorrectTimeLimit() {
        assertEquals(1200, Difficulty.MEDIUM.getTimeLimitSec());
    }

    @Test
    public void hardDifficulty_shouldHaveCorrectTimeLimit() {
        assertEquals(600, Difficulty.HARD.getTimeLimitSec());
    }

    @Test
    public void enumValues_shouldContainAllThreeDifficulties() {
        Difficulty[] difficulties = Difficulty.values();
        assertEquals(3, difficulties.length);
        assertEquals(Difficulty.EASY, difficulties[0]);
        assertEquals(Difficulty.MEDIUM, difficulties[1]);
        assertEquals(Difficulty.HARD, difficulties[2]);
    }

    @Test
    public void valueOf_shouldReturnCorrectEnum() {
        assertEquals(Difficulty.EASY, Difficulty.valueOf("EASY"));
        assertEquals(Difficulty.MEDIUM, Difficulty.valueOf("MEDIUM"));
        assertEquals(Difficulty.HARD, Difficulty.valueOf("HARD"));
    }
}