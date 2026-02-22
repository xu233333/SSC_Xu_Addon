package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class SpeedDamageBoostPower extends Power {
    private final ActionFactory<Pair<Entity, Entity>>.Instance onHitAction;
    private final double speedToDamageRadio;
    private final double knockbackRadio;
    private Vec3d lastPos;
    private double lastSpeed = 0;

    public SpeedDamageBoostPower(PowerType<?> type, LivingEntity entity, ActionFactory<Pair<Entity, Entity>>.Instance onHitAction, double speedToDamageRadio, double knockbackRadio) {
        super(type, entity);
        this.onHitAction = onHitAction;
        this.speedToDamageRadio = speedToDamageRadio;
        this.knockbackRadio = knockbackRadio;
        this.setTicking();
        if (this.entity != null) {
            this.lastPos = this.entity.getPos();
        } else {
            this.lastPos = Vec3d.ZERO;
        }
    }

    @Override
    public void tick() {
        if (this.entity instanceof PlayerEntity player) {
            Vec3d nowPos = this.entity.getPos();
            Vec3d movement = nowPos.subtract(this.lastPos);
            this.lastSpeed = movement.length();
            this.lastPos = nowPos;
        }
    }

    public float modifyDamageDealt(DamageSource source, float amount, LivingEntity target) {
        if (this.entity instanceof PlayerEntity player) {
            float BoostValue = (float) (lastSpeed * speedToDamageRadio);
            if (this.onHitAction != null) {
                this.onHitAction.accept(new Pair<>(this.entity, target));
            }
            target.takeKnockback(knockbackRadio * lastSpeed, this.entity.getX() - target.getX(), this.entity.getZ() - target.getZ());
            return amount + BoostValue;
        }
        return amount;
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("speed_to_damage"),
                new SerializableData()
                        .add("speed_to_damage_radio", SerializableDataTypes.DOUBLE, 0.0d)
                        .add("knockback_radio", SerializableDataTypes.DOUBLE, 0.0d)
                        .add("on_hit_action", ApoliDataTypes.BIENTITY_ACTION, null),
                data -> (type, player) -> new SpeedDamageBoostPower(type, player, data.get("on_hit_action"), data.get("speed_to_damage_radio"), data.get("knockback_radio"))
        ).allowCondition();
    }
}
