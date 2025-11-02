package com.escape.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class InventoryTest {

    private Inventory inventory;

    @Before
    public void setUp() {
        inventory = new Inventory(3); // Capacity of 3
    }

    @Test
    public void constructor_withCapacity_shouldSetCapacityCorrectly() {
        assertEquals(3, inventory.getCapacity());
        assertEquals(0, inventory.getItemCount());
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    public void defaultConstructor_shouldCreateValidInstance() {
        Inventory defaultInventory = new Inventory();
        assertNotNull(defaultInventory);
    }

    @Test
    public void addItem_withinCapacity_shouldAddItemAndReturnTrue() {
        assertTrue(inventory.addItem("Sword"));
        assertTrue(inventory.hasItem("Sword"));
        assertEquals(1, inventory.getItemCount());
        
        assertTrue(inventory.addItem("Shield"));
        assertEquals(2, inventory.getItemCount());
    }

    @Test
    public void addItem_atCapacity_shouldReturnFalse() {
        inventory.addItem("Item1");
        inventory.addItem("Item2");
        inventory.addItem("Item3");
        
        assertFalse(inventory.addItem("Item4")); // At capacity
        assertEquals(3, inventory.getItemCount());
    }

    @Test
    public void addItem_duplicateItem_shouldAddDuplicate() {
        inventory.addItem("Key");
        assertTrue(inventory.addItem("Key")); // Allows duplicates
        assertEquals(2, inventory.getItemCount());
    }

    @Test
    public void removeItem_existingItem_shouldRemoveAndReturnTrue() {
        inventory.addItem("Potion");
        assertTrue(inventory.removeItem("Potion"));
        assertFalse(inventory.hasItem("Potion"));
        assertEquals(0, inventory.getItemCount());
    }

    @Test
    public void removeItem_nonexistentItem_shouldReturnFalse() {
        assertFalse(inventory.removeItem("Nonexistent"));
        assertEquals(0, inventory.getItemCount());
    }

    @Test
    public void removeItem_fromEmptyInventory_shouldReturnFalse() {
        assertFalse(inventory.removeItem("Anything"));
    }

    @Test
    public void hasItem_existingItem_shouldReturnTrue() {
        inventory.addItem("Map");
        assertTrue(inventory.hasItem("Map"));
    }

    @Test
    public void hasItem_nonexistentItem_shouldReturnFalse() {
        assertFalse(inventory.hasItem("Compass"));
    }

    @Test
    public void hasItem_afterRemoval_shouldReturnFalse() {
        inventory.addItem("Gem");
        inventory.removeItem("Gem");
        assertFalse(inventory.hasItem("Gem"));
    }

    @Test
    public void getItems_shouldReturnCurrentItemList() {
        inventory.addItem("A");
        inventory.addItem("B");
        
        ArrayList<String> items = inventory.getItems();
        assertEquals(2, items.size());
        assertTrue(items.contains("A"));
        assertTrue(items.contains("B"));
    }

    @Test
    public void getItems_shouldReturnCopyNotReference() {
        inventory.addItem("Original");
        ArrayList<String> firstCall = inventory.getItems();
        ArrayList<String> secondCall = inventory.getItems();
        
        assertNotSame(firstCall, secondCall);
    }

    @Test
    public void getItemCount_shouldReflectCurrentState() {
        assertEquals(0, inventory.getItemCount());
        inventory.addItem("Item");
        assertEquals(1, inventory.getItemCount());
        inventory.removeItem("Item");
        assertEquals(0, inventory.getItemCount());
    }

    @Test
    public void getCapacity_shouldReturnConstructorValue() {
        Inventory smallInventory = new Inventory(1);
        assertEquals(1, smallInventory.getCapacity());
        
        Inventory largeInventory = new Inventory(100);
        assertEquals(100, largeInventory.getCapacity());
    }
}