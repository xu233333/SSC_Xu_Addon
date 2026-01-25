package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RaycastUtils {
    public static @Nullable Vec3d getPlayerTarget(PlayerEntity player, double range) {
        Vec3d origin = player.getCameraPosVec(1.0F);
        Vec3d target = origin.add(player.getRotationVec(1.0F).multiply(range, range, range));
        Vec3d ray = target.subtract(origin);
        Box box = player.getBoundingBox().stretch(ray).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(player, origin, target, box, (entityx) -> !entityx.isSpectator(), ray.lengthSquared());
        if (entityHitResult != null) {
            return entityHitResult.getPos();
        }
        return null;
    }

    public static @NotNull Vec3d getPlayerTargetPos(PlayerEntity player, double range) {
        Vec3d origin = player.getCameraPosVec(1.0F);
        Vec3d target = origin.add(player.getRotationVec(1.0F).multiply(range, range, range));
        RaycastContext context = new RaycastContext(origin, target, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player);
        return player.getWorld().raycast(context).getPos();
    }
}
