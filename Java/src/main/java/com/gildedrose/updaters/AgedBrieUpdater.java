package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Updater for "Aged Brie" items in the Gilded Rose inventory.
 * 
 * Aged Brie is unique because it improves with age (like real cheese):
 * - Quality increases by 1 each day before the sell date
 * - Quality increases by 2 each day after the sell date has passed
 * - Quality never exceeds 50 (maximum quality cap)
 * - SellIn decreases by 1 each day
 * 
 * This behavior is the opposite of normal items, which degrade over time.
 */
public class AgedBrieUpdater implements ItemUpdater {

    /**
     * Updates Aged Brie's quality and sellIn.
     * 
     * Algorithm:
     * 1. Increase quality by 1 (daily improvement)
     * 2. Decrease sellIn by 1 (one day passes)
     * 3. If now expired, increase quality by 1 more (improvement doubles after expiry)
     * 
     * @param item The Aged Brie item to update
     */
    @Override
    public void update(Item item) {
        increaseQuality(item);
        decreaseSellIn(item);
        
        if (isExpired(item)) {
            increaseQuality(item);
        }
    }
}
