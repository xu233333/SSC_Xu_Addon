package xu_mod.SSCXuAddon.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.nbt.NbtCompound;
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

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("keep_speed", this.keepSpeed);
        if (this.speed != null) {
            nbt.putDouble("speed_x", this.speed.x);
            nbt.putDouble("speed_y", this.speed.y);
            nbt.putDouble("speed_z", this.speed.z);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.keepSpeed = nbt.getBoolean("keep_speed");
        double x = 0;
        double y = 0;
        double z = 0;
        if (nbt.contains("speed_x")) {
            x = nbt.getDouble("speed_x");
        }
        if (nbt.contains("speed_y")) {
            y = nbt.getDouble("speed_y");
        }
        if (nbt.contains("speed_z")) {
            z = nbt.getDouble("speed_z");
        }
        this.speed = new Vec3d(x, y, z);
    }

    @Override
    public void SSC_Xu_Addon$keep_speed() {
        ProjectileEntity realThis = (ProjectileEntity)(Object)this;
        this.keepSpeed = true;
        this.speed = realThis.getVelocity();
    }
}