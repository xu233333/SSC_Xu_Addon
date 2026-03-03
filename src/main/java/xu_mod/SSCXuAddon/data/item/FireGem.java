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
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.powers.LeveledManaPower;

import java.util.List;

public class FireGem extends Item {
    public FireGem(Settings settings) {
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
            PlayerFormBase form = RegPlayerFormComponent.PLAYER_FORM.get(user).getCurrentForm();
            if (Init_Form.FamiliarFoxPurify.equals(form)) {
                player.heal(player.getMaxHealth() * 0.25f);  // 回复 25% 的血量
                ManaUtils.gainPlayerMana(player, ManaUtils.getPlayerMaxMana(player));  // 回复全部魔力
                PowerHolderComponent.getPowers(player, LeveledManaPower.class).forEach(power -> {
                    if (power.getManaLevel() == 0) {
                        power.SetManaLevel(1);
                    }
                });  // 解除魔力衰竭
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1, false, true));  // +6 ATK
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0, false, true));  // 防火
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
                player.getItemCooldownManager().set(this, 300);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.fire_gem.tooltip").formatted(Formatting.YELLOW));
    }
}
