package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import xu_mod.SSCXuAddon.SSCXuAddon;

import java.util.function.Consumer;

public class FallFlyingBoostPower extends Power implements Active {

    private int actionTickRate = 1;
    private Consumer<Entity> trAction = null;
    private double speed = 1.5;
    private double minSoeedBoost = 0.1;
    private Key key = null;

    private int actionNowTick = 0;

    public FallFlyingBoostPower(PowerType<?> type, LivingEntity entity, double speed, double minSoeedBoost, int actionTickRate, Consumer<Entity> trAction, boolean UseKeyMode) {
        super(type, entity);
        this.actionTickRate = actionTickRate;
        this.speed = speed;
        this.minSoeedBoost = minSoeedBoost;
        this.trAction = trAction;
        if (!UseKeyMode) {
            this.setTicking();
        }
    }

    @Override
    public void tick() {
        if (this.entity.isFallFlying()) {
            if (this.actionNowTick >= this.actionTickRate) {
                this.actionNowTick = 0;
                if (this.trAction != null) {
                    this.trAction.accept(this.entity);
                }
            } else {
                this.actionNowTick++;
            }
            Vec3d vec3d = this.entity.getRotationVector();
            Vec3d vec3d2 = this.entity.getVelocity();
            double spd = this.speed;
            double msb = this.minSoeedBoost;
            this.entity.setVelocity(vec3d2.add(vec3d.x * msb + (vec3d.x * spd - vec3d2.x) * 0.5D, vec3d.y * msb + (vec3d.y * spd - vec3d2.y) * 0.5D, vec3d.z * msb + (vec3d.z * spd - vec3d2.z) * 0.5D));
            this.entity.velocityModified = true;
        }
    }


    @Override
    public void onUse() {
        this.tick();
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public void setKey(Key key) {
        this.key = key;
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("fall_flying_boost"),
                new SerializableData()
                        .add("speed", SerializableDataTypes.DOUBLE, 1.5)
                        .add("min_speed_boost", SerializableDataTypes.DOUBLE, 0.1)
                        .add("action_tick_rate", SerializableDataTypes.INT, 1)
                        .add("action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("key", ApoliDataTypes.KEY, null),
                data -> (type, entity) -> {
                    Key key = data.get("key");
                    FallFlyingBoostPower power = new FallFlyingBoostPower(type, entity, data.getDouble("speed"), data.getDouble("min_speed_boost"), data.getInt("action_tick_rate"), data.get("action"), key != null);
                    if (key != null) {
                        power.setKey(key);
                    }
                    return power;
                }
        );
    }
}
