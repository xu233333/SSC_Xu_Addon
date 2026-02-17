package xu_mod.SSCXuAddon.data.item.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Config;
import xu_mod.SSCXuAddon.utils.Inventory.InventoryMenuUtils;

import java.util.List;

public class SpaceBag extends Item {
    public SpaceBag(Settings settings) {
        super(settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 1;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // 我看过不少这种背包Mod刷物品的方法 还是价格配置来减少刷物品影响(发现即可在不更新时停止刷物品)
        if (user instanceof PlayerEntity player && !world.isClient && Init_Config.serverConfig.enableSpaceBag) {
            InventoryMenuUtils.openItemSpaceBag(player, stack, 9);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.space_bag.tooltip").formatted(Formatting.YELLOW));
    }
}
