package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class ManaToFoodPower extends Power {
    private final int minFoodValue;
    private final float minSaturationValue;
    private final double manaToFoodRatio;
    private final double manaToSaturationRatio;

    public ManaToFoodPower(PowerType<?> type, LivingEntity entity, int minFoodValue, float minSaturationValue, double manaToFoodRatio, double manaToSaturationRatio) {
        super(type, entity);
        this.minFoodValue = minFoodValue;
        this.minSaturationValue = minSaturationValue;
        this.manaToFoodRatio = manaToFoodRatio;
        this.manaToSaturationRatio = manaToSaturationRatio;
        this.setTicking();
    }

    public void tick() {
        if (this.entity.age % 10 != 0) {
            return;
        }
        if (this.entity instanceof PlayerEntity player) {
            HungerManager hungerManager = player.getHungerManager();
            if (hungerManager.getFoodLevel() < this.minFoodValue) {
                hungerManager.add(1, 0);
                ManaUtils.consumePlayerMana(player, this.manaToFoodRatio);
            }
            if (hungerManager.getSaturationLevel() < this.minSaturationValue) {
                hungerManager.setSaturationLevel(hungerManager.getSaturationLevel() + 1);
                ManaUtils.consumePlayerMana(player, this.manaToSaturationRatio);
            }
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("mana_to_food"),
                new SerializableData()
                        .add("min_food_value", SerializableDataTypes.INT, 0)
                        .add("min_saturation_value", SerializableDataTypes.FLOAT, 0.0f)
                        .add("mana_to_food_ratio", SerializableDataTypes.DOUBLE, 20.0d)
                        .add("mana_to_saturation_ratio", SerializableDataTypes.DOUBLE, 10.0d),
                data -> (type, player) -> new ManaToFoodPower(type, player, data.get("min_food_value"), data.getFloat("min_saturation_value"), data.getDouble("mana_to_food_ratio"), data.getDouble("mana_to_saturation_ratio"))
        ).allowCondition();
    }
}
