package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Updater for "Conjured" items in the Gilded Rose inventory.
 * 
 * Conjured items are magically created and inherently unstable:
 * - Quality degrades TWICE as fast as normal items
 * - Quality degrades by 2 each day before the sell date
 * - Quality degrades by 4 each day after the sell date has passed
 * - Quality never goes below 0
 * - SellIn decreases by 1 each day
 * 
 * This is a NEW FEATURE added to support the new supplier of conjured items.
 * Any item with a name starting with "Conjured" uses this updater.
 * Examples: "Conjured Mana Cake", "Conjured Dark Blade"
 */
public class ConjuredItemUpdater implements ItemUpdater {

    /** Degradation multiplier for conjured items (2x normal) */
    private static final int DEGRADATION_MULTIPLIER = 2;

    /**
     * Updates a Conjured item's quality and sellIn.
     * 
     * Algorithm:
     * 1. Decrease quality by 2 (2x normal daily degradation)
     * 2. Decrease sellIn by 1 (one day passes)
     * 3. If now expired, decrease quality by 2 more (4 total, 2x the doubled rate)
     * 
     * @param item The Conjured item to update
     */
    @Override
    public void update(Item item) {
        // Conjured items degrade twice as fast as normal
        decreaseQuality(item, DEGRADATION_MULTIPLIER);
        decreaseSellIn(item);
        
        // After expiry, degradation doubles again (4 total)
        if (isExpired(item)) {
            decreaseQuality(item, DEGRADATION_MULTIPLIER);
        }
    }
}
