package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.LootFunctionTypes;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.util.EnchantmentUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.item.*;
import xu_mod.SSCXuAddon.data.item.tools.BloodClaw;
import xu_mod.SSCXuAddon.data.item.tools.SpaceBag;
import xu_mod.SSCXuAddon.data.item.tools.StableSpaceGem;
import xu_mod.SSCXuAddon.data.item.trinket.NineLiveCharm;
import xu_mod.SSCXuAddon.data.item.trinket.TrinketWithToolTip;

public class Init_Item {
    public static final Item UNSTABLE_HOLY_APPLE = register("unstable_holy_apple", new UnStableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build()).maxCount(64)
    ));
    public static final Item STABLE_HOLY_APPLE = register("stable_holy_apple", new StableHolyApple(
            new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build()).maxCount(64)
    ));
    public static final Item MANA_BOOST_BRACELET = register("mana_boost_bracelet", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.mana_boost_bracelet.tooltip").formatted(Formatting.YELLOW)));
    public static final Item CHARM_OF_BLOOD = register("charm_of_blood", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.charm_of_blood.tooltip").formatted(Formatting.YELLOW)));
    public static final Item BLOODRAGE_GAUNTLETS = register("bloodrage_gauntlets", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.bloodrage_gauntlets.tooltip").formatted(Formatting.YELLOW)));
    public static final Item SUNVEIL_CIRCLET = register("sunveil_circlet", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.sunveil_circlet.tooltip").formatted(Formatting.YELLOW)));
    public static final Item CHARM_OF_EMERALD = register("charm_of_emerald", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.charm_of_emerald.tooltip").formatted(Formatting.YELLOW)));
    public static final Item ANKLET_OF_THE_WITCH_FAMILIAR = register("anklet_of_the_witch_familiar", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.anklet_of_the_witch_familiar.tooltip").formatted(Formatting.YELLOW)));

    public static final Item BLOOD_GEM = register("blood_gem", new BloodGem(new Item.Settings().maxCount(64)));
    public static final Item BLOOD_CLAW = register("blood_claw", new BloodClaw(Materials.BLOOD_CLAW, 2, -2.4F, new Item.Settings().maxCount(1)));

    public static final Item EMERALD_ESSENCE = register("emerald_essence", new EmeraldEssence(new Item.Settings().maxCount(64)));

    public static final Item SPACE_GEM = register("space_gem", new SpaceGem(new Item.Settings().maxCount(64)));
    public static final Item STABLE_SPACE_GEM = register("stable_space_gem", new StableSpaceGem(new Item.Settings().maxCount(1).maxDamage(32)));
    public static final Item SPACE_BAG = register("space_bag", new SpaceBag(new Item.Settings().maxCount(1)));
    public static final Item SPACE_STABILIZER = register("space_stabilizer", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.space_stabilizer.tooltip").formatted(Formatting.YELLOW)));

    public static final Item WIND_GEM = register("wind_gem", new WindGem(new Item.Settings().maxCount(64)));
    public static final Item CHARM_OF_WIND = register("charm_of_wind", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.charm_of_wind.tooltip").formatted(Formatting.YELLOW)));
    public static final Item HEAVY_BRACELET = register("heavy_bracelet", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.heavy_bracelet.tooltip").formatted(Formatting.YELLOW)));
    public static final Item VITALITY_STONE = register("vitality_stone", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.vitality_stone.tooltip").formatted(Formatting.YELLOW)));

    // 火焰宝石 大地宝石 超级神圣金苹果
    public static final Item FIRE_GEM = register("fire_gem", new FireGem(new Item.Settings().maxCount(64)));
    public static final Item GROUND_GEM = register("ground_gem", new GroundGem(new Item.Settings().maxCount(64)));
    public static final Item WATER_GEM = register("water_gem", new WaterGem(new Item.Settings().maxCount(64)));
    public static final Item SUPER_HOLY_APPLE = register("super_holy_apple", new SuperHolyApple(new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(1.2f).build()).maxCount(64)));

    // 魔法海螺
    public static final Item MAGIC_CONCH = register("magic_conch", new MagicConch(new Item.Settings().maxCount(1)));

    // 通用饰品
    public static final Item CHARM_OF_NINE_LIVE = register("charm_of_nine_live", new NineLiveCharm(new Item.Settings().maxCount(1).maxDamage(8)));
    public static final Item CHARM_OF_BLOOD_THIRST = register("charm_of_blood_thirst", new TrinketWithToolTip(new Item.Settings().maxCount(1), Text.translatable("item.ssc_xu_addon.charm_of_blood_thirst.tooltip").formatted(Formatting.YELLOW)));

    // 单独画一个 Icon 没有灵感 还是用魔法海螺作为 Icon 或者之后我重绘神圣金苹果(不是太好看 我觉得现在的图标就魔法海螺好看点)或整点什么终极物品后再换吧
    public static final ItemGroup SSC_XU_ADDON_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(MAGIC_CONCH))
            .displayName(Text.translatable("itemGroup.ssc_xu_addon.ssc_xu_addon_item"))
            .entries((context, entries) -> {
                entries.add(MAGIC_CONCH);
                entries.add(MANA_BOOST_BRACELET);
                entries.add(CHARM_OF_BLOOD);
                entries.add(BLOODRAGE_GAUNTLETS);
                entries.add(SUNVEIL_CIRCLET);
                entries.add(BLOOD_CLAW);
                entries.add(CHARM_OF_EMERALD);
                entries.add(ANKLET_OF_THE_WITCH_FAMILIAR);
                entries.add(STABLE_SPACE_GEM);
                entries.add(SPACE_BAG);
                entries.add(SPACE_STABILIZER);
                entries.add(CHARM_OF_WIND);
                entries.add(HEAVY_BRACELET);
                entries.add(VITALITY_STONE);
                // entries.add(CHARM_OF_NINE_LIVE);  // 隐藏物品 不展示 之后加一下掉落
                entries.add(CHARM_OF_BLOOD_THIRST);

                entries.add(UNSTABLE_HOLY_APPLE);
                entries.add(STABLE_HOLY_APPLE);
                entries.add(SUPER_HOLY_APPLE);

                entries.add(BLOOD_GEM);
                entries.add(EMERALD_ESSENCE);
                entries.add(SPACE_GEM);
                entries.add(WIND_GEM);
                entries.add(FIRE_GEM);
                entries.add(GROUND_GEM);
                entries.add(WATER_GEM);
            })
            .build();

    public static void init() {
        Registry.register(Registries.ITEM_GROUP, SSCXuAddon.identifier("ssc_xu_addon_item"), SSC_XU_ADDON_GROUP);

        // 挂载附魔 使用我在主Mod写的API 与神化部分冲突 会在启用神化后禁用附魔台修改(不过仅互联版本会出现此冲突 毕竟神化没Fabric版)
        EnchantmentUtils.registerEnchantmentItem(Enchantments.SHARPNESS, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.SMITE, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.BANE_OF_ARTHROPODS, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.FIRE_ASPECT, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.KNOCKBACK, BloodClaw.class);
        EnchantmentUtils.registerEnchantmentItem(Enchantments.LOOTING, BloodClaw.class);

        // 互联似乎没有LootTableLoadingCallback的API 所以加个判断
        if (!FabricLoader.getInstance().isModLoaded("connectormod")) {
            LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, tableBuilder, setter) -> {
                // 鲜血宝石 会在地狱要塞(中 20% 2-3)和废弃地狱门(少 10% 1-2)宝箱刷新
                if (id.equals(new Identifier("minecraft", "chests/nether_bridge"))) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(Init_Item.BLOOD_GEM).weight(2).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 3))))
                            .with(ItemEntry.builder(Items.AIR).weight(8).quality(-1));
                    tableBuilder.pool(poolBuilder);
                }
                if (id.equals(new Identifier("minecraft", "chests/ruined_portal"))) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(Init_Item.BLOOD_GEM).weight(2).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 2))))
                            .with(ItemEntry.builder(Items.AIR).weight(18).quality(-2));
                    tableBuilder.pool(poolBuilder);
                }
                // 风之宝石 丛林神庙刷新 或者用很贵的材料合成(别问为什么配方那么难获得 主要为了鼓励探索丛林神庙)
                if (id.equals(new Identifier("minecraft", "chests/jungle_temple"))) {
                    LootPool.Builder poolBuilder = LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1))
                            .with(ItemEntry.builder(Init_Item.WIND_GEM).weight(2).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 3))))
                            .with(ItemEntry.builder(Items.AIR).weight(6).quality(-1));
                    tableBuilder.pool(poolBuilder);
                }
            });
        }
    }

    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, SSCXuAddon.identifier(path), item);
    }
}
