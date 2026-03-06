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
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import xu_mod.SSCXuAddon.utils.Misc.ExplosionBehaviorExceptBreakBlock;

public class SuperHolyApple extends Item {
    public SuperHolyApple(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack FinalStack = super.finishUsing(stack, world, user);
        if (world.isClient) {
            return FinalStack;
        }
        if (user instanceof PlayerEntity player) {
            if (RegPlayerForms.ORIGINAL_SHIFTER.equals(RegPlayerFormComponent.PLAYER_FORM.get(player).getCurrentForm())) {
                TransformManager.handleDirectTransform(player, RegPlayerForms.ORIGINAL_BEFORE_ENABLE, false);
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.super_holy_apple.effect").formatted(Formatting.YELLOW), false);
                return FinalStack;
            } else {
                // 需要还原到初始形态后才能用 否则会爆炸
                // 剧情是 只祛除了诅咒 身体内的魔力没有诅咒的约束 直接失控爆炸
                Explosion explosion = new Explosion(
                        world,
                        null,
                        world.getDamageSources().explosion(null, null),
                        new ExplosionBehaviorExceptBreakBlock(),
                        user.getX(), user.getY(), user.getZ(),
                        6.0f,
                        false,
                        Explosion.DestructionType.KEEP
                );
                explosion.collectBlocksAndDamageEntities();
                explosion.affectWorld(world.isClient);

                if (!(world instanceof ServerWorld serverWorld)) {
                    return FinalStack;
                }
                if (!explosion.shouldDestroy()) {
                    explosion.clearAffectedBlocks();
                }
                for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                    if (serverPlayerEntity.squaredDistanceTo(user.getX(), user.getY(), user.getZ()) < 4096.0) {
                        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(user.getX(), user.getY(), user.getZ(), 6.0f, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)));
                    }
                }
                user.damage(world.getDamageSources().explosion(null, null), 100);
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.super_holy_apple.effect_failed").formatted(Formatting.YELLOW), false);
            }

        }
        return FinalStack;
    }

    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
