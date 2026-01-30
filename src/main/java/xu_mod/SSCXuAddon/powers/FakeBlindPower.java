package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.config.PlayerCustomConfigData;

import java.util.function.Function;
import java.util.function.Predicate;

public class FakeBlindPower extends Power {
    private boolean useClientConfig = false;
    private boolean hasGiveBlindness = false;

    private Predicate<Entity> localCondition = null;

    public FakeBlindPower(PowerType<?> type, LivingEntity entity, boolean useClientConfig, ConditionFactory<Entity>.Instance localCondition) {
        super(type, entity);
        this.useClientConfig = useClientConfig;
        this.localCondition = localCondition;
        this.setTicking();
    }

    public boolean shouldEnablePower() {
        if (localCondition == null || !localCondition.test(entity)) {
            return false;
        }
        if (useClientConfig) {
            PlayerCustomConfigData pccd = PlayerCustomConfigData.playerCustomConfigDataMap.getOrDefault(entity.getUuid(), null);
            if (pccd != null) {
                return pccd.enableFakeBlindPower;
            }
        }
        return true;
    }

    @Override
    public void tick() {
        if (shouldEnablePower()) {
            if (!entity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 1000000, 0, false, false, true));
                hasGiveBlindness = true;
            }
        } else if (hasGiveBlindness) {
            entity.removeStatusEffect(StatusEffects.BLINDNESS);
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("fake_blind"),
                new SerializableData()
                        .add("use_client_config", SerializableDataTypes.BOOLEAN, false)
                        .add("condition", ApoliDataTypes.ENTITY_CONDITION, null),
                data -> (type, entity) -> new FakeBlindPower(type, entity, data.getBoolean("use_client_config"), data.get("condition"))
        );
    }
}
