package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;

public class SavedDataTests {
    
    private SavedData savedData;

    @Before
    public void setUp() {
        savedData = new SavedData();
    }

    /**
     * Tests that the default constructor creates an empty SavedData object with null/zero values.
     */
    @Test
    public void defaultConstructor_shouldCreateEmptyObject() {
        SavedData data = new SavedData();
        
        assertNotNull(data);
        assertNull(data.getRoom());
        assertEquals(0, data.getScore());
        assertEquals(0, data.getHints());
        assertNull(data.getPuzzle());
        assertNull(data.getSaveData());
    }

    /**
     * Tests that the parameterized constructor correctly sets all field values.
     */
    @Test
    public void parameterizedConstructor_shouldSetAllValues() {
        String room = "Room1";
        int score = 100;
        int hints = 3;
        String puzzle = "Puzzle1";
        
        SavedData data = new SavedData(room, score, hints, puzzle);
        
        assertNotNull(data);
        assertEquals(room, data.getRoom());
        assertEquals(score, data.getScore());
        assertEquals(hints, data.getHints());
        assertEquals(puzzle, data.getPuzzle());
    }

    /**
     * Tests that the parameterized constructor handles null values gracefully.
     */
    @Test
    public void parameterizedConstructor_withNullValues_shouldHandleGracefully() {
        SavedData data = new SavedData(null, 0, 0, null);
        
        assertNotNull(data);
        assertNull(data.getRoom());
        assertEquals(0, data.getScore());
        assertEquals(0, data.getHints());
        assertNull(data.getPuzzle());
    }

    /**
     * Tests that setRoom correctly updates the room field.
     */
    @Test
    public void setRoom_shouldUpdateRoom() {
        String room = "TestRoom";
        
        savedData.setRoom(room);
        
        assertEquals(room, savedData.getRoom());
    }

    /**
     * Tests that setRoom can set the room to null.
     */
    @Test
    public void setRoom_withNull_shouldSetNull() {
        savedData.setRoom("InitialRoom");
        savedData.setRoom(null);
        
        assertNull(savedData.getRoom());
    }

    /**
     * Tests that setRoom can set the room to an empty string.
     */
    @Test
    public void setRoom_withEmptyString_shouldSetEmptyString() {
        savedData.setRoom("");
        
        assertEquals("", savedData.getRoom());
    }

    /**
     * Tests that setScore correctly updates the score field.
     */
    @Test
    public void setScore_shouldUpdateScore() {
        int score = 250;
        
        savedData.setScore(score);
        
        assertEquals(score, savedData.getScore());
    }

    /**
     * Tests that setScore can set the score to zero.
     */
    @Test
    public void setScore_withZero_shouldSetZero() {
        savedData.setScore(100);
        savedData.setScore(0);
        
        assertEquals(0, savedData.getScore());
    }

    /**
     * Tests that setScore can handle negative values.
     */
    @Test
    public void setScore_withNegativeValue_shouldSetNegativeValue() {
        savedData.setScore(-50);
        
        assertEquals(-50, savedData.getScore());
    }

    /**
     * Tests that setScore can handle large values.
     */
    @Test
    public void setScore_withLargeValue_shouldSetLargeValue() {
        int largeScore = 999999;
        savedData.setScore(largeScore);
        
        assertEquals(largeScore, savedData.getScore());
    }

    /**
     * Tests that setHints correctly updates the hints field.
     */
    @Test
    public void setHints_shouldUpdateHints() {
        int hints = 5;
        
        savedData.setHints(hints);
        
        assertEquals(hints, savedData.getHints());
    }

    /**
     * Tests that setHints can set the hints to zero.
     */
    @Test
    public void setHints_withZero_shouldSetZero() {
        savedData.setHints(10);
        savedData.setHints(0);
        
        assertEquals(0, savedData.getHints());
    }

    /**
     * Tests that setHints can handle negative values.
     */
    @Test
    public void setHints_withNegativeValue_shouldSetNegativeValue() {
        savedData.setHints(-3);
        
        assertEquals(-3, savedData.getHints());
    }

    /**
     * Tests that setPuzzle correctly updates the puzzle field.
     */
    @Test
    public void setPuzzle_shouldUpdatePuzzle() {
        String puzzle = "RiddlePuzzle";
        
        savedData.setPuzzle(puzzle);
        
        assertEquals(puzzle, savedData.getPuzzle());
    }

    /**
     * Tests that setPuzzle can set the puzzle to null.
     */
    @Test
    public void setPuzzle_withNull_shouldSetNull() {
        savedData.setPuzzle("InitialPuzzle");
        savedData.setPuzzle(null);
        
        assertNull(savedData.getPuzzle());
    }

    /**
     * Tests that setPuzzle can set the puzzle to an empty string.
     */
    @Test
    public void setPuzzle_withEmptyString_shouldSetEmptyString() {
        savedData.setPuzzle("");
        
        assertEquals("", savedData.getPuzzle());
    }

    /**
     * Tests that setSaveData correctly updates the saveData field.
     */
    @Test
    public void setSaveData_shouldUpdateSaveData() {
        @SuppressWarnings("unchecked")
        HashMap<String, String>[] data = new HashMap[2];
        data[0] = new HashMap<>();
        data[0].put("key1", "value1");
        data[1] = new HashMap<>();
        data[1].put("key2", "value2");
        
        savedData.setSaveData(data);
        
        assertNotNull(savedData.getSaveData());
        assertEquals(2, savedData.getSaveData().length);
        assertEquals("value1", savedData.getSaveData()[0].get("key1"));
        assertEquals("value2", savedData.getSaveData()[1].get("key2"));
    }

    /**
     * Tests that setSaveData can set the saveData to null.
     */
    @Test
    public void setSaveData_withNull_shouldSetNull() {
        @SuppressWarnings("unchecked")
        HashMap<String, String>[] data = new HashMap[1];
        savedData.setSaveData(data);
        savedData.setSaveData(null);
        
        assertNull(savedData.getSaveData());
    }

    /**
     * Tests that setSaveData can set an empty array.
     */
    @Test
    public void setSaveData_withEmptyArray_shouldSetEmptyArray() {
        @SuppressWarnings("unchecked")
        HashMap<String, String>[] data = new HashMap[0];
        
        savedData.setSaveData(data);
        
        assertNotNull(savedData.getSaveData());
        assertEquals(0, savedData.getSaveData().length);
    }

    /**
     * Tests that multiple setters can be used to update all fields independently.
     */
    @Test
    public void multipleSetters_shouldUpdateAllFields() {
        String room = "FinalRoom";
        int score = 500;
        int hints = 7;
        String puzzle = "NumberPuzzle";
        
        savedData.setRoom(room);
        savedData.setScore(score);
        savedData.setHints(hints);
        savedData.setPuzzle(puzzle);
        
        assertEquals(room, savedData.getRoom());
        assertEquals(score, savedData.getScore());
        assertEquals(hints, savedData.getHints());
        assertEquals(puzzle, savedData.getPuzzle());
    }

    /**
     * Tests that all getters return the correct values after using the parameterized constructor.
     */
    @Test
    public void getters_afterParameterizedConstructor_shouldReturnCorrectValues() {
        String room = "StartRoom";
        int score = 150;
        int hints = 2;
        String puzzle = "WordPuzzle";
        
        SavedData data = new SavedData(room, score, hints, puzzle);
        
        assertEquals(room, data.getRoom());
        assertEquals(score, data.getScore());
        assertEquals(hints, data.getHints());
        assertEquals(puzzle, data.getPuzzle());
    }

    /**
     * Tests that setters correctly overwrite previous values.
     */
    @Test
    public void setters_shouldOverwritePreviousValues() {
        savedData.setRoom("Room1");
        savedData.setScore(100);
        savedData.setHints(3);
        savedData.setPuzzle("Puzzle1");
        
        savedData.setRoom("Room2");
        savedData.setScore(200);
        savedData.setHints(5);
        savedData.setPuzzle("Puzzle2");
        
        assertEquals("Room2", savedData.getRoom());
        assertEquals(200, savedData.getScore());
        assertEquals(5, savedData.getHints());
        assertEquals("Puzzle2", savedData.getPuzzle());
    }

    /**
     * Tests that setSaveData correctly maintains complex HashMap data structures.
     */
    @Test
    public void saveData_withComplexHashMaps_shouldMaintainData() {
        @SuppressWarnings("unchecked")
        HashMap<String, String>[] data = new HashMap[3];
        data[0] = new HashMap<>();
        data[0].put("difficulty", "hard");
        data[0].put("time", "120");
        
        data[1] = new HashMap<>();
        data[1].put("inventory", "key,potion");
        
        data[2] = new HashMap<>();
        data[2].put("status", "in_progress");
        
        savedData.setSaveData(data);
        
        HashMap<String, String>[] retrieved = savedData.getSaveData();
        assertEquals("hard", retrieved[0].get("difficulty"));
        assertEquals("120", retrieved[0].get("time"));
        assertEquals("key,potion", retrieved[1].get("inventory"));
        assertEquals("in_progress", retrieved[2].get("status"));
    }
}
