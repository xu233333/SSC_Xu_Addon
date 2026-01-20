package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.item.StableHolyApple;
import xu_mod.SSCXuAddon.data.item.UnStableHolyApple;
import xu_mod.SSCXuAddon.data.item.trinket.ManaBoostBracelet;

public class Init_Item {
    public static final Item UNSTABLE_HOLY_APPLE = register("unstable_holy_apple", new UnStableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build())
    ));
    public static final Item STABLE_HOLY_APPLE = register("stable_holy_apple", new StableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build())
    ));
    public static final Item MANA_BOOST_BRACELET = register("mana_boost_bracelet", new ManaBoostBracelet(new Item.Settings().maxCount(1)));
    public static void init() {
        // 先放到原版物品栏中 等物品多了之后再开一个标签页(>=9)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(MANA_BOOST_BRACELET);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(UNSTABLE_HOLY_APPLE);
            entries.add(STABLE_HOLY_APPLE);
        });
    }

    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, SSCXuAddon.identifier(path), item);
    }
}
