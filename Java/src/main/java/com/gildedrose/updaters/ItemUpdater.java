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
 * Shared business rules are provided as default methods so implementers
 * can reuse them without extending a base class, preserving the option
 * to extend another class if needed.
 *
 * Benefits of this approach:
 * - Open/Closed Principle: New item types can be added without modifying existing code
 * - Single Responsibility: Each updater handles only its item type's logic
 * - Testability: Each updater can be unit tested in isolation
 * - No single-inheritance lock-in: Implementers can extend other classes
 */
public interface ItemUpdater {

    /** Minimum quality value for any item (business rule) */
    int MIN_QUALITY = 0;

    /** Maximum quality value for regular items (legendary items can exceed this) */
    int MAX_QUALITY = 50;

    /**
     * Updates the quality and sellIn values for the given item.
     * This method applies the specific business rules for the item type.
     *
     * @param item The item to update. Will be modified in place.
     */
    void update(Item item);

    /**
     * Decreases item quality by 1, respecting the minimum bound.
     * Quality will never go below MIN_QUALITY (0).
     *
     * @param item The item whose quality to decrease
     */
    default void decreaseQuality(Item item) {
        if (item.quality > MIN_QUALITY) {
            item.quality = item.quality - 1;
        }
    }

    /**
     * Decreases item quality by a specified amount, respecting the minimum bound.
     * Quality will never go below MIN_QUALITY (0).
     *
     * @param item   The item whose quality to decrease
     * @param amount The amount to decrease quality by
     */
    default void decreaseQuality(Item item, int amount) {
        item.quality = Math.max(MIN_QUALITY, item.quality - amount);
    }

    /**
     * Increases item quality by 1, respecting the maximum bound.
     * Quality will never exceed MAX_QUALITY (50).
     *
     * @param item The item whose quality to increase
     */
    default void increaseQuality(Item item) {
        if (item.quality < MAX_QUALITY) {
            item.quality = item.quality + 1;
        }
    }

    /**
     * Increases item quality by a specified amount, respecting the maximum bound.
     * Quality will never exceed MAX_QUALITY (50).
     *
     * @param item   The item whose quality to increase
     * @param amount The amount to increase quality by
     */
    default void increaseQuality(Item item, int amount) {
        item.quality = Math.min(MAX_QUALITY, item.quality + amount);
    }

    /**
     * Decreases the sellIn value by 1.
     * This represents one day passing in the inventory.
     *
     * @param item The item whose sellIn to decrease
     */
    default void decreaseSellIn(Item item) {
        item.sellIn = item.sellIn - 1;
    }

    /**
     * Checks if an item has passed its sell date.
     * An item is considered expired when sellIn is less than 0.
     *
     * Note: This check should be performed AFTER decreasing sellIn
     * to determine if the item became expired during this update.
     *
     * @param item The item to check
     * @return true if sellIn &lt; 0 (item is past sell date)
     */
    default boolean isExpired(Item item) {
        return item.sellIn < 0;
    }
}
