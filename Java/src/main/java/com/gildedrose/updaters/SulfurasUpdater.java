package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Updater for "Sulfuras, Hand of Ragnaros" - a legendary item.
 * 
 * Sulfuras is a legendary artifact with special properties:
 * - Never needs to be sold (sellIn doesn't change)
 * - Never decreases in quality (quality is fixed at 80)
 * - Quality of 80 is above the normal maximum of 50 (legendary exception)
 * 
 * This updater intentionally does nothing - the item is immutable.
 * Implementing the no-op pattern keeps the code consistent with the
 * Strategy Pattern while honoring the legendary item's special nature.
 */
public class SulfurasUpdater extends AbstractItemUpdater {

    /**
     * No-op update for Sulfuras.
     * 
     * Legendary items are immutable - their quality and sellIn values
     * never change. This method intentionally does nothing to preserve
     * the legendary status of Sulfuras.
     * 
     * @param item The Sulfuras item (will not be modified)
     */
    @Override
    public void update(Item item) {
        // Legendary items never change - intentional no-op
    }
}
