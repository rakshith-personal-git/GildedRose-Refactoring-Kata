package com.gildedrose;

import com.gildedrose.updaters.ItemUpdater;
import com.gildedrose.updaters.ItemUpdaterFactory;

/**
 * Gilded Rose Inventory Management System.
 * 
 * This class manages the daily update of item quality and sell-in values
 * for the Gilded Rose inn. Each item type has specific rules for how its
 * quality changes over time.
 * 
 * Architecture:
 * This implementation uses the Strategy Pattern to handle different item types.
 * Instead of complex nested if-else logic, each item type has its own updater
 * class that encapsulates its specific business rules.
 * 
 * Benefits of this design:
 * - Open/Closed Principle: Add new item types by creating new updaters
 * - Single Responsibility: Each updater handles one item type
 * - Testability: Each updater can be unit tested in isolation
 * - Readability: Clear separation of concerns
 * 
 * Item Types Supported:
 * - Normal items: Quality degrades over time
 * - Aged Brie: Quality increases over time (improves with age)
 * - Sulfuras: Legendary item, never changes
 * - Backstage passes: Quality increases as concert approaches, drops to 0 after
 * - Conjured items: Quality degrades twice as fast as normal items
 * 
 * @see ItemUpdater Interface for item update strategies
 * @see ItemUpdaterFactory Factory for creating appropriate updaters
 */
class GildedRose {
    
    /** The inventory of items managed by this system */
    Item[] items;

    /**
     * Creates a new GildedRose inventory manager.
     * 
     * @param items The initial inventory of items to manage
     */
    public GildedRose(Item[] items) {
        this.items = items;
    }

    /**
     * Updates quality and sellIn values for all items in inventory.
     * 
     * This method should be called once per day (at end of day) to:
     * 1. Update each item's quality based on its type-specific rules
     * 2. Decrease each item's sellIn value (except legendary items)
     * 
     * The update logic is delegated to type-specific ItemUpdater implementations,
     * which are obtained from the ItemUpdaterFactory based on each item's name.
     * 
     * Algorithm complexity: O(n) where n is the number of items
     */
    public void updateQuality() {
        for (Item item : items) {
            ItemUpdater updater = ItemUpdaterFactory.getUpdater(item);
            updater.update(item);
        }
    }
}
