package xu_mod.SSCXuAddon.mixin;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xu_mod.SSCXuAddon.utils.Interface.IProjectileEX;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin implements IProjectileEX {
    @Unique
    private boolean keepSpeed = false;
    @Unique
    private Vec3d speed = null;
    @Unique
    private long SSC_Xu_Addon$maxAge = -1;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        ProjectileEntity realThis = (ProjectileEntity)(Object)this;
        if (this.keepSpeed) {
            if (!realThis.getVelocity().equals(this.speed)) {
                realThis.setVelocity(this.speed);
                realThis.velocityDirty = true;
            }
        }
        if (this.SSC_Xu_Addon$maxAge > 0) {
            if (realThis.age >= this.SSC_Xu_Addon$maxAge) {
                realThis.discard();
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("ssc_xu_addon_keep_speed", this.keepSpeed);
        if (this.speed != null) {
            nbt.putDouble("ssc_xu_addon_speed_x", this.speed.x);
            nbt.putDouble("ssc_xu_addon_speed_y", this.speed.y);
            nbt.putDouble("ssc_xu_addon_speed_z", this.speed.z);
        }
        nbt.putLong("ssc_xu_addon_max_age", this.SSC_Xu_Addon$maxAge);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.keepSpeed = nbt.getBoolean("keep_speed");
        double x = 0;
        double y = 0;
        double z = 0;
        if (nbt.contains("ssc_xu_addon_speed_x")) {
            x = nbt.getDouble("ssc_xu_addon_speed_x");
        }
        if (nbt.contains("ssc_xu_addon_speed_y")) {
            y = nbt.getDouble("ssc_xu_addon_speed_y");
        }
        if (nbt.contains("ssc_xu_addon_speed_z")) {
            z = nbt.getDouble("ssc_xu_addon_speed_z");
        }
        this.speed = new Vec3d(x, y, z);
        this.SSC_Xu_Addon$maxAge = nbt.getLong("ssc_xu_addon_max_age");
    }

    @Override
    public void SSC_Xu_Addon$keep_speed() {
        ProjectileEntity realThis = (ProjectileEntity)(Object)this;
        this.keepSpeed = true;
        this.speed = realThis.getVelocity();
    }

    @Override
    public void SSC_Xu_Addon$setMaxAge(long maxAge) {
        this.SSC_Xu_Addon$maxAge = maxAge;
    }
}