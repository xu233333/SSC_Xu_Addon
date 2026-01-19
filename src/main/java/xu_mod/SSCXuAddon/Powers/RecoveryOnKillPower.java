package xu_mod.SSCXuAddon.Powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.SelfActionOnKillPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_CCA;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class RecoveryOnKillPower extends SelfActionOnKillPower {

    private final Predicate<Pair<DamageSource, Float>> damageCondition;
    private final Predicate<Entity> targetCondition;
    private final Consumer<Entity> entityAction;
    private final double[] recoveryRadios = new double[5];

    public RecoveryOnKillPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<DamageSource, Float>> damageCondition, Predicate<Entity> targetCondition, Consumer<Entity> entityAction) {
        super(type, entity, 0, HudRender.DONT_RENDER, null, null, null);
        this.damageCondition = damageCondition;
        this.targetCondition = targetCondition;
        this.entityAction = entityAction;
    }

    public void recoveryMana(Entity KilledEntity) {
        if (KilledEntity instanceof LivingEntity livingEntity && this.entity instanceof PlayerEntity player) {
            float KE_MaxHP = livingEntity.getMaxHealth();
            int nowManaLevel = Init_CCA.AddonData.get(player).getManaLevel();
            double recoveryRadio = recoveryRadios[Math.max(0, Math.min(nowManaLevel, 4))];
            ManaUtils.gainPlayerMana(player, (int) (KE_MaxHP * recoveryRadio));
        }
        return;
    }

    @Override
    public void onKill(Entity target, DamageSource damageSource, float damageAmount) {
        if(targetCondition == null || targetCondition.test(target)) {
            if(damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) {
                if(canUse()) {
                    if (entityAction != null) {
                        this.entityAction.accept(this.entity);
                    }
                    recoveryMana(target);
                    use();
                }
            }
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("recovery_on_kill_power"),
                new SerializableData()
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                        .add("target_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("level_0_recovery_radio", SerializableDataTypes.DOUBLE, 5.0)
                        .add("level_1_recovery_radio", SerializableDataTypes.DOUBLE, 2.0)
                        .add("level_2_recovery_radio", SerializableDataTypes.DOUBLE, 2.0)
                        .add("level_3_recovery_radio", SerializableDataTypes.DOUBLE, 2.0)
                        .add("level_4_recovery_radio", SerializableDataTypes.DOUBLE, 2.0),
                data -> (type, player) -> {
                    ActionFactory<Entity>.Instance entity_action = data.get("entity_action");
                    ConditionFactory<Pair<DamageSource, Float>>.Instance damage_condition = data.get("damage_condition");
                    ConditionFactory<Entity>.Instance target_condition = data.get("target_condition");
                    double level_0_recovery_radio = data.get("level_0_recovery_radio");
                    double level_1_recovery_radio = data.get("level_1_recovery_radio");
                    double level_2_recovery_radio = data.get("level_2_recovery_radio");
                    double level_3_recovery_radio = data.get("level_3_recovery_radio");
                    double level_4_recovery_radio = data.get("level_4_recovery_radio");
                    RecoveryOnKillPower power = new RecoveryOnKillPower(type, player, damage_condition, target_condition, entity_action);
                    power.recoveryRadios[0] = level_0_recovery_radio;
                    power.recoveryRadios[1] = level_1_recovery_radio;
                    power.recoveryRadios[2] = level_2_recovery_radio;
                    power.recoveryRadios[3] = level_3_recovery_radio;
                    power.recoveryRadios[4] = level_4_recovery_radio;
                    return power;
                }
        ).allowCondition();
    }

}
