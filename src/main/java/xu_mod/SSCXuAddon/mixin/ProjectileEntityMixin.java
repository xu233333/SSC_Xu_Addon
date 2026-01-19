package xu_mod.SSCXuAddon.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xu_mod.SSCXuAddon.utils.Interface.IKeepSpeedProjectile;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin implements IKeepSpeedProjectile {
    @Unique
    private boolean keepSpeed = false;
    @Unique
    private Vec3d speed = null;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (this.keepSpeed) {
            ProjectileEntity realThis = (ProjectileEntity)(Object)this;
            if (!realThis.getVelocity().equals(this.speed)) {
                realThis.setVelocity(this.speed);
                realThis.velocityDirty = true;
            }
        }
    }

    @Override
    public void SSC_Xu_Addon$keep_speed() {
        ProjectileEntity realThis = (ProjectileEntity)(Object)this;
        this.keepSpeed = true;
        this.speed = realThis.getVelocity();
    }
}