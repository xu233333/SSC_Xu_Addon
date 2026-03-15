package xu_mod.SSCXuAddon.data.item.trinket;

import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import xu_mod.SSCXuAddon.init.Init_Item;
import xu_mod.SSCXuAddon.network.ModPacketsServer;

import java.util.List;

public class NineLiveCharm extends TrinketItem implements Vanishable {
    public NineLiveCharm(Settings settings) {
        super(settings);
    }

    public static boolean CanTrigger(PlayerEntity player, ItemStack itemStack) {
        if (player.getItemCooldownManager().isCoolingDown(itemStack.getItem())) {
            return false;
        }
        return true;
    }

    public static void OnTrigger(PlayerEntity player, ItemStack itemStack) {
        player.setHealth(player.getMaxHealth() * 0.5f);  // 恢复一半生命值
        itemStack.damage(1, player, (entity) -> {});  // 消耗耐久
        player.getItemCooldownManager().set(itemStack.getItem(), 6000);  // 5分钟冷却
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ModPacketsServer.sendTriggerNineLiveCharm(serverPlayerEntity);
        }
    }

    public static void ClientTrigger(@NotNull PlayerEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null) {
            client.particleManager.addEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            client.world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0f, 1.0f, false);
            if (entity != client.player) {
                return;
            }
            client.gameRenderer.showFloatingItem(new ItemStack(Init_Item.CHARM_OF_NINE_LIVE, 1));
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.charm_of_nine_live.tooltip").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("item.ssc_xu_addon.charm_of_nine_live.count", stack.getMaxDamage() - stack.getDamage() + 1).formatted(Formatting.YELLOW));
    }
}
