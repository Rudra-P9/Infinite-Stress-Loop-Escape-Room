package com.escape.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class ScoreTest {

    @Test
    public void constructor_sets_fields_correctly() {
        Date now = new Date();
        Score s = new Score("Alice", Difficulty.MEDIUM, 300, now, 600);
        assertEquals("Alice", s.getUsername());
        assertEquals(Difficulty.MEDIUM, s.getDifficulty());
        assertEquals(300, s.getTimeLeftSec());
        assertNotNull(s.getDate());
        assertEquals(600, s.getScore());
    }

    @Test
    public void setUsername_updates_username() {
        Score s = new Score();
        s.setUsername("Bob");
        assertEquals("Bob", s.getUsername());
    }

    @Test
    public void setDifficulty_from_string_valid_value() {
        Score s = new Score();
        s.setDifficulty("HARD");
        assertEquals(Difficulty.HARD, s.getDifficulty());
    }

    @Test
    public void setDifficulty_from_string_invalid_value_defaults_to_easy() {
        Score s = new Score();
        s.setDifficulty("INVALID");
        assertEquals(Difficulty.EASY, s.getDifficulty());
    }

    @Test
    public void setDifficulty_from_enum_updates_field() {
        Score s = new Score();
        s.setDifficulty(Difficulty.MEDIUM);
        assertEquals(Difficulty.MEDIUM, s.getDifficulty());
    }

    @Test
    public void setTimeLeftSec_and_getTimeLeftSec_work() {
        Score s = new Score();
        s.setTimeLeftSec(500);
        assertEquals(500, s.getTimeLeftSec());
    }

    @Test
    public void setTimeSeconds_alias_updates_timeLeftSec() {
        Score s = new Score();
        s.setTimeSeconds(450);
        assertEquals(450, s.getTimeLeftSec());
    }

    @Test
    public void setDate_string_sets_current_date() {
        Score s = new Score();
        s.setDate("2025-11-02");
        assertNotNull(s.getDate());
    }

    @Test
    public void setDate_object_sets_given_date() {
        Score s = new Score();
        Date d = new Date(0);
        s.setDate(d);
        assertEquals(d, s.getDate());
    }

    @Test
    public void setScore_and_getScore_work() {
        Score s = new Score();
        s.setScore(900);
        assertEquals(900, s.getScore());
    }

    @Test
    public void calculateScore_easy_returns_time_times_one() {
        long result = Score.calculateScore(100, Difficulty.EASY);
        assertEquals(100, result);
    }

    @Test
    public void calculateScore_medium_returns_time_times_onePointFive() {
        long result = Score.calculateScore(200, Difficulty.MEDIUM);
        assertEquals(300, result);
    }

    @Test
    public void calculateScore_hard_returns_time_times_two() {
        long result = Score.calculateScore(250, Difficulty.HARD);
        assertEquals(500, result);
    }

    @Test
    public void toString_contains_all_key_fields() {
        Date now = new Date();
        Score s = new Score("Tester", Difficulty.EASY, 100, now, 200);
        String str = s.toString();
        assertTrue(str.contains("Tester"));
        assertTrue(str.contains("EASY"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getUserId_throws_unsupported_operation() {
        Score s = new Score();
        s.getUserId();
    }
}