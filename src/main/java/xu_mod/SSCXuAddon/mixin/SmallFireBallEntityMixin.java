package xu_mod.SSCXuAddon.mixin;

import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xu_mod.SSCXuAddon.utils.Interface.IFireBallDamage;
import xu_mod.SSCXuAddon.utils.Misc.ExplosionBehaviorExceptBreakBlock;

@Mixin(SmallFireballEntity.class)
public class SmallFireBallEntityMixin implements IFireBallDamage {
    @Unique
    private float damageOverWrite = -1;
    @Unique
    private boolean canExplosion = false;
    @Unique
    private float explosionPower = 0;
    @Unique
    private boolean createFire = false;

    @Unique
    private void createExplosion() {
        SmallFireballEntity realThis = (SmallFireballEntity)(Object)this;
        if (canExplosion) {
            Explosion explosion = new Explosion(
                    realThis.getWorld(),
                    realThis,
                    realThis.getWorld().getDamageSources().explosion(realThis, realThis),
                    new ExplosionBehaviorExceptBreakBlock(),
                    realThis.getX(), realThis.getY(), realThis.getZ(),
                    explosionPower,
                    createFire,
                    Explosion.DestructionType.KEEP
            );
            explosion.collectBlocksAndDamageEntities();
            explosion.affectWorld(realThis.getWorld().isClient);

            if (!(realThis.getWorld() instanceof ServerWorld serverWorld)) {
                return;
            }

            if (!explosion.shouldDestroy()) {
                explosion.clearAffectedBlocks();
            }

            for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
                if (serverPlayerEntity.squaredDistanceTo(realThis.getX(), realThis.getY(), realThis.getZ()) < 4096.0) {
                    serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(realThis.getX(), realThis.getY(), realThis.getZ(), explosionPower, explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity)));
                }
            }
        }
    }

    @ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), index = 1)
    private float modifyDamage(float amount) {
        if (damageOverWrite > 0) {
            return damageOverWrite;
        }
        return amount;
    }

    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        SmallFireballEntity realThis = (SmallFireballEntity)(Object)this;
        if (!realThis.getWorld().isClient) {
            createExplosion();
        }
    }

    @Inject(method = "onBlockHit", at = @At("TAIL"))
    private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
        SmallFireballEntity realThis = (SmallFireballEntity)(Object)this;
        if (!realThis.getWorld().isClient) {
            createExplosion();
        }
    }

    @Override
    public void SSC_Xu_Addon$setDamage(float damage) {
        this.damageOverWrite = damage;
    }

    @Override
    public void SSC_Xu_Addon$setExplosion(boolean explosion, float explosionPower, boolean createFire) {
        this.canExplosion = explosion;
        this.explosionPower = explosionPower;
        this.createFire = createFire;
    }
}
