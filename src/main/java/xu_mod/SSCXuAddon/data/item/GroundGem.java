package xu_mod.SSCXuAddon.data.item;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.powers.LeveledManaPower;

import java.util.List;

public class GroundGem extends Item {
    public GroundGem(Settings settings) {
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
            // 强力 buff 等什么时候有形态需要这个物品时再削弱
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 3, false, true));  // -80% 受伤比例
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0, false, true));  // 防火
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 4, false, true));  // 伤害吸收
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 1, false, true));  // 减速2
            player.getHungerManager().add(20, 2.0f);  // 回满饱食度
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
                player.getItemCooldownManager().set(this, 600);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.ground_gem.tooltip.1").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("item.ssc_xu_addon.ground_gem.tooltip.2").formatted(Formatting.YELLOW));  // 地下矿坑/藏宝图宝箱
    }
}
