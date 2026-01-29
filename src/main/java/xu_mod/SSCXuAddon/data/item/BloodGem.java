package xu_mod.SSCXuAddon.data.item;

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
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;

import java.util.List;

public class BloodGem extends Item {
    public BloodGem(Settings settings) {
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
            if (RegPlayerForms.BAT_3.equals(form)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.blood_gem.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.handleDirectTransform(player, Init_Form.BatVampire, false);
            } else if (Init_Form.BatVampire.equals(form)) {
                player.heal(player.getMaxHealth() * 0.2f);  // 回复 20% 的血量
            } else {
                // 血液排斥
                player.damage(player.getDamageSources().generic(), player.getMaxHealth() * 0.4f);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 300, 1, false, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 3, false, true));  // +12 ATK
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 1, false, true));  // -40% 受伤比例
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, SoundCategory.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
                player.getItemCooldownManager().set(this, 600);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.blood_gem.tooltip.1").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("item.ssc_xu_addon.blood_gem.tooltip.2").formatted(Formatting.DARK_RED));
    }
}
