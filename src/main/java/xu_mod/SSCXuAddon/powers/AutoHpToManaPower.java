package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class AutoHpToManaPower extends Power {
    public float tier1ManaPercentStart = 0.0f;
    public float tier1CovHpCost = 0.0f;
    public float tier1CovMana = 0.0f;
    public float tier2ManaPercentStart = 0.0f;
    public float tier2CovHpCost = 0.0f;
    public float tier2CovMana = 0.0f;
    public float tier3ManaPercentStart = 0.0f;
    public float tier3CovHpCost = 0.0f;
    public float tier3CovMana = 0.0f;
    public float tier4ManaPercentStart = 0.0f;
    public float tier4CovHpCost = 0.0f;
    public float tier4CovMana = 0.0f;
    public int tickRate = 40;
    public int nowTick = 0;

    public AutoHpToManaPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
        setTicking();
    }

    @Override
    public void tick() {
        if (nowTick >= tickRate) {
            if (entity instanceof PlayerEntity player) {
                double manaPercent = ManaUtils.getPlayerManaPercent(player, 0.0d);
                if (manaPercent < tier4ManaPercentStart) {
                    player.damage(player.getWorld().getDamageSources().starve(), tier4CovHpCost);
                    ManaUtils.gainPlayerMana(player, tier4CovMana);
                } else if (manaPercent < tier3ManaPercentStart) {
                    player.damage(player.getWorld().getDamageSources().starve(), tier3CovHpCost);
                    ManaUtils.gainPlayerMana(player, tier3CovMana);
                } else if (manaPercent < tier2ManaPercentStart) {
                    player.damage(player.getWorld().getDamageSources().starve(), tier2CovHpCost);
                    ManaUtils.gainPlayerMana(player, tier2CovMana);
                } else if (manaPercent < tier1ManaPercentStart) {
                    player.damage(player.getWorld().getDamageSources().starve(), tier1CovHpCost);
                    ManaUtils.gainPlayerMana(player, tier1CovMana);
                }
            }
        }
        nowTick ++;
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("auto_hp_to_mana"),
                new SerializableData()
                        .add("tier1_mana_percent_start", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier1_cov_hp_cost", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier1_cov_mana", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier2_mana_percent_start", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier2_cov_hp_cost", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier2_cov_mana", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier3_mana_percent_start", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier3_cov_hp_cost", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier3_cov_mana", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier4_mana_percent_start", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier4_cov_hp_cost", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tier4_cov_mana", SerializableDataTypes.FLOAT, 0.0f)
                        .add("tick_rate", SerializableDataTypes.INT, 40),
                data -> (type, entity) -> {
                    AutoHpToManaPower power = new AutoHpToManaPower(type, entity);
                    power.tier1ManaPercentStart = data.getFloat("tier1_mana_percent_start");
                    power.tier1CovHpCost = data.getFloat("tier1_cov_hp_cost");
                    power.tier1CovMana = data.getFloat("tier1_cov_mana");
                    power.tier2ManaPercentStart = data.getFloat("tier2_mana_percent_start");
                    power.tier2CovHpCost = data.getFloat("tier2_cov_hp_cost");
                    power.tier2CovMana = data.getFloat("tier2_cov_mana");
                    power.tier3ManaPercentStart = data.getFloat("tier3_mana_percent_start");
                    power.tier3CovHpCost = data.getFloat("tier3_cov_hp_cost");
                    power.tier3CovMana = data.getFloat("tier3_cov_mana");
                    power.tier4ManaPercentStart = data.getFloat("tier4_mana_percent_start");
                    power.tier4CovHpCost = data.getFloat("tier4_cov_hp_cost");
                    power.tier4CovMana = data.getFloat("tier4_cov_mana");
                    power.tickRate = data.getInt("tick_rate");
                    return power;
                }
        );
    }
}
