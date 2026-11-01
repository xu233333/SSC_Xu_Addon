package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.utils.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.utils.Inventory.InventoryMenuUtils;

import java.util.List;

public class SpaceGem extends Item {
    public static final Identifier extraLootTableId = new Identifier("ssc_xu_addon", "gameplay/space_gem_extra_loot");
    public static final Identifier netherLootTableId = new Identifier("minecraft", "chests/bastion_hoglin_stable");
    public static final Identifier endLootTableId = new Identifier("minecraft", "chests/end_city_treasure");
    public static final Identifier[] lootTableList = {
            new Identifier("minecraft", "chests/nether_bridge"),
            new Identifier("minecraft", "chests/ruined_portal"),
            new Identifier("minecraft", "chests/jungle_temple"),
            new Identifier("minecraft", "chests/abandoned_mineshaft"),
            new Identifier("minecraft", "chests/buried_treasure"),
            new Identifier("minecraft", "chests/underwater_ruin_small"),
            new Identifier("minecraft", "chests/underwater_ruin_big"),
            new Identifier("minecraft", "chests/spawn_bonus_chest")
    };

    public SpaceGem(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 24;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            // 我认为就一种形态需要用这种方式变身 就不拆成函数吧
            if (RegPlayerForms.ALLAY_SP.isPlayerForm(player) && !user.isSneaking()) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.space_gem.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.startTransform(player, Init_Form.AllayEngineer, null);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            else {
                int r = world.random.nextInt(100);
                int breakChance = 75;
                if (Init_Form.AllayEngineer.isPlayerForm(player)) {
                    breakChance = 50;
                    int minManaAdd = 30;
                    int maxManaAdd = 120;
                    if (r < 1) {  // 下界之星 中大奖了 1%
                        player.giveItemStack(new ItemStack(Items.NETHER_STAR, 1));
                        minManaAdd += 120;
                        maxManaAdd += 120;
                        breakChance += 100;
                    }
                    else if (r < 5) {  // 下界合金之类的 二等奖 4% 保底一个碎片
                        LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                        LootTable lootTable = world.getServer().getLootManager().getLootTable(extraLootTableId);
                        List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                        for (ItemStack itemStack : stacks) {
                            player.giveItemStack(itemStack);
                        }
                        minManaAdd += 80;
                        maxManaAdd += 100;
                        breakChance += 100;
                    }
                    else if (r < 15) {  // 猪灵要塞箱子 10%
                        r = world.random.nextInt(2) + 1;
                        for (int i = 0; i < r; i++) {
                            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                            LootTable lootTable = world.getServer().getLootManager().getLootTable(netherLootTableId);
                            List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                            for (ItemStack itemStack : stacks) {
                                player.giveItemStack(itemStack);
                            }
                            minManaAdd += 24;
                            maxManaAdd += 48;
                            breakChance += 30;
                        }
                    } else if (r < 35) {  // 末地战利品 20%
                        r = world.random.nextInt(3) + 1;
                        for (int i = 0; i < r; i++) {
                            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                            LootTable lootTable = world.getServer().getLootManager().getLootTable(endLootTableId);
                            List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                            for (ItemStack itemStack : stacks) {
                                player.giveItemStack(itemStack);
                            }
                            minManaAdd += 15;
                            maxManaAdd += 30;
                            breakChance += 20;
                        }
                    } else if (r < 55) {  // 随机战利品(从会掉落宝石的战利品表中抽奖) 20%
                        r = world.random.nextInt(lootTableList.length);
                        Identifier targetLootTableId = lootTableList[r];
                        r = world.random.nextInt(2) + 1;
                        for (int i = 0; i < r; i++) {
                            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                            LootTable lootTable = world.getServer().getLootManager().getLootTable(targetLootTableId);
                            List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                            for (ItemStack itemStack : stacks) {
                                player.giveItemStack(itemStack);
                            }
                            minManaAdd += 15;
                            maxManaAdd += 30;
                            breakChance += 20;
                        }
                    } else {  // 虚空伤害 45%
                        player.timeUntilRegen = 0;
                        player.lastDamageTaken = 0.0f;
                        r = world.random.nextInt(5) + 8;
                        player.damage(player.getWorld().getDamageSources().outOfWorld(), r);
                        minManaAdd += 60;
                        maxManaAdd += 60;
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.25F, 1F);
                    }
                    r = world.random.nextInt(maxManaAdd - minManaAdd + 1) + minManaAdd;
                    double maxMana = ManaUtils.getPlayerMaxMana(player);
                    double nowMana = ManaUtils.getPlayerMana(player);
                    double addMana = Math.min(maxMana - nowMana, r);
                    ManaUtils.gainPlayerMana(player, addMana);
                    double remainMana = r - addMana;
                    if (remainMana > 0.1) {
                        float voidDamage = (float) (remainMana / 10);
                        player.timeUntilRegen = 0;
                        player.lastDamageTaken = 0.0f;
                        player.damage(player.getWorld().getDamageSources().outOfWorld(), voidDamage);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.25F, 1F);
                    }
                } else {
                    if (r < 20) {  // 悦灵空间2级
                        InventoryMenuUtils.openPlayerSpaceBag(player, Init_Form.AllayEngineer.isPlayerForm(player) ? 6 : 4);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    } else if (r < 30) {  // 末影箱
                        EnderChestInventory enderChestInventory = player.getEnderChestInventory();
                        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, p) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.translatable("container.enderchest")));
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    } else if (r < 50) {  // 战利品 (X1~X4)
                        r = world.random.nextInt(4) + 1;
                        for (int i = 0; i < r; i++) {
                            LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                            LootTable lootTable = world.getServer().getLootManager().getLootTable(endLootTableId);
                            List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                            for (ItemStack itemStack : stacks) {
                                player.giveItemStack(itemStack);
                            }
                        }
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    } else {  // 10点虚空伤害
                        player.timeUntilRegen = 0;
                        player.lastDamageTaken = 0.0f;
                        r = world.random.nextInt(5) + 8;
                        player.damage(player.getWorld().getDamageSources().outOfWorld(), r);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.25F, 1F);
                    }
                }
                r = world.random.nextInt(100);
                if (r < breakChance) { // 75% 碎裂 比直接没了更能体现出不稳定性
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.25F, 1F);
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                    player.getItemCooldownManager().set(this, 100);
                } else {
                    player.getItemCooldownManager().set(this, 600);
                }
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.space_gem.tooltip").formatted(Formatting.YELLOW));
    }
}
