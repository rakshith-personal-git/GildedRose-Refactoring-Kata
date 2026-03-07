package com.gildedrose.updaters;

import com.gildedrose.Item;

/**
 * Updater for "Backstage passes to a TAFKAL80ETC concert" items.
 * 
 * Backstage passes have complex, time-sensitive quality rules:
 * - Quality increases by 1 when more than 10 days remain (anticipation builds)
 * - Quality increases by 2 when 10 days or less remain (excitement grows)
 * - Quality increases by 3 when 5 days or less remain (urgency peaks)
 * - Quality drops to 0 after the concert (passes become worthless)
 * - Quality never exceeds 50 (maximum quality cap)
 * - SellIn decreases by 1 each day
 * 
 * The sellIn value represents days until the concert. Once the concert
 * passes (sellIn < 0), the backstage pass has no value.
 */
public class BackstagePassUpdater implements ItemUpdater {

    /** Threshold for moderate quality increase (+2) */
    private static final int MEDIUM_URGENCY_DAYS = 10;
    
    /** Threshold for high quality increase (+3) */
    private static final int HIGH_URGENCY_DAYS = 5;

    /**
     * Updates a Backstage pass's quality and sellIn.
     * 
     * Algorithm:
     * 1. Determine quality increase based on days remaining
     * 2. Apply quality increase (capped at 50)
     * 3. Decrease sellIn by 1 (one day passes)
     * 4. If concert has passed, set quality to 0
     * 
     * @param item The Backstage pass to update
     */
    @Override
    public void update(Item item) {
        // Calculate quality increase based on proximity to concert
        int qualityIncrease = calculateQualityIncrease(item.sellIn);
        increaseQuality(item, qualityIncrease);
        
        decreaseSellIn(item);
        
        // After the concert, passes are worthless
        if (isExpired(item)) {
            item.quality = MIN_QUALITY;
        }
    }

    /**
     * Calculates how much quality should increase based on days remaining.
     * 
     * Business rules encoded:
     * - More than 10 days: +1 (normal anticipation)
     * - 6-10 days: +2 (growing excitement)
     * - 1-5 days: +3 (high urgency/demand)
     * 
     * @param sellIn Days remaining until the concert
     * @return The amount to increase quality by
     */
    private int calculateQualityIncrease(int sellIn) {
        if (sellIn <= HIGH_URGENCY_DAYS) {
            return 3;
        } else if (sellIn <= MEDIUM_URGENCY_DAYS) {
            return 2;
        } else {
            return 1;
        }
    }
}
