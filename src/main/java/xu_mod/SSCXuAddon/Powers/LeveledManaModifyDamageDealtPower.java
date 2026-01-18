package xu_mod.SSCXuAddon.Powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LeveledManaModifyDamageDealtPower extends Power {
    private final HashMap<Integer, Double> ManaCostPercent = new HashMap<>();
    private final HashMap<Integer, Double> ExtraDamageManaPercent = new HashMap<>();
    private final Predicate<Pair<DamageSource, Float>> DS_Condition;
    private final Predicate<Entity> TargetCondition;
    private final Predicate<Pair<Entity, Entity>> BiEntityCondition;
    private final HashMap<Integer, Consumer<Entity>> targetAction = new HashMap<>();
    private final HashMap<Integer, Consumer<Pair<Entity, Entity>>> biEntityAction = new HashMap<>();
    private final HashMap<Integer, Consumer<Entity>> selfAction = new HashMap<>();

    public LeveledManaModifyDamageDealtPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<DamageSource, Float>> condition, Predicate<Entity> targetCondition, Predicate<Pair<Entity, Entity>> biEntityCondition) {
        super(type, entity);
        this.DS_Condition = condition;
        this.TargetCondition = targetCondition;
        this.BiEntityCondition = biEntityCondition;
    }

    public void addLevelProcessLine(int level, double ManaCostPercent, double ExtraDamageManaPercent, Consumer<Entity> targetAction, Consumer<Pair<Entity, Entity>> biEntityAction, Consumer<Entity> selfAction) {
        this.ManaCostPercent.put(level, ManaCostPercent);
        this.ExtraDamageManaPercent.put(level, ExtraDamageManaPercent);
        this.targetAction.put(level, targetAction);
        this.biEntityAction.put(level, biEntityAction);
        this.selfAction.put(level, selfAction);
    }

    public float getExtraDamage(Double NowMana, int ManaLevel) {
        return (float) (ExtraDamageManaPercent.get(ManaLevel) * NowMana);
    }

    public double getManaCost(Double NowMana, int ManaLevel) {
        return ManaCostPercent.get(ManaLevel) * NowMana;
    }

    public void invokeAction(int level, Entity target) {
        if (selfAction.get(level) != null) {
            selfAction.get(level).accept(this.entity);
        }
        if (targetAction.get(level) != null) {
            targetAction.get(level).accept(target);
        }
        if (biEntityAction.get(level) != null) {
            biEntityAction.get(level).accept(new Pair<>(this.entity, target));
        }
    }

    public float modifyDamageDealt(DamageSource source, float amount, LivingEntity target) {
        if (this.DS_Condition != null && !this.DS_Condition.test(new Pair<>(source, amount))) {
            return amount;
        }
        if (this.TargetCondition != null && !this.TargetCondition.test(target)) {
            return amount;
        }
        if (this.BiEntityCondition != null && !this.BiEntityCondition.test(new Pair<>(this.entity, target))) {
            return amount;
        }
        if (this.entity instanceof PlayerEntity player) {
            double NowMana = ManaUtils.getPlayerMana(player);
            int ManaLevel = Init_CCA.AddonData.get(this.entity).getManaLevel();
            float ExtraDamage = getExtraDamage(NowMana, ManaLevel);
            double ManaCost = getManaCost(NowMana, ManaLevel);
            ManaUtils.consumePlayerMana(player, ManaCost);
            invokeAction(ManaLevel, target);
            return amount + ExtraDamage;
        }
        return amount;
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("leveled_mana_modify_damage_dealt"),
                new SerializableData()
                        .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                        .add("target_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)

                        .add("level_0_extra_damage_mana_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_0_mana_cost_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_0_self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_0_target_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_0_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                        .add("level_1_extra_damage_mana_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_1_mana_cost_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_1_self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_1_target_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_1_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                        .add("level_2_extra_damage_mana_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_2_mana_cost_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_2_self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_2_target_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_2_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                        .add("level_3_extra_damage_mana_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_3_mana_cost_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_3_self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_3_target_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_3_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)

                        .add("level_4_extra_damage_mana_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_4_mana_cost_percent", SerializableDataTypes.DOUBLE, 0.0)
                        .add("level_4_self_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_4_target_action", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_4_bientity_action", ApoliDataTypes.BIENTITY_ACTION, null),
                data -> (type, player) -> {
                    LeveledManaModifyDamageDealtPower power = new LeveledManaModifyDamageDealtPower(type, player, data.get("damage_condition"), data.get("target_condition"), data.get("bientity_condition"));
                    power.addLevelProcessLine(0, data.get("level_0_mana_cost_percent"), data.get("level_0_extra_damage_mana_percent"), data.get("level_0_self_action"), data.get("level_0_target_action"), data.get("level_0_bientity_action"));
                    power.addLevelProcessLine(1, data.get("level_1_mana_cost_percent"), data.get("level_1_extra_damage_mana_percent"), data.get("level_1_self_action"), data.get("level_1_target_action"), data.get("level_1_bientity_action"));
                    power.addLevelProcessLine(2, data.get("level_2_mana_cost_percent"), data.get("level_2_extra_damage_mana_percent"), data.get("level_2_self_action"), data.get("level_2_target_action"), data.get("level_2_bientity_action"));
                    power.addLevelProcessLine(3, data.get("level_3_mana_cost_percent"), data.get("level_3_extra_damage_mana_percent"), data.get("level_3_self_action"), data.get("level_3_target_action"), data.get("level_3_bientity_action"));
                    power.addLevelProcessLine(4, data.get("level_4_mana_cost_percent"), data.get("level_4_extra_damage_mana_percent"), data.get("level_4_self_action"), data.get("level_4_target_action"), data.get("level_4_bientity_action"));
                    return power;
                }
        );
    }
}
