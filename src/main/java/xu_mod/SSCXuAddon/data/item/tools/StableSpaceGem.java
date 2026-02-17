package xu_mod.SSCXuAddon.data.item.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.init.Init_Item;
import xu_mod.SSCXuAddon.utils.Inventory.InventoryMenuUtils;

import java.util.List;

public class StableSpaceGem extends Item implements Vanishable {
    public StableSpaceGem(Settings settings) {
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
            if (!user.isSneaking()) {
                InventoryMenuUtils.openPlayerSpaceBag(player, Init_Form.AllayEngineer.equals(form) ? 3 : 2);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
            } else {
                EnderChestInventory enderChestInventory = player.getEnderChestInventory();
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, p) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.translatable("container.enderchest")));
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            int r = world.random.nextInt(100);
            if (r < 80) { // 80% 减少耐久 稳定但又不太稳定
                if (!player.getAbilities().creativeMode) {
                    stack.damage(1, player, (p) -> {
                        p.sendToolBreakStatus(player.getActiveHand());
                        p.giveItemStack(new ItemStack(Init_Item.SPACE_GEM, 1));  // 只是稳定器损坏 宝石没事
                    });
                }
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.stable_space_gem.tooltip").formatted(Formatting.YELLOW));
    }
}
