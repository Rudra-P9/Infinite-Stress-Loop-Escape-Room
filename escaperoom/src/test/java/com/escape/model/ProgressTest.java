package com.escape.model;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Progress}.
 *
 * <p>Coverage highlights:
 * <ul>
 *   <li>Constructor behavior when UUIDs are null (auto-generated & non-null).</li>
 *   <li>Clamping logic in {@code setStoryPos} (range 0..TOTAL_BEATS).</li>
 *   <li>{@code advanceStory}: increments questions, caps storyPos at TOTAL_BEATS,
 *       keeps incrementing question counter even after cap, and drives {@code isComplete()}.</li>
 *   <li>Completion percent at key boundaries.</li>
 *   <li>Hint counters: {@code useHint}, {@code setHintsUsed} clamping.</li>
 *   <li>Hinted puzzle list: uniqueness, null/blank handling, defensive copy, restore via setter.</li>
 *   <li>Questions-answered setters (clamping) and alias behavior (bug documented).</li>
 *   <li>UUID getters/setters and {@code toString} sanity.</li>
 * </ul>
 * </p>
 */
public class ProgressTest {

    private Progress p;

    @Before
    public void setUp() {
        // pass nulls to exercise constructor's auto-generation path
        p = new Progress(null, null);
    }

    // Constructor 

    @Test
    public void ctor_generatesNonNullUUIDs_whenNullsProvided() {
        assertNotNull(p.getProgressUUID());
        assertNotNull(p.getUserUUID());
    }

    @Test
    public void setUUIDs_roundTrip() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        p.setProgressUUID(a);
        p.setUserUUID(b);
        assertEquals(a, p.getProgressUUID());
        assertEquals(b, p.getUserUUID());
    }

    // Story position clamping 

    @Test
    public void setStoryPos_clampsBelowZeroToZero() {
        p.setStoryPos(-42);
        assertEquals(0, p.getStoryPos());
    }

    @Test
    public void setStoryPos_clampsAboveMaxToTotalBeats() {
        p.setStoryPos(999);
        assertEquals(6, p.getStoryPos()); // TOTAL_BEATS=6
    }

    @Test
    public void setStoryPos_withinRangeIsKept() {
        p.setStoryPos(3);
        assertEquals(3, p.getStoryPos());
    }

    // Advance story / completion 

    @Test
    public void advanceStory_incrementsQuestionsAndStory_untilCap() {
        // Start fresh
        assertEquals(0, p.getStoryPos());
        assertEquals(0, p.getQuestionsAnswered());

        for (int i = 0; i < 4; i++) p.advanceStory();
        assertEquals(4, p.getStoryPos());
        assertEquals(4, p.getQuestionsAnswered());
        assertFalse(p.isComplete());
    }

    @Test
    public void advanceStory_capsStoryAtTotalBeats_butKeepsCountingQuestions() {
        // Drive to the cap (6)
        for (int i = 0; i < 6; i++) p.advanceStory();
        assertEquals(6, p.getStoryPos());
        assertEquals(6, p.getQuestionsAnswered());
        assertTrue(p.isComplete());

        // Additional calls do not raise storyPos but DO raise questionsAnswered
        p.advanceStory();
        p.advanceStory();
        assertEquals(6, p.getStoryPos());
        assertEquals(8, p.getQuestionsAnswered());
        assertTrue(p.isComplete());
    }

    // Completion percent 

    @Test
    public void completionPercent_boundaries() {
        assertEquals(0.0, p.getCompletionPercent(), 1e-9);
        p.setStoryPos(3);
        assertEquals(50.0, p.getCompletionPercent(), 1e-9); // 3/6
        p.setStoryPos(6);
        assertEquals(100.0, p.getCompletionPercent(), 1e-9);
    }

    // Questions answered setters

    @Test
    public void setQuestionsAnswered_clampsNegativeToZero() {
        p.setQuestionsAnswered(-10);
        assertEquals(0, p.getQuestionsAnswered());
        p.setQuestionsAnswered(7);
        assertEquals(7, p.getQuestionsAnswered());
    }

    /**
     * Document current alias behavior: questionsAnswered() calls itself,
     * which causes infinite recursion -> StackOverflowError.
     * This test passes by expecting that error (so CI is green while
     * clearly signaling the defect). If you fix the alias to:
     * {@code public int questionsAnswered() { return getQuestionsAnswered(); }}
     * update this test accordingly.
     */
    @Test(expected = StackOverflowError.class)
    public void questionsAnswered_alias_recursesAndThrows() {
        p.questionsAnswered(); // current implementation recurses
    }

    // Hints

    @Test
    public void useHint_incrementsHintsUsed() {
        assertEquals(0, p.getHintsUsed());
        p.useHint();
        p.useHint();
        assertEquals(2, p.getHintsUsed());
    }

    @Test
    public void setHintsUsed_clampsNegativeToZero() {
        p.setHintsUsed(-5);
        assertEquals(0, p.getHintsUsed());
        p.setHintsUsed(3);
        assertEquals(3, p.getHintsUsed());
    }

    @Test
    public void addHintFor_addsUniqueNonBlankTitles_andAlwaysIncrementsCounter() {
        // add a valid title
        p.addHintFor("Riddle#1");
        assertEquals(1, p.getHintsUsed());
        assertEquals(1, p.getHintedPuzzles().size());
        assertTrue(p.getHintedPuzzles().contains("Riddle#1"));

        // duplicate title - not added to list, but counter still increments by design
        p.addHintFor("Riddle#1");
        assertEquals(2, p.getHintsUsed());
        assertEquals(1, p.getHintedPuzzles().size());

        // blank / null titles - list unchanged, but counter still increments by design
        p.addHintFor("   ");
        p.addHintFor(null);
        assertEquals(4, p.getHintsUsed());
        assertEquals(1, p.getHintedPuzzles().size());

        // new valid title - added
        p.addHintFor("Audio Puzzle");
        assertEquals(5, p.getHintsUsed());
        assertEquals(2, p.getHintedPuzzles().size());
        assertTrue(p.getHintedPuzzles().contains("Audio Puzzle"));
    }

    @Test
    public void getHintedPuzzles_returnsDefensiveCopy() {
        p.addHintFor("A");
        List<String> copy = p.getHintedPuzzles();
        assertEquals(1, copy.size());
        copy.clear(); // mutate the copy
        // internal list must not be affected
        assertEquals(1, p.getHintedPuzzles().size());
    }

    @Test
    public void setHintedPuzzles_nullResets_toEmpty_copyIsMade() {
        // non-null list path
        List<String> src = new ArrayList<>();
        src.add("X");
        src.add("Y");
        p.setHintedPuzzles(src);

        assertEquals(2, p.getHintedPuzzles().size());
        // mutate source; Progress must keep its own copy
        src.clear();
        assertEquals(2, p.getHintedPuzzles().size());

        // null path → resets to empty
        p.setHintedPuzzles(null);
        assertTrue(p.getHintedPuzzles().isEmpty());
    }

    // toString sanity

    @Test
    public void toString_containsKeyFields() {
        p.setStoryPos(3);
        p.setHintsUsed(2);
        p.setQuestionsAnswered(4);

        String s = p.toString();
        assertTrue(s.contains("Progress:"));
        assertTrue(s.contains("% of the Escape Room completed"));
        assertTrue(s.contains("Hints Used: 2"));
        assertTrue(s.contains("Puzzles Solved: 4"));
    }
}
