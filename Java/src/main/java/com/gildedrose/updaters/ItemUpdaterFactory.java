package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Factory class for creating the appropriate ItemUpdater for each item type.
 * 
 * This factory implements the Factory Method pattern to encapsulate the logic
 * for determining which updater should handle each item. This keeps the item
 * type detection logic in one place and makes it easy to add new item types.
 * 
 * Item type detection rules (in order of precedence):
 * 1. "Sulfuras, Hand of Ragnaros" - Legendary item, uses SulfurasUpdater
 * 2. "Aged Brie" - Ages well, uses AgedBrieUpdater
 * 3. "Backstage passes to a TAFKAL80ETC concert" - Concert pass, uses BackstagePassUpdater
 * 4. Items starting with "Conjured" - Magical items, uses ConjuredItemUpdater
 * 5. Everything else - Normal items, uses NormalItemUpdater (default)
 * 
 * Design decisions:
 * - Static factory method for simplicity (no need to instantiate the factory)
 * - New instances created each time to ensure thread safety
 * - Could be optimized with caching/singleton updaters if performance becomes critical
 */
public class ItemUpdaterFactory {

    /** Item name constant for Aged Brie */
    private static final String AGED_BRIE = "Aged Brie";
    
    /** Item name constant for Sulfuras (legendary) */
    private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    
    /** Item name constant for Backstage passes */
    private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";
    
    /** Item name prefix for Conjured items */
    private static final String CONJURED_PREFIX = "Conjured";

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private ItemUpdaterFactory() {
        // Utility class - prevent instantiation
    }

    /**
     * Returns the appropriate ItemUpdater for the given item.
     * 
     * The factory determines the item type based on the item's name and
     * returns an updater that implements the correct business rules for
     * that type.
     * 
     * @param item The item to get an updater for
     * @return The appropriate ItemUpdater implementation
     */
    public static ItemUpdater getUpdater(Item item) {
        if (isSulfuras(item)) {
            return new SulfurasUpdater();
        }
        
        if (isAgedBrie(item)) {
            return new AgedBrieUpdater();
        }
        
        if (isBackstagePass(item)) {
            return new BackstagePassUpdater();
        }
        
        if (isConjured(item)) {
            return new ConjuredItemUpdater();
        }
        
        // Default: treat as normal item
        return new NormalItemUpdater();
    }

    /**
     * Checks if the item is the legendary Sulfuras.
     * Sulfuras is identified by exact name match.
     */
    private static boolean isSulfuras(Item item) {
        return SULFURAS.equals(item.name);
    }

    /**
     * Checks if the item is Aged Brie.
     * Aged Brie is identified by exact name match.
     */
    private static boolean isAgedBrie(Item item) {
        return AGED_BRIE.equals(item.name);
    }

    /**
     * Checks if the item is a Backstage pass.
     * Backstage passes are identified by exact name match.
     */
    private static boolean isBackstagePass(Item item) {
        return BACKSTAGE_PASS.equals(item.name);
    }

    /**
     * Checks if the item is a Conjured item.
     * Conjured items are identified by name prefix, allowing for
     * different types of conjured items (e.g., "Conjured Mana Cake",
     * "Conjured Dark Blade").
     */
    private static boolean isConjured(Item item) {
        return item.name.startsWith(CONJURED_PREFIX);
    }
}
