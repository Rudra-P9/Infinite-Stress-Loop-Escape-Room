package com.escape.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class HintsTest {

    @Test
    public void constructor_initializes_all_fields_correctly() {
        Hints h = new Hints("hint-abc", 3, false, "Look behind the book.");
        assertEquals("hint-abc", h.getHint());
        assertEquals(3, h.order);
        assertEquals("Look behind the book.", h.text);
        assertFalse(h.isRevealed());
    }

    @Test
    public void setHint_returns_updated_value_and_getHint_reflects_change() {
        Hints h = new Hints("x", 1, false, "old");
        String returned = h.setHint("new-hint");
        assertEquals("new-hint", returned);
        assertEquals("new-hint", h.getHint());
    }

    @Test
    public void reveal_changes_revealed_flag_to_true() {
        Hints h = new Hints("h1", 1, false, "secret");
        assertFalse(h.isRevealed());
        h.reveal();
        assertTrue(h.isRevealed());
    }

    @Test
    public void setRevealed_allows_toggling_revealed_flag() {
        Hints h = new Hints("h2", 2, true, "already");
        assertTrue(h.isRevealed());
        h.setRevealed(false);
        assertFalse(h.isRevealed());
        h.setRevealed(true);
        assertTrue(h.isRevealed());
    }

    @Test
    public void toString_contains_hint_order_text_and_revealed_status() {
        Hints h = new Hints("hintX", 7, true, "hidden message");
        String s = h.toString();
        assertTrue(s.contains("hintX"));
        assertTrue(s.contains("7"));
        assertTrue(s.contains("hidden message"));
        assertTrue(s.contains("true"));
    }

    @Test
    public void public_fields_are_mutable_and_reflect_changes() {
        Hints h = new Hints("id", 5, false, "t");
        h.order = 9;
        h.text = "changed";
        h.revealed = true;
        assertEquals(9, h.getOrder());
        assertEquals("changed", h.getText());
        assertTrue(h.isRevealed());
    }
}