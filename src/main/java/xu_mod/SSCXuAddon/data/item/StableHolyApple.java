package xu_mod.SSCXuAddon.data.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.onixary.shapeShifterCurseFabric.cursed_moon.CursedMoon;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.utils.Misc.ExplosionBehaviorExceptBreakBlock;

public class StableHolyApple extends Item {
    public StableHolyApple(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack FinalStack = super.finishUsing(stack, world, user);
        if (world.isClient) {
            return FinalStack;
        }
        if (user instanceof PlayerEntity player) {
            if (RegPlayerForms.ORIGINAL_BEFORE_ENABLE.equals(RegPlayerFormComponent.PLAYER_FORM.get(player).getCurrentForm())) {
                return FinalStack;
            }
            TransformManager.handleDirectTransform(player, RegPlayerForms.ORIGINAL_SHIFTER, false);
            player.sendMessage(Text.translatable("message.ssc_xu_addon.item.stable_holy_apple.effect").formatted(Formatting.YELLOW), false);
        }
        return FinalStack;
    }

    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
