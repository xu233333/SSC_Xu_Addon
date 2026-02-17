package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.utils.Inventory.InventoryMenuUtils;

import java.util.List;

public class SpaceGem extends Item {
    public static final Identifier lootTableId = new Identifier("minecraft", "chests/end_city_treasure");

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
            PlayerFormBase form = RegPlayerFormComponent.PLAYER_FORM.get(user).getCurrentForm();
            if (RegPlayerForms.ALLAY_SP.equals(form)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.space_gem.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.handleDirectTransform(player, Init_Form.AllayEngineer, false);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            else {
                int r = world.random.nextInt(100);
                if (r < 20) {  // 悦灵空间2级
                    InventoryMenuUtils.openPlayerSpaceBag(player, Init_Form.AllayEngineer.equals(form) ? 3 : 2);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                } else if (r < 30) {  // 末影箱
                    EnderChestInventory enderChestInventory = player.getEnderChestInventory();
                    player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, p) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.translatable("container.enderchest")));
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                } else if (r < 50) {  // 战利品 (X1~X4)
                    r = world.random.nextInt(3) + 1;
                    for (int i = 0; i < r; i++) {
                        LootContextParameterSet lootContextParameterSet = (new LootContextParameterSet.Builder((ServerWorld) world)).addOptional(LootContextParameters.THIS_ENTITY, user).add(LootContextParameters.ORIGIN, user.getPos()).build(LootContextTypes.CHEST);
                        LootTable lootTable = world.getServer().getLootManager().getLootTable(lootTableId);
                        List<ItemStack> stacks = lootTable.generateLoot(lootContextParameterSet);
                        for (ItemStack itemStack : stacks) {
                            player.giveItemStack(itemStack);
                        }
                    }
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
                } else {  // 10点虚空伤害
                    player.lastDamageTaken = 0.0f;
                    player.damage(player.getWorld().getDamageSources().outOfWorld(), 10);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.25F, 1F);
                }
                r = world.random.nextInt(100);
                if (r < 75) { // 75% 碎裂 比直接没了更能体现出不稳定性
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.25F, 1F);
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
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
