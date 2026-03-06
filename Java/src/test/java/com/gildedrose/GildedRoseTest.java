package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Comprehensive test suite for the Gilded Rose inventory management system.
 *
 * Tests are organized by item type using @Nested classes for better readability
 * and logical grouping. Each test verifies specific business rules from the
 * requirements specification.
 */
@DisplayName("Gilded Rose Inventory System")
class GildedRoseTest {

    /**
     * Helper method to create a GildedRose instance with a single item
     * and run one day's update cycle.
     */
    private Item updateSingleItem(String name, int sellIn, int quality) {
        Item[] items = new Item[]{new Item(name, sellIn, quality)};
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        return app.items[0];
    }

    @Nested
    @DisplayName("Normal Items")
    class NormalItemTests {

        /**
         * Normal items are any items that don't have special rules.
         * They degrade in quality by 1 each day before the sell date.
         */

        @Test
        @DisplayName("should decrease quality by 1 before sell date")
        void qualityDecreasesByOneBeforeSellDate() {
            Item result = updateSingleItem("+5 Dexterity Vest", 10, 20);
            assertEquals(19, result.quality);
        }

        @Test
        @DisplayName("should decrease sellIn by 1 each day")
        void sellInDecreasesByOne() {
            Item result = updateSingleItem("+5 Dexterity Vest", 10, 20);
            assertEquals(9, result.sellIn);
        }

        @Test
        @DisplayName("should decrease quality by 2 after sell date has passed")
        void qualityDecreasesTwiceAsFastAfterSellDate() {
            Item result = updateSingleItem("+5 Dexterity Vest", 0, 20);
            assertEquals(18, result.quality);
        }

        @Test
        @DisplayName("should decrease quality by 2 when sellIn is negative")
        void qualityDecreasesTwiceWhenSellInNegative() {
            Item result = updateSingleItem("+5 Dexterity Vest", -1, 20);
            assertEquals(18, result.quality);
        }

        @Test
        @DisplayName("should never have negative quality")
        void qualityNeverNegative() {
            Item result = updateSingleItem("+5 Dexterity Vest", 5, 0);
            assertEquals(0, result.quality);
        }

        @Test
        @DisplayName("should not go below zero even when degrading twice as fast")
        void qualityNeverNegativeAfterExpiry() {
            Item result = updateSingleItem("+5 Dexterity Vest", -1, 1);
            assertEquals(0, result.quality);
        }

        @ParameterizedTest(name = "quality {1} -> {2} when sellIn is {0}")
        @DisplayName("should degrade quality correctly at various sellIn values")
        @CsvSource({
            "15, 20, 19",   // well before sell date
            "1, 20, 19",    // just before sell date
            "0, 20, 18",    // on sell date (expired)
            "-1, 20, 18",   // after sell date
            "-10, 20, 18"   // well after sell date
        })
        void qualityDegradeCorrectly(int sellIn, int initialQuality, int expectedQuality) {
            Item result = updateSingleItem("Normal Item", sellIn, initialQuality);
            assertEquals(expectedQuality, result.quality);
        }

        @ParameterizedTest(name = "quality stays at 0 when sellIn is {0}")
        @DisplayName("should keep quality at zero boundary")
        @ValueSource(ints = {10, 5, 0, -1, -5})
        void qualityStaysAtZero(int sellIn) {
            Item result = updateSingleItem("Normal Item", sellIn, 0);
            assertEquals(0, result.quality);
        }
    }

    @Nested
    @DisplayName("Aged Brie")
    class AgedBrieTests {

        /**
         * Aged Brie is a special item that increases in quality as it ages.
         * It gets better with time, unlike normal items.
         */

        private static final String AGED_BRIE = "Aged Brie";

        @Test
        @DisplayName("should increase quality by 1 before sell date")
        void qualityIncreasesByOneBeforeSellDate() {
            Item result = updateSingleItem(AGED_BRIE, 10, 20);
            assertEquals(21, result.quality);
        }

        @Test
        @DisplayName("should decrease sellIn by 1 each day")
        void sellInDecreasesByOne() {
            Item result = updateSingleItem(AGED_BRIE, 10, 20);
            assertEquals(9, result.sellIn);
        }

        @Test
        @DisplayName("should increase quality by 2 after sell date has passed")
        void qualityIncreasesTwiceAsFastAfterSellDate() {
            Item result = updateSingleItem(AGED_BRIE, 0, 20);
            assertEquals(22, result.quality);
        }

        @Test
        @DisplayName("should increase quality by 2 when sellIn is negative")
        void qualityIncreasesTwiceWhenSellInNegative() {
            Item result = updateSingleItem(AGED_BRIE, -5, 20);
            assertEquals(22, result.quality);
        }

        @Test
        @DisplayName("should never have quality above 50")
        void qualityNeverExceedsFifty() {
            Item result = updateSingleItem(AGED_BRIE, 10, 50);
            assertEquals(50, result.quality);
        }

        @Test
        @DisplayName("should cap quality at 50 when it would exceed")
        void qualityCapsAtFifty() {
            Item result = updateSingleItem(AGED_BRIE, 10, 49);
            assertEquals(50, result.quality);
        }

        @Test
        @DisplayName("should cap quality at 50 even after sell date double increase")
        void qualityCapsAtFiftyAfterExpiry() {
            Item result = updateSingleItem(AGED_BRIE, -1, 49);
            assertEquals(50, result.quality);
        }

        @ParameterizedTest(name = "quality {1} -> {2} when sellIn is {0}")
        @DisplayName("should increase quality correctly at various sellIn values")
        @CsvSource({
            "15, 20, 21",   // well before sell date
            "1, 20, 21",    // just before sell date
            "0, 20, 22",    // on sell date (expired, +2)
            "-1, 20, 22",   // after sell date
            "-10, 20, 22"   // well after sell date
        })
        void qualityIncreasesCorrectly(int sellIn, int initialQuality, int expectedQuality) {
            Item result = updateSingleItem(AGED_BRIE, sellIn, initialQuality);
            assertEquals(expectedQuality, result.quality);
        }

        @ParameterizedTest(name = "quality stays at 50 when sellIn is {0}")
        @DisplayName("should keep quality at maximum boundary")
        @ValueSource(ints = {10, 5, 0, -1, -5})
        void qualityStaysAtMaximum(int sellIn) {
            Item result = updateSingleItem(AGED_BRIE, sellIn, 50);
            assertEquals(50, result.quality);
        }
    }

    @Nested
    @DisplayName("Sulfuras, Hand of Ragnaros (Legendary Item)")
    class SulfurasTests {

        /**
         * Sulfuras is a legendary item with special properties:
         * - Never has to be sold (sellIn doesn't change)
         * - Never decreases in quality
         * - Quality is always 80
         */

        private static final String SULFURAS = "Sulfuras, Hand of Ragnaros";

        @Test
        @DisplayName("should never change quality")
        void qualityNeverChanges() {
            Item result = updateSingleItem(SULFURAS, 10, 80);
            assertEquals(80, result.quality);
        }

        @Test
        @DisplayName("should never change sellIn")
        void sellInNeverChanges() {
            Item result = updateSingleItem(SULFURAS, 10, 80);
            assertEquals(10, result.sellIn);
        }

        @Test
        @DisplayName("should maintain quality at 80 regardless of initial sellIn")
        void qualityStaysAtEightyWithPositiveSellIn() {
            Item result = updateSingleItem(SULFURAS, 5, 80);
            assertEquals(80, result.quality);
        }

        @Test
        @DisplayName("should maintain quality even with zero sellIn")
        void qualityStaysAtEightyWithZeroSellIn() {
            Item result = updateSingleItem(SULFURAS, 0, 80);
            assertEquals(80, result.quality);
        }

        @Test
        @DisplayName("should maintain quality even with negative sellIn")
        void qualityStaysAtEightyWithNegativeSellIn() {
            Item result = updateSingleItem(SULFURAS, -1, 80);
            assertEquals(80, result.quality);
        }

        @ParameterizedTest(name = "sellIn {0} remains unchanged")
        @DisplayName("should never modify sellIn at any value")
        @ValueSource(ints = {100, 10, 1, 0, -1, -100})
        void sellInRemainsUnchanged(int sellIn) {
            Item result = updateSingleItem(SULFURAS, sellIn, 80);
            assertEquals(sellIn, result.sellIn);
        }
    }

    @Nested
    @DisplayName("Backstage Passes")
    class BackstagePassTests {

        /**
         * Backstage passes have complex quality rules based on proximity to concert:
         * - Quality increases by 1 when more than 10 days remain
         * - Quality increases by 2 when 10 days or less remain
         * - Quality increases by 3 when 5 days or less remain
         * - Quality drops to 0 after the concert (sellIn < 0)
         */

        private static final String BACKSTAGE_PASS = "Backstage passes to a TAFKAL80ETC concert";

        @Nested
        @DisplayName("More than 10 days before concert")
        class MoreThanTenDays {

            @Test
            @DisplayName("should increase quality by 1 when 11 days remain")
            void qualityIncreasesByOneAt11Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 11, 20);
                assertEquals(21, result.quality);
            }

            @Test
            @DisplayName("should increase quality by 1 when 15 days remain")
            void qualityIncreasesByOneAt15Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 15, 20);
                assertEquals(21, result.quality);
            }

            @Test
            @DisplayName("should decrease sellIn by 1")
            void sellInDecreasesByOne() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 15, 20);
                assertEquals(14, result.sellIn);
            }

            @ParameterizedTest(name = "quality increases by 1 when {0} days remain")
            @DisplayName("should increase quality by 1 for various days > 10")
            @ValueSource(ints = {11, 15, 20, 50, 100})
            void qualityIncreasesByOne(int sellIn) {
                Item result = updateSingleItem(BACKSTAGE_PASS, sellIn, 20);
                assertEquals(21, result.quality);
            }
        }

        @Nested
        @DisplayName("10 days or less before concert")
        class TenDaysOrLess {

            @Test
            @DisplayName("should increase quality by 2 when exactly 10 days remain")
            void qualityIncreasesByTwoAt10Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 10, 20);
                assertEquals(22, result.quality);
            }

            @Test
            @DisplayName("should increase quality by 2 when 6 days remain")
            void qualityIncreasesByTwoAt6Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 6, 20);
                assertEquals(22, result.quality);
            }

            @ParameterizedTest(name = "quality increases by 2 when {0} days remain")
            @DisplayName("should increase quality by 2 for days between 6 and 10")
            @ValueSource(ints = {10, 9, 8, 7, 6})
            void qualityIncreasesByTwo(int sellIn) {
                Item result = updateSingleItem(BACKSTAGE_PASS, sellIn, 20);
                assertEquals(22, result.quality);
            }

            @Test
            @DisplayName("should cap quality at 50 when increase would exceed")
            void qualityCapsAtFifty() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 10, 49);
                assertEquals(50, result.quality);
            }
        }

        @Nested
        @DisplayName("5 days or less before concert")
        class FiveDaysOrLess {

            @Test
            @DisplayName("should increase quality by 3 when exactly 5 days remain")
            void qualityIncreasesByThreeAt5Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 5, 20);
                assertEquals(23, result.quality);
            }

            @Test
            @DisplayName("should increase quality by 3 when 1 day remains")
            void qualityIncreasesByThreeAt1Day() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 1, 20);
                assertEquals(23, result.quality);
            }

            @ParameterizedTest(name = "quality increases by 3 when {0} days remain")
            @DisplayName("should increase quality by 3 for days between 1 and 5")
            @ValueSource(ints = {5, 4, 3, 2, 1})
            void qualityIncreasesByThree(int sellIn) {
                Item result = updateSingleItem(BACKSTAGE_PASS, sellIn, 20);
                assertEquals(23, result.quality);
            }

            @Test
            @DisplayName("should cap quality at 50 when increase would exceed")
            void qualityCapsAtFifty() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 5, 48);
                assertEquals(50, result.quality);
            }

            @Test
            @DisplayName("should cap quality at 50 from 49")
            void qualityCapsAtFiftyFrom49() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 5, 49);
                assertEquals(50, result.quality);
            }
        }

        @Nested
        @DisplayName("After the concert (expired)")
        class AfterConcert {

            @Test
            @DisplayName("should drop quality to 0 when sellIn becomes negative")
            void qualityDropsToZeroAfterConcert() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 0, 50);
                assertEquals(0, result.quality);
            }

            @Test
            @DisplayName("should have quality at 0 when already expired")
            void qualityStaysAtZeroWhenExpired() {
                Item result = updateSingleItem(BACKSTAGE_PASS, -1, 20);
                assertEquals(0, result.quality);
            }

            @ParameterizedTest(name = "quality drops to 0 regardless of initial quality {0}")
            @DisplayName("should drop any quality to 0 after concert")
            @ValueSource(ints = {50, 40, 30, 20, 10, 1})
            void qualityDropsToZero(int quality) {
                Item result = updateSingleItem(BACKSTAGE_PASS, 0, quality);
                assertEquals(0, result.quality);
            }
        }

        @Nested
        @DisplayName("Quality boundary conditions")
        class QualityBoundaries {

            @Test
            @DisplayName("should never exceed quality of 50 at 11+ days")
            void neverExceedsFiftyMoreThan10Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 15, 50);
                assertEquals(50, result.quality);
            }

            @Test
            @DisplayName("should never exceed quality of 50 at 6-10 days")
            void neverExceedsFifty6To10Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 8, 50);
                assertEquals(50, result.quality);
            }

            @Test
            @DisplayName("should never exceed quality of 50 at 1-5 days")
            void neverExceedsFifty1To5Days() {
                Item result = updateSingleItem(BACKSTAGE_PASS, 3, 50);
                assertEquals(50, result.quality);
            }
        }
    }

    @Nested
    @DisplayName("Conjured Items (NEW FEATURE)")
    class ConjuredItemTests {

        /**
         * Conjured items degrade in quality twice as fast as normal items:
         * - Quality decreases by 2 before sell date
         * - Quality decreases by 4 after sell date
         * - Quality never goes below 0
         */

        private static final String CONJURED = "Conjured Mana Cake";

        @Test
        @DisplayName("should decrease quality by 2 before sell date")
        void qualityDecreasesByTwoBeforeSellDate() {
            Item result = updateSingleItem(CONJURED, 10, 20);
            assertEquals(18, result.quality);
        }

        @Test
        @DisplayName("should decrease sellIn by 1 each day")
        void sellInDecreasesByOne() {
            Item result = updateSingleItem(CONJURED, 10, 20);
            assertEquals(9, result.sellIn);
        }

        @Test
        @DisplayName("should decrease quality by 4 after sell date has passed")
        void qualityDecreasesByFourAfterSellDate() {
            Item result = updateSingleItem(CONJURED, 0, 20);
            assertEquals(16, result.quality);
        }

        @Test
        @DisplayName("should decrease quality by 4 when sellIn is negative")
        void qualityDecreasesByFourWhenSellInNegative() {
            Item result = updateSingleItem(CONJURED, -1, 20);
            assertEquals(16, result.quality);
        }

        @Test
        @DisplayName("should never have negative quality")
        void qualityNeverNegative() {
            Item result = updateSingleItem(CONJURED, 5, 1);
            assertEquals(0, result.quality);
        }

        @Test
        @DisplayName("should not go below zero even when degrading by 4")
        void qualityNeverNegativeAfterExpiry() {
            Item result = updateSingleItem(CONJURED, -1, 3);
            assertEquals(0, result.quality);
        }

        @ParameterizedTest(name = "quality {1} -> {2} when sellIn is {0}")
        @DisplayName("should degrade quality correctly at various sellIn values")
        @CsvSource({
            "15, 20, 18",   // well before sell date (-2)
            "1, 20, 18",    // just before sell date (-2)
            "0, 20, 16",    // on sell date (expired, -4)
            "-1, 20, 16",   // after sell date (-4)
            "-10, 20, 16"   // well after sell date (-4)
        })
        void qualityDegradeCorrectly(int sellIn, int initialQuality, int expectedQuality) {
            Item result = updateSingleItem(CONJURED, sellIn, initialQuality);
            assertEquals(expectedQuality, result.quality);
        }

        @Test
        @DisplayName("should work with different conjured item names")
        void worksWithDifferentConjuredNames() {
            Item result = updateSingleItem("Conjured Dark Blade", 10, 20);
            assertEquals(18, result.quality);
        }

        @ParameterizedTest(name = "quality stays at 0 when sellIn is {0}")
        @DisplayName("should keep quality at zero boundary")
        @ValueSource(ints = {10, 5, 0, -1, -5})
        void qualityStaysAtZero(int sellIn) {
            Item result = updateSingleItem(CONJURED, sellIn, 0);
            assertEquals(0, result.quality);
        }
    }

    @Nested
    @DisplayName("Multiple Items Processing")
    class MultipleItemsTests {

        /**
         * Tests to verify that the system correctly handles multiple items
         * in a single update cycle, each with their own rules.
         */

        @Test
        @DisplayName("should update all items in a single cycle")
        void updatesAllItems() {
            Item[] items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80)
            };
            GildedRose app = new GildedRose(items);
            app.updateQuality();

            assertEquals(19, items[0].quality, "Vest quality should decrease");
            assertEquals(1, items[1].quality, "Brie quality should increase");
            assertEquals(80, items[2].quality, "Sulfuras quality should stay at 80");
        }

        @Test
        @DisplayName("should handle empty items array")
        void handlesEmptyArray() {
            Item[] items = new Item[]{};
            GildedRose app = new GildedRose(items);
            app.updateQuality();
            assertEquals(0, items.length);
        }

        @Test
        @DisplayName("should correctly process all item types together")
        void processesAllItemTypes() {
            Item[] items = new Item[]{
                new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 10, 20),
                new Item("Sulfuras, Hand of Ragnaros", 10, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 20),
                new Item("Conjured Mana Cake", 10, 20)
            };
            GildedRose app = new GildedRose(items);
            app.updateQuality();

            assertEquals(19, items[0].quality, "Normal item");
            assertEquals(21, items[1].quality, "Aged Brie");
            assertEquals(80, items[2].quality, "Sulfuras");
            assertEquals(22, items[3].quality, "Backstage pass (+2 at 10 days)");
            assertEquals(18, items[4].quality, "Conjured item (-2)");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        /**
         * Tests for boundary conditions and edge cases that might
         * reveal bugs in the quality update logic.
         */

        @Test
        @DisplayName("should handle quality at exactly 0")
        void qualityAtZero() {
            Item result = updateSingleItem("Normal Item", 10, 0);
            assertEquals(0, result.quality);
        }

        @Test
        @DisplayName("should handle quality at exactly 50")
        void qualityAtFifty() {
            Item result = updateSingleItem("Aged Brie", 10, 50);
            assertEquals(50, result.quality);
        }

        @Test
        @DisplayName("should handle sellIn at exactly 0 (expiry day)")
        void sellInAtZero() {
            Item result = updateSingleItem("Normal Item", 0, 10);
            assertEquals(8, result.quality);
            assertEquals(-1, result.sellIn);
        }

        @Test
        @DisplayName("should handle large positive sellIn values")
        void largePositiveSellIn() {
            Item result = updateSingleItem("Normal Item", 1000, 20);
            assertEquals(19, result.quality);
            assertEquals(999, result.sellIn);
        }

        @Test
        @DisplayName("should handle large negative sellIn values")
        void largeNegativeSellIn() {
            Item result = updateSingleItem("Normal Item", -1000, 20);
            assertEquals(18, result.quality);
            assertEquals(-1001, result.sellIn);
        }

        @Test
        @DisplayName("should preserve item name through updates")
        void preservesItemName() {
            Item result = updateSingleItem("My Special Item", 10, 20);
            assertEquals("My Special Item", result.name);
        }

        @Nested
        @DisplayName("Quality Boundary Transitions")
        class QualityBoundaryTransitions {

            @Test
            @DisplayName("should stop at 0 when quality would go to -1")
            void stopsAtZeroFromOne() {
                Item result = updateSingleItem("Normal Item", 10, 1);
                assertEquals(0, result.quality);
            }

            @Test
            @DisplayName("should stop at 0 when quality would go to -2")
            void stopsAtZeroFromOneExpired() {
                Item result = updateSingleItem("Normal Item", 0, 1);
                assertEquals(0, result.quality);
            }

            @Test
            @DisplayName("should stop at 50 when Aged Brie would go to 51")
            void stopsAtFiftyFromFortyNine() {
                Item result = updateSingleItem("Aged Brie", 10, 49);
                assertEquals(50, result.quality);
            }

            @Test
            @DisplayName("should stop at 50 when Backstage pass would go to 52")
            void backstageStopsAtFiftyFrom49With10Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 10, 49);
                assertEquals(50, result.quality);
            }

            @Test
            @DisplayName("should stop at 50 when Backstage pass would go to 53")
            void backstageStopsAtFiftyFrom48With5Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 5, 48);
                assertEquals(50, result.quality);
            }
        }

        @Nested
        @DisplayName("SellIn Boundary Transitions")
        class SellInBoundaryTransitions {

            @Test
            @DisplayName("should transition from sellIn 1 to 0")
            void transitionsFromOneToZero() {
                Item result = updateSingleItem("Normal Item", 1, 20);
                assertEquals(0, result.sellIn);
            }

            @Test
            @DisplayName("should transition from sellIn 0 to -1")
            void transitionsFromZeroToNegativeOne() {
                Item result = updateSingleItem("Normal Item", 0, 20);
                assertEquals(-1, result.sellIn);
            }

            @Test
            @DisplayName("Backstage pass at sellIn 11 should increase by 1 (boundary)")
            void backstageAt11Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 11, 20);
                assertEquals(21, result.quality);
            }

            @Test
            @DisplayName("Backstage pass at sellIn 10 should increase by 2 (boundary)")
            void backstageAt10Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 10, 20);
                assertEquals(22, result.quality);
            }

            @Test
            @DisplayName("Backstage pass at sellIn 6 should increase by 2 (boundary)")
            void backstageAt6Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 6, 20);
                assertEquals(22, result.quality);
            }

            @Test
            @DisplayName("Backstage pass at sellIn 5 should increase by 3 (boundary)")
            void backstageAt5Days() {
                Item result = updateSingleItem("Backstage passes to a TAFKAL80ETC concert", 5, 20);
                assertEquals(23, result.quality);
            }
        }
    }

    @Nested
    @DisplayName("Multi-Day Simulations")
    class MultiDaySimulationTests {

        /**
         * Tests that simulate multiple days of updates to verify
         * cumulative behavior and state transitions.
         */

        @Test
        @DisplayName("should correctly degrade normal item over multiple days")
        void normalItemMultipleDays() {
            Item[] items = new Item[]{new Item("Normal Item", 5, 10)};
            GildedRose app = new GildedRose(items);

            for (int day = 0; day < 10; day++) {
                app.updateQuality();
            }

            assertEquals(0, items[0].quality);
            assertEquals(-5, items[0].sellIn);
        }

        @Test
        @DisplayName("should correctly age Brie to maximum over multiple days")
        void agedBrieReachesMaximum() {
            Item[] items = new Item[]{new Item("Aged Brie", 10, 40)};
            GildedRose app = new GildedRose(items);

            for (int day = 0; day < 20; day++) {
                app.updateQuality();
            }

            assertEquals(50, items[0].quality);
        }

        @Test
        @DisplayName("should correctly handle Backstage pass through concert")
        void backstagePassThroughConcert() {
            Item[] items = new Item[]{new Item("Backstage passes to a TAFKAL80ETC concert", 3, 45)};
            GildedRose app = new GildedRose(items);

            app.updateQuality();
            assertEquals(48, items[0].quality);
            assertEquals(2, items[0].sellIn);

            app.updateQuality();
            assertEquals(50, items[0].quality);
            assertEquals(1, items[0].sellIn);

            app.updateQuality();
            assertEquals(50, items[0].quality);
            assertEquals(0, items[0].sellIn);

            app.updateQuality();
            assertEquals(0, items[0].quality);
            assertEquals(-1, items[0].sellIn);
        }

        @Test
        @DisplayName("Sulfuras should remain unchanged over multiple days")
        void sulfurasUnchangedOverTime() {
            Item[] items = new Item[]{new Item("Sulfuras, Hand of Ragnaros", 5, 80)};
            GildedRose app = new GildedRose(items);

            for (int day = 0; day < 100; day++) {
                app.updateQuality();
            }

            assertEquals(80, items[0].quality);
            assertEquals(5, items[0].sellIn);
        }
    }
}
