package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.entity.projectiles.BloodThornEntity;
import xu_mod.SSCXuAddon.utils.RaycastUtils;

import java.util.function.Consumer;

public class ShootBloodThornAction {
    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(
                new ActionFactory<>(
                        SSCXuAddon.identifier("shoot_blood_thorn"),
                        new SerializableData()
                                .add("damage", SerializableDataTypes.FLOAT, 5.0f)
                                .add("speed", SerializableDataTypes.FLOAT, 2.5f)
                                .add("extra_thorn_count", SerializableDataTypes.INT, 0)
                                .add("extra_thorn_circle_range", SerializableDataTypes.FLOAT, 0.5f)
                                .add("recoveryOwnerHPPercent", SerializableDataTypes.FLOAT, 0.1f)
                                .add("recoveryOwnerManaPercent", SerializableDataTypes.FLOAT, 8f)
                                .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null)
                                .add("projectile_action", ApoliDataTypes.ENTITY_ACTION, null),
                        (data, e) -> {
                            if (e.getWorld().isClient) {
                                return;
                            }
                            float damage = data.get("damage");
                            float speed = data.get("speed");
                            int extra_thorn_count = data.get("extra_thorn_count");
                            float extra_thorn_circle_range = data.get("extra_thorn_circle_range");
                            float recoveryOwnerHPPercent = data.get("recoveryOwnerHPPercent");
                            float recoveryOwnerManaPercent = data.get("recoveryOwnerManaPercent");
                            Consumer<Entity> owner_action = data.get("owner_action");
                            Consumer<Entity> projectile_action = data.get("projectile_action");
                            if (e instanceof PlayerEntity player) {
                                @Nullable Vec3d playerTarget = RaycastUtils.getPlayerTarget(player,32);
                                // Primary Thorn
                                BloodThornEntity bloodThornEntity = new BloodThornEntity(player, speed, new Vec3d(0d,0d,0d));
                                bloodThornEntity.setDamage(damage, recoveryOwnerHPPercent, recoveryOwnerManaPercent);
                                if (player.getWorld().spawnEntity(bloodThornEntity)) {
                                    if (projectile_action != null) {
                                        projectile_action.accept(bloodThornEntity);
                                    }
                                }
                                for (int i = 0; i < extra_thorn_count; i++) {
                                    // 半圆
                                    double xOffset, yOffset;
                                    if (extra_thorn_count == 1) {
                                         xOffset = 0;
                                         yOffset = extra_thorn_circle_range;
                                    } else {
                                        double angle = Math.PI * i / (extra_thorn_count - 1);
                                        xOffset = extra_thorn_circle_range * Math.cos(angle);
                                        yOffset = extra_thorn_circle_range * Math.sin(angle);
                                    }
                                    double rYaw = player.getYaw() * ((float)Math.PI / 180F);
                                    double realXOffset = xOffset * Math.cos(rYaw);
                                    double realZOffset = xOffset * Math.sin(rYaw);
                                    BloodThornEntity bloodThornEntityExtra;
                                    if (playerTarget != null) {
                                        bloodThornEntityExtra = new BloodThornEntity(player, speed, new Vec3d(realXOffset, yOffset, realZOffset), playerTarget);
                                    } else {
                                        bloodThornEntityExtra = new BloodThornEntity(player, speed, new Vec3d(realXOffset, yOffset, realZOffset));
                                    }
                                    bloodThornEntityExtra.setDamage(damage, recoveryOwnerHPPercent, recoveryOwnerManaPercent);
                                    if (player.getWorld().spawnEntity(bloodThornEntityExtra)) {
                                        if (projectile_action != null) {
                                            projectile_action.accept(bloodThornEntityExtra);
                                        }
                                    }

                                }
                                if (owner_action != null) {
                                    owner_action.accept(player);
                                }
                            }
                        }
                )
        );
    }
}
