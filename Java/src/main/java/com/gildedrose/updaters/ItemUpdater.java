package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Strategy interface for updating item quality and sellIn values.
 * 
 * This interface enables the Strategy Pattern, allowing different item types
 * to have their own update logic without modifying the core GildedRose class.
 * Each item type (Normal, Aged Brie, Sulfuras, Backstage Pass, Conjured)
 * implements this interface with its specific business rules.
 * 
 * Benefits of this approach:
 * - Open/Closed Principle: New item types can be added without modifying existing code
 * - Single Responsibility: Each updater handles only its item type's logic
 * - Testability: Each updater can be unit tested in isolation
 */
public interface ItemUpdater {
    
    /**
     * Updates the quality and sellIn values for the given item.
     * This method applies the specific business rules for the item type.
     * 
     * @param item The item to update. Will be modified in place.
     */
    void update(Item item);
}
