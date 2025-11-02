package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StoryElementsTest {

    private StoryElements story;

    @Before
    public void setUp() {
        story = new StoryElements();
    }

    @Test
    public void defaultConstructor_shouldCreateEmptyStory() {
        assertNotNull(story);
        assertNull(story.getIntro());
        assertNull(story.getRoomOneIntro());
        assertNull(story.getRoomOneConc());
        assertNull(story.getRoomTwoIntro());
        assertNull(story.getRoomTwoBetween());
        assertNull(story.getRoomTwoConc());
        assertNull(story.getRoomThreeIntro());
        assertNull(story.getRoomThreeBetween());
        assertNull(story.getRoomThreeConc());
        assertNull(story.getFinalPuzzle());
        assertNull(story.getConclusion());
    }

    @Test
    public void setIntro_shouldStoreAndReturnIntro() {
        story.setIntro("Welcome to the escape room!");
        assertEquals("Welcome to the escape room!", story.getIntro());
    }

    @Test
    public void setRoomOneIntro_shouldStoreAndReturnRoomOneIntro() {
        story.setRoomOneIntro("You enter the first room...");
        assertEquals("You enter the first room...", story.getRoomOneIntro());
    }

    @Test
    public void setRoomOneConc_shouldStoreAndReturnRoomOneConclusion() {
        story.setRoomOneConc("You completed the first room!");
        assertEquals("You completed the first room!", story.getRoomOneConc());
    }

    @Test
    public void setRoomTwoIntro_shouldStoreAndReturnRoomTwoIntro() {
        story.setRoomTwoIntro("The second room awaits...");
        assertEquals("The second room awaits...", story.getRoomTwoIntro());
    }

    @Test
    public void setRoomTwoBetween_shouldStoreAndReturnRoomTwoBetween() {
        story.setRoomTwoBetween("Between puzzles in room two...");
        assertEquals("Between puzzles in room two...", story.getRoomTwoBetween());
    }
}