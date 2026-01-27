package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.onixary.shapeShifterCurseFabric.util.EnchantmentUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.item.BloodGem;
import xu_mod.SSCXuAddon.data.item.Materials;
import xu_mod.SSCXuAddon.data.item.StableHolyApple;
import xu_mod.SSCXuAddon.data.item.UnStableHolyApple;
import xu_mod.SSCXuAddon.data.item.tools.BloodClaw;
import xu_mod.SSCXuAddon.data.item.trinket.TrinketWithToolTip;

public class Init_Item {
    public static final Item UNSTABLE_HOLY_APPLE = register("unstable_holy_apple", new UnStableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build())
    ));
    public static final Item STABLE_HOLY_APPLE = register("stable_holy_apple", new StableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build())
    ));
    public static final Item MANA_BOOST_BRACELET = register("mana_boost_bracelet", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.mana_boost_bracelet.tooltip").formatted(Formatting.YELLOW)));
    public static final Item CHARM_OF_BLOOD = register("charm_of_blood", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.charm_of_blood.tooltip").formatted(Formatting.YELLOW)));
    public static final Item BLOODRAGE_GAUNTLETS = register("bloodrage_gauntlets", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.bloodrage_gauntlets.tooltip").formatted(Formatting.YELLOW)));
    public static final Item SUNBLOCK_CIRCLET = register("sunblock_circlet", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.sunblock_circlet.tooltip").formatted(Formatting.YELLOW)));

    public static final Item BLOOD_CLAW = register("blood_claw", new BloodClaw(Materials.BLOOD_CLAW, 2, -2.4F, new Item.Settings()));

    public static final Item BLOOD_GEM = register("blood_gem", new BloodGem(new Item.Settings()));

    public static void init() {
        // 先放到原版物品栏中 等物品多了之后再开一个标签页(>=9)
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(MANA_BOOST_BRACELET);
            entries.add(CHARM_OF_BLOOD);
            entries.add(BLOODRAGE_GAUNTLETS);
            entries.add(SUNBLOCK_CIRCLET);
            entries.add(BLOOD_CLAW);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(UNSTABLE_HOLY_APPLE);
            entries.add(STABLE_HOLY_APPLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLOOD_GEM);
        });

        // 挂载附魔 使用我在主Mod写的API 与神化部分冲突 会在启用神化后禁用附魔台修改(不过仅互联版本会出现此冲突 毕竟神化没Fabric版)
        EnchantmentUtils.registerEnchantmentItem(Enchantments.SHARPNESS, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.SMITE, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.BANE_OF_ARTHROPODS, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.FIRE_ASPECT, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.KNOCKBACK, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.LOOTING, BloodClaw.class);
    }

    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, SSCXuAddon.identifier(path), item);
    }
}
