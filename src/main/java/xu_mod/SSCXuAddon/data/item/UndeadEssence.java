package xu_mod.SSCXuAddon.data.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.utils.TransformManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.init.Init_Item;
import xu_mod.SSCXuAddon.network.ModPacketsServer;

import java.util.List;

public class UndeadEssence extends Item {
    public UndeadEssence(Settings settings) {
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
        if (!world.isClient) {
            if (RegPlayerForms.SPIDER_3.isPlayerForm(user)) {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(user.getStackInHand(hand));
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            if (RegPlayerForms.SPIDER_3.isPlayerForm(player)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.undead_essence.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.startTransform(player, Init_Form.SpiderUndead, null);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }
            if (RegPlayerForms.ANUBIS_WOLF_3.isPlayerForm(player)) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.undead_essence.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.startTransform(player, Init_Form.AnubisWolfSoulkeeper, null);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }

        }
        return stack;
    }

    public static void useForTotem(PlayerEntity player, ItemStack stack) {
        player.setHealth(player.getMaxHealth() * 0.5f);  // 恢复一半生命值
        player.getItemCooldownManager().set(Init_Item.UNDEAD_ESSENCE, 1200);  // 1分钟冷却
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 1));  // 30s 伤害吸收2
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 1200, 0));  // 60s 防火
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 600, 0));  // 30s 凋零1
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, 1));  // 60s 虚弱2
        stack.decrement(1);
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ModPacketsServer.sendTriggerUndeadEssenceLikeItem(serverPlayerEntity, 0);
        }
    }

    public static void useForTotemClient(@NotNull PlayerEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            client.particleManager.addEmitter(entity, ParticleTypes.SMOKE, 30);
            client.particleManager.addEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            client.world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_WITHER_DEATH, entity.getSoundCategory(), 0.75f, 0.8f, false);
            if (entity != client.player) {
                return;
            }
            client.gameRenderer.showFloatingItem(new ItemStack(Init_Item.UNDEAD_ESSENCE, 1));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.undead_essence.tooltip.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("item.ssc_xu_addon.undead_essence.tooltip.2").formatted(Formatting.GRAY));
    }
}
