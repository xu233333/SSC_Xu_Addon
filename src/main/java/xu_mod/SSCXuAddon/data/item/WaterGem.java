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

public class WaterGem extends Item {
    // 潮汐宝石 给美西螈变形用的 可以变形为美西螈王(玩三叉戟的 模型上画点黄金 三叉戟也画点黄金)

    public WaterGem(Settings settings) {
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
            // if (RegPlayerForms.AXOLOTL_3.equals(form)) {
            //     player.sendMessage(Text.translatable("message.ssc_xu_addon.item.water_gem.special_form").formatted(Formatting.YELLOW), false);
            //     // TransformManager.handleDirectTransform(player, Init_Form.AXOLOTL_SEA, false);
            // }
            // else if (Init_Form.AXOLOTL_SEA.equals(form)) {
            //     player.getItemCooldownManager().set(this, 600);
            // }
            // 环境适应力
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 12000, 0, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 12000, 0, false, true));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 12000, 0, false, true));
            player.getItemCooldownManager().set(this, 600);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.water_gem.tooltip").formatted(Formatting.YELLOW));
    }
}
