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
import net.onixary.shapeShifterCurseFabric.player_form.ability.FormAbilityManager;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import net.onixary.shapeShifterCurseFabric.player_form.transform.TransformManager;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.init.Init_Form;
import xu_mod.SSCXuAddon.utils.Misc.ExplosionBehaviorExceptBreakBlock;

public class UnStableHolyApple extends Item {
    public UnStableHolyApple(Item.Settings settings) {
        super(settings);
    }

    public @Nullable PlayerFormBase SpecialFormCovCheck(ItemStack stack, World world, PlayerEntity user) {
        PlayerFormBase form = RegPlayerFormComponent.PLAYER_FORM.get(user).getCurrentForm();
        if (form != null) {
            if (form.equals(RegPlayerForms.FAMILIAR_FOX_3) && (CursedMoon.isCursedMoon(world) && CursedMoon.isNight(world))) {
                return Init_Form.FamiliarFoxPurify;
            }
        }
        return null;
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
            @Nullable PlayerFormBase form = SpecialFormCovCheck(stack, world, player);
            if (form != null) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.unstable_holy_apple.special_form").formatted(Formatting.YELLOW), false);
                TransformManager.handleDirectTransform(player, form, false);
                return FinalStack;
            }
            // 95%爆炸 5%还原
            if (world.random.nextInt(100) < 5) {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.unstable_holy_apple.luck").formatted(Formatting.YELLOW), false);
                TransformManager.handleDirectTransform(player, RegPlayerForms.ORIGINAL_SHIFTER, false);
            }
            else {
                player.sendMessage(Text.translatable("message.ssc_xu_addon.item.unstable_holy_apple.bad_luck").formatted(Formatting.YELLOW), false);
                float explosionPower = 3.0f;
                if (CursedMoon.isNight(world)) {
                    explosionPower += 1.0f;
                }
                if (CursedMoon.isCursedMoon(world)) {
                    explosionPower += 2.0f;
                }
                Explosion explosion = new Explosion(
                        world,
                        null,
                        world.getDamageSources().explosion(null, null),
                        new ExplosionBehaviorExceptBreakBlock(),
                        user.getX(), user.getY(), user.getZ(),
                        explosionPower,
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
                        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(user.getX(), user.getY(), user.getZ(), explosionPower, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)));
                    }
                }
                user.damage(world.getDamageSources().explosion(null, null), explosionPower * 25f);  // 75~150 + 爆炸伤害
            }
        }
        return FinalStack;
    }

    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
