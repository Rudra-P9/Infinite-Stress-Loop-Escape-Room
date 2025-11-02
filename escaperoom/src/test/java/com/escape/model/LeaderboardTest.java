package com.escape.model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link Leaderboard}.
 *
 * <p>Goals:
 * <ul>
 *   <li>Adding scores sorts entries best to worst.</li>
 *   <li>Adding a score for an existing username replaces (does not duplicate).</li>
 *   <li>Bulk setters accept/ignore inputs as documented and keep sorting.</li>
 *   <li>Removal by username and by userID behaves correctly (including reflection path).</li>
 *   <li>Boundary behavior of topN(), clear(), size(), toString(), getLB() (copy) is correct.</li>
 *   <li>Null/invalid inputs are handled defensively without throwing.</li>
 * </ul>
 * </p>
 */
public class LeaderboardTest {

    private Leaderboard lb;

    @Before
    public void setUp() {
        lb = new Leaderboard();
    }

    // Helpers

    // Make a Score with a given username and explicit score value.
    private Score makeScore(String username, long scoreValue) {
        // force score
        Score s = new Score(username, Difficulty.EASY, /*timeSeconds*/0, new Date(), /*hints*/0);
        s.setScore(scoreValue);
        return s;
    }

    /**
     * Stub Score that exposes a userID through an accessor and field names
     * that Leaderboard.tryExtractIdString() searches for (exercises reflection path).
     */
    public static class ScoreWithId extends Score {
        public UUID userID; // field name Leaderboard tries
        public ScoreWithId(String username, long score, UUID id) {
            super(username, Difficulty.EASY, 0, new Date(), 0);
            setScore(score);
            this.userID = id;
        }
        @SuppressWarnings("unused")
        public UUID getUserID() { // method name Leaderboard tries
            return userID;
        }
    }

    //

    // Adding scores maintains descending order by score
    @Test
    public void addOrReplace_sortsDescending() {
        Score a = makeScore("Alice",   100);
        Score b = makeScore("Bob",     250);
        Score c = makeScore("Charlie", 175);

        lb.addOrReplace(a);
        lb.addOrReplace(b);
        lb.addOrReplace(c);

        List<Score> top = lb.topN(10);
        assertEquals(3, top.size());
        assertEquals("Bob",     top.get(0).getUsername());
        assertEquals("Charlie", top.get(1).getUsername());
        assertEquals("Alice",   top.get(2).getUsername());
    }

    // Adding with an existing username replaces the old entry (no duplicate). 
    @Test
    public void addOrReplace_replacesExistingUsername_noDuplicates() {
        Score a1 = makeScore("Alice", 100);
        Score b  = makeScore("Bob",   200);
        lb.addOrReplace(a1);
        lb.addOrReplace(b);
        assertEquals(2, lb.size());

        // New score for same username should replace (size stays 2),
        // and resort so Alice now leads.
        Score a2 = makeScore("Alice", 300);
        lb.addOrReplace(a2);

        assertEquals(2, lb.size());
        List<Score> top = lb.topN(10);
        assertEquals("Alice", top.get(0).getUsername());
        assertEquals(300, top.get(0).getScore());
        assertEquals("Bob",   top.get(1).getUsername());
    }

    // addScore() is just an alias for addOrReplace() behavior. 
    @Test
    public void addScore_aliasOfAddOrReplace() {
        lb.addScore(makeScore("U1", 10));
        lb.addScore(makeScore("U1", 99)); // replace
        assertEquals(1, lb.size());
        assertEquals(99, lb.topN(1).get(0).getScore());
    }

    // Passing null to addOrReplace is safely ignored. 
    @Test
    public void addOrReplace_nullIsIgnored() {
        lb.addOrReplace(null);
        assertEquals(0, lb.size());
    }

    // Bulk setters 

    // setEntries replaces existing and sorts; passing null clears.
    @Test
    public void setEntries_replacesAndSorts_thenClearsOnNull() {
        List<Score> list = Arrays.asList(
            makeScore("A", 10),
            makeScore("B", 50),
            makeScore("C", 30)
        );
        lb.setEntries(list);

        List<Score> top = lb.topN(10);
        assertEquals(3, top.size());
        assertEquals("B", top.get(0).getUsername());
        assertEquals("C", top.get(1).getUsername());
        assertEquals("A", top.get(2).getUsername());

        lb.setEntries(null);
        assertEquals(0, lb.size());
    }

    // setLB accepts List<?>; ignores non-Score items and sorts the rest. 
    @Test
    public void setLB_acceptsMixedList_ignoresNonScores_andSorts() {
        List<Object> mixed = new ArrayList<>();
        mixed.add(makeScore("X", 5));
        mixed.add("not-a-score");
        mixed.add(makeScore("Y", 50));
        mixed.add(null); // should also be ignored

        lb.setLB(mixed);
        assertEquals(2, lb.size());
        List<Score> top = lb.topN(10);
        assertEquals("Y", top.get(0).getUsername());
        assertEquals("X", top.get(1).getUsername());
    }

    // Removal

    // removeByUsername removes the matching entry if present; otherwise returns false. 
    @Test
    public void removeByUsername_behavesCorrectly() {
        lb.addOrReplace(makeScore("A", 1));
        lb.addOrReplace(makeScore("B", 2));
        lb.addOrReplace(makeScore("C", 3));
        assertEquals(3, lb.size());

        assertTrue(lb.removeByUsername("B"));
        assertEquals(2, lb.size());

        // Not present - false, no change
        assertFalse(lb.removeByUsername("ZZ"));
        assertEquals(2, lb.size());
    }

    // removeByUserID finds IDs via reflection on Score and removes correct entry. 
    @Test
    public void removeByUserID_reflectionPathWorks() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        ScoreWithId s1 = new ScoreWithId("Ivy",   300, id1);
        ScoreWithId s2 = new ScoreWithId("Jules", 200, id2);

        lb.addOrReplace(s1);
        lb.addOrReplace(s2);
        assertEquals(2, lb.size());

        assertTrue(lb.removeByUserID(id2));
        assertEquals(1, lb.size());
        assertEquals("Ivy", lb.topN(10).get(0).getUsername());

        // Non-existent id
        assertFalse(lb.removeByUserID(UUID.randomUUID()));
        assertEquals(1, lb.size());
    }

    // removeByUserID with null safely returns false.
    @Test
    public void removeByUserID_nullReturnsFalse() {
        lb.addOrReplace(makeScore("Only", 1));
        assertFalse(lb.removeByUserID(null));
        assertEquals(1, lb.size());
    }

    // ---------- topN / clear / size / toString / getLB ----------

    // topN respects bounds (0, between, >size). 
    @Test
    public void topN_respectsBounds() {
        lb.setEntries(Arrays.asList(
            makeScore("A", 10),
            makeScore("B", 20),
            makeScore("C", 30)
        ));
        assertTrue(lb.topN(0).isEmpty());
        assertEquals(2, lb.topN(2).size());
        assertEquals(3, lb.topN(999).size());
    }

    // clear removes all entries; size reflects it; toString reports count. 
    @Test
    public void clear_size_toString() {
        lb.addOrReplace(makeScore("A", 10));
        lb.addOrReplace(makeScore("B", 20));
        assertEquals(2, lb.size());

        lb.clear();
        assertEquals(0, lb.size());
        assertTrue(lb.toString().contains("entries=0"));
    }

    // getLB returns a copy that does NOT mutate the internal leaderboard when modified. 
    @Test
    public void getLB_returnsCopy_notLiveList() {
        lb.setEntries(Arrays.asList(
            makeScore("A", 1),
            makeScore("B", 2)
        ));
        List<Score> copy = lb.getLB();
        assertEquals(2, copy.size());

        // Mutate the copy; original should not change
        copy.clear();
        assertEquals(2, lb.size());
        assertEquals(2, lb.topN(10).size());
    }
}
