package com.escape.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link User}.
 *
 * <p>
 * Focus areas:
 * <ul>
 * <li>Identity & credentials accessors: username, password, userID.</li>
 * <li>Inventory behaviors (default capacity, capacity limits, duplicates, null
 * inventory).</li>
 * <li>getCollectedLetters vs. addCollectedLetter interactions.</li>
 * <li>Setter paths: setInventory, setScore.</li>
 * <li>No-op methods don't throw (login/checkCredentials/changeUsername/logout
 * are stubs).</li>
 * </ul>
 * </p>
 *
 * <p>
 * <b>Assumptions:</b> {@link Inventory} supports:
 * capacity via constructor, {@code addItem(String)} returns false when full,
 * {@code hasItem(String)}, and {@code getItems()} returns a mutable list.
 * </p>
 */
public class UserTest {

    private UUID uid;
    private User user;

    @Before
    public void setUp() {
        uid = UUID.randomUUID();
        user = new User(uid, "testUser", "password123", "test@example.com");
    }

    // Identity & credential getters

    @Test
    public void constructor_setsUUIDUsernamePassword() {
        assertEquals(uid, user.userID); // public field in our class
        assertEquals("testUser", user.getUsername());
        assertEquals("testUser", user.getUser()); // alias to username
        assertEquals("password123", user.getPassword());

    }

    @Test
    public void userId_isNonNull_andStable() {
        assertNotNull(user.userID);
        UUID before = user.userID;
        // No setter; just verify it doesn't change through other ops
        user.setScore(123);
        user.addCollectedLetter("R");
        assertEquals(before, user.userID);
    }

    // Inventory: defaults & capacity

    @Test
    public void newUser_hasDefaultInventoryCapacity_allowsUpTo5Items_thenRejects() {
        // By spec your User constructor creates Inventory(5)
        assertNotNull(user.getInventory());

        assertTrue(user.addCollectedLetter("A"));
        assertTrue(user.addCollectedLetter("B"));
        assertTrue(user.addCollectedLetter("C"));
        assertTrue(user.addCollectedLetter("D"));
        assertTrue(user.addCollectedLetter("E"));

        // 6th should fail due to capacity
        assertFalse("Default capacity reached; sixth insert must fail",
                user.addCollectedLetter("F"));

        ArrayList<String> items = user.getCollectedLetters();
        assertEquals(5, items.size());
        assertTrue(items.contains("A"));
        assertTrue(items.contains("E"));
    }

    @Test
    public void addCollectedLetter_rejectsDuplicates() {
        assertTrue(user.addCollectedLetter("R"));
        assertFalse("Duplicate should be rejected", user.addCollectedLetter("R"));

        // another item still ok
        assertTrue(user.addCollectedLetter("E"));
        assertTrue(user.getCollectedLetters().contains("R"));
        assertTrue(user.getCollectedLetters().contains("E"));
    }

    @Test
    public void addCollectedLetter_nullIsRejected() {
        assertFalse(user.addCollectedLetter(null));
        assertTrue(user.getCollectedLetters().isEmpty());
    }

    // setInventory & null-inventory behavior

    @Test
    public void setInventory_replacesInventoryAndAffectsCapacity() {
        // Replace with capacity 1; only one item should fit
        user.setInventory(new Inventory(1));
        assertTrue(user.addCollectedLetter("Z"));
        assertFalse("Capacity(1) reached; second add must fail", user.addCollectedLetter("Y"));
        assertEquals(1, user.getCollectedLetters().size());
        assertTrue(user.getCollectedLetters().contains("Z"));
    }

    @Test
    public void setInventory_null_makesGetCollectedLettersReturnEmpty_butAddInitializesNewInventory() {
        // Make inventory null
        user.setInventory(null);

        // Reading collected letters should return an empty list
        ArrayList<String> none = user.getCollectedLetters();
        assertNotNull(none);
        assertTrue(none.isEmpty());

        // Adding should lazily create a new Inventory(26)
        assertTrue(user.addCollectedLetter("A"));
        assertTrue(user.addCollectedLetter("B"));
        assertTrue(user.getCollectedLetters().contains("A"));
        assertTrue(user.getCollectedLetters().contains("B"));
    }

    // getCollectedLetters exposure semantics

    @Test
    public void getCollectedLetters_returnsCopy_fromInventory() {
        // This verifies that we get a safe copy
        user.addCollectedLetter("R");
        ArrayList<String> list = user.getCollectedLetters();
        list.add("E"); // mutate returned list

        // The original inventory should NOT contain "E"
        assertFalse(user.getCollectedLetters().contains("E"));
        // The local list should contain "E"
        assertTrue(list.contains("E"));
    }

    // Score

    @Test
    public void score_getterAndSetter() {
        assertEquals(0, user.getScore()); // default
        user.setScore(1500);
        assertEquals(1500, user.getScore());
        user.setScore(-10); // negative scores currently allowed; assert stored value
        assertEquals(-10, user.getScore());
    }

    // No-op methods shouldn’t throw

    @Test
    public void login_noOp_doesNotThrow_andDoesNotChangeState() {
        String beforeName = user.getUsername();
        int beforeSize = user.getCollectedLetters().size();
        user.login("any", "thing"); // no-op in current implementation
        assertEquals(beforeName, user.getUsername());
        assertEquals(beforeSize, user.getCollectedLetters().size());
    }

    @Test
    public void checkCredentials_noOp_doesNotThrow() {
        user.checkCredentials("Leni", "leniSecret"); // stub
        user.checkCredentials(null, null); // even nulls shouldn’t throw
    }

    @Test
    public void changeUsername_noOp_doesNotThrow_andUsernameUnchanged() {
        String before = user.getUsername();
        user.changeUsername("NewName");
        assertEquals(before, user.getUsername());
    }

    @Test
    public void logout_noOp_doesNotThrow_andStateUnchanged() {
        String before = user.getUsername();
        int beforeSize = user.getCollectedLetters().size();
        user.logout();
        assertEquals(before, user.getUsername());
        assertEquals(beforeSize, user.getCollectedLetters().size());
    }
}
