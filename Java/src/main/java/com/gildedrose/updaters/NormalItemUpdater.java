package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Updater for normal/standard items in the Gilded Rose inventory.
 * 
 * Normal items follow these business rules:
 * - Quality degrades by 1 each day before the sell date
 * - Quality degrades by 2 each day after the sell date has passed
 * - Quality never goes below 0
 * - SellIn decreases by 1 each day
 * 
 * Examples of normal items: "+5 Dexterity Vest", "Elixir of the Mongoose"
 */
public class NormalItemUpdater extends AbstractItemUpdater {

    /**
     * Updates a normal item's quality and sellIn.
     * 
     * Algorithm:
     * 1. Decrease quality by 1 (standard daily degradation)
     * 2. Decrease sellIn by 1 (one day passes)
     * 3. If now expired, decrease quality by 1 more (degradation doubles after expiry)
     * 
     * @param item The normal item to update
     */
    @Override
    public void update(Item item) {
        decreaseQuality(item);
        decreaseSellIn(item);
        
        if (isExpired(item)) {
            decreaseQuality(item);
        }
    }
}
