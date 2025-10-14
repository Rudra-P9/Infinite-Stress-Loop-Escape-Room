package com.escape.model;

import java.util.ArrayList;

/**
 * Represents a user's inventory in the escape room game.
 * Stores items and enforces a capacity limit.
 * 
 * @author Dylan Diaz
 */
public class Inventory {

    /**
     * List of item names currently in the inventory.
     */
    private ArrayList<String> items;

    /**
     * Maximum number of items the inventory can hold.
     */
    private int capacity;

    /**
     * Constructs an inventory with a specified capacity.
     * 
     * @param capacity the maximum number of items allowed
     */
    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory if there's space.
     * 
     * @param item the item to add
     * @return true if added successfully, false if inventory is full
     */
    public boolean addItem(String item) {
        if (items.size() < capacity) {
            items.add(item);
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the inventory.
     * 
     * @param item the item to remove
     * @return true if removed, false if item not found
     */
    public boolean removeItem(String item) {
        return items.remove(item);
    }

    /**
     * Checks if the inventory contains a specific item.
     * 
     * @param item the item to check
     * @return true if the item is present, false otherwise
     */
    public boolean hasItem(String item) {
        return items.contains(item);
    }

    /**
     * Returns the list of items in the inventory.
     * 
     * @return the inventory items
     */
    public ArrayList<String> getItems() {
        return items;
    }

    /**
     * Returns the maximum capacity of the inventory.
     * 
     * @return the inventory capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of items currently stored.
     * 
     * @return the current item count
     */
    public int getItemCount() {
        return items.size();
    }
}