package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Abstract base class providing common functionality for all item updaters.
 * 
 * This class encapsulates the shared business rules that apply to most items:
 * - Quality has a minimum bound of 0
 * - Quality has a maximum bound of 50 (except for legendary items)
 * - SellIn decreases by 1 each day (except for legendary items)
 * 
 * Concrete updaters extend this class and implement the update() method
 * using these protected helper methods to ensure consistent boundary enforcement.
 */
public abstract class AbstractItemUpdater implements ItemUpdater {

    /** Minimum quality value for any item (business rule) */
    protected static final int MIN_QUALITY = 0;
    
    /** Maximum quality value for regular items (legendary items can exceed this) */
    protected static final int MAX_QUALITY = 50;

    /**
     * Decreases item quality by 1, respecting the minimum bound.
     * Quality will never go below MIN_QUALITY (0).
     * 
     * @param item The item whose quality to decrease
     */
    protected void decreaseQuality(Item item) {
        if (item.quality > MIN_QUALITY) {
            item.quality = item.quality - 1;
        }
    }

    /**
     * Decreases item quality by a specified amount, respecting the minimum bound.
     * Quality will never go below MIN_QUALITY (0).
     * 
     * @param item The item whose quality to decrease
     * @param amount The amount to decrease quality by
     */
    protected void decreaseQuality(Item item, int amount) {
        item.quality = Math.max(MIN_QUALITY, item.quality - amount);
    }

    /**
     * Increases item quality by 1, respecting the maximum bound.
     * Quality will never exceed MAX_QUALITY (50).
     * 
     * @param item The item whose quality to increase
     */
    protected void increaseQuality(Item item) {
        if (item.quality < MAX_QUALITY) {
            item.quality = item.quality + 1;
        }
    }

    /**
     * Increases item quality by a specified amount, respecting the maximum bound.
     * Quality will never exceed MAX_QUALITY (50).
     * 
     * @param item The item whose quality to increase
     * @param amount The amount to increase quality by
     */
    protected void increaseQuality(Item item, int amount) {
        item.quality = Math.min(MAX_QUALITY, item.quality + amount);
    }

    /**
     * Decreases the sellIn value by 1.
     * This represents one day passing in the inventory.
     * 
     * @param item The item whose sellIn to decrease
     */
    protected void decreaseSellIn(Item item) {
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
     * @return true if sellIn < 0 (item is past sell date)
     */
    protected boolean isExpired(Item item) {
        return item.sellIn < 0;
    }
}
