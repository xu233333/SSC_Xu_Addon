package xu_mod.SSCXuAddon.Powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.Pair;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.utils.Interface.IFireBallDamage;
import xu_mod.SSCXuAddon.utils.Interface.IProjectileEX;

import java.util.function.Consumer;

public class ShootFireBallAction {
    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(
                new ActionFactory<>(
                        SSCXuAddon.identifier("shoot_fireball"),
                        new SerializableData()
                                .add("damage", SerializableDataTypes.FLOAT, 5.0f)
                                .add("speed", SerializableDataTypes.FLOAT, 1.0f)
                                .add("spread", SerializableDataTypes.FLOAT, 0.0f)
                                .add("can_explode", SerializableDataTypes.BOOLEAN, false)
                                .add("explosion_power", SerializableDataTypes.DOUBLE, 0.0d)
                                .add("explosion_create_fire", SerializableDataTypes.BOOLEAN, false)
                                .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null)
                                .add("projectile_action", ApoliDataTypes.ENTITY_ACTION, null),
                        (data, entity) -> {
                            if (entity instanceof PlayerEntity player) {
                                float damage = data.get("damage");
                                float speed = data.get("speed");
                                float spread = data.get("spread");
                                boolean can_explode = data.get("can_explode");
                                double explosion_power = data.get("explosion_power");
                                boolean explosion_create_fire = data.get("explosion_create_fire");
                                Consumer<Entity> owner_action = data.get("owner_action");
                                Consumer<Entity> projectile_action = data.get("projectile_action");
                                SmallFireballEntity fireball = new SmallFireballEntity(player.getWorld(), player, 0d, 0d, 0d);
                                fireball.setPos(fireball.getX(), fireball.getY() + player.getEyeHeight(player.getPose()), fireball.getZ());
                                fireball.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, speed, spread);
                                if (fireball instanceof IFireBallDamage iFireBallDamage) {
                                    iFireBallDamage.SSC_Xu_Addon$setDamage((float) damage);
                                    iFireBallDamage.SSC_Xu_Addon$setExplosion(can_explode, (float) explosion_power, explosion_create_fire);
                                }
                                if (fireball instanceof IProjectileEX iProjectileEX) {
                                    iProjectileEX.SSC_Xu_Addon$keep_speed();
                                    iProjectileEX.SSC_Xu_Addon$setMaxAge(200);
                                }
                                if (player.getWorld().spawnEntity(fireball)) {
                                    if (owner_action != null) {
                                        owner_action.accept(player);
                                    }
                                    if (projectile_action != null) {
                                        projectile_action.accept(fireball);
                                    }
                                }
                            }
                        }
                )
        );
    }
}
