package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class AllayPower extends Power {
    // 伪线性有点消耗性能 精度越高 性能消耗越高 5%的精度得有40个Power 我还是直接写一个Power吧 精度大 性能消耗低

    public static TagKey<DamageType> AE_Bypass_Damage_Reduce = TagKey.of(RegistryKeys.DAMAGE_TYPE, SSCXuAddon.identifier("ae_bypass_damage_reduce"));

    public boolean ReverseManaPercent;
    public float DamageDealtModifier;
    public float DamageTakenModifier;
    public float DamageDealtManaGainModifier;
    public float DamageTakenManaGainModifier;

    public AllayPower(PowerType<?> type, LivingEntity entity, Boolean ReverseManaPercent, float DamageDealtModifier, float DamageTakenModifier, float DamageDealtManaGainModifier, float DamageTakenManaGainModifier) {
        super(type, entity);
        this.ReverseManaPercent = ReverseManaPercent;
        this.DamageDealtModifier = DamageDealtModifier;
        this.DamageTakenModifier = DamageTakenModifier;
        this.DamageDealtManaGainModifier = DamageDealtManaGainModifier;
        this.DamageTakenManaGainModifier = DamageTakenManaGainModifier;
    }

    public float modifyDamageDealt(DamageSource source, float amount, LivingEntity target) {
        if (this.entity instanceof PlayerEntity player) {
            double ManaPercent = ManaUtils.getPlayerManaPercent(player, 0);
            if (ReverseManaPercent) {
                ManaPercent = 1 - ManaPercent;
            }
            float Modifier = 1.0f + ((float) ManaPercent * DamageDealtModifier);
            if (DamageDealtManaGainModifier > 0) {
                ManaUtils.gainPlayerMana(player, DamageDealtManaGainModifier * amount);
            } else if (DamageDealtManaGainModifier < 0) {
                ManaUtils.consumePlayerMana(player, -DamageDealtManaGainModifier * amount);
            }
            return amount * Modifier;
        }
        return amount;
    }

    public float modifyDamageTaken(DamageSource source, float amount, Entity attacker) {
        if (source.isIn(AE_Bypass_Damage_Reduce)) {
            return amount;
        }
        if (this.entity instanceof PlayerEntity player) {
            double ManaPercent = ManaUtils.getPlayerManaPercent(player, 0);
            if (ReverseManaPercent) {
                ManaPercent = 1 - ManaPercent;
            }
            float Modifier = 1.0f + ((float) ManaPercent * DamageTakenModifier);
            if (DamageTakenManaGainModifier > 0) {
                ManaUtils.gainPlayerMana(player, DamageTakenManaGainModifier * amount);
            } else if (DamageTakenManaGainModifier < 0) {
                ManaUtils.consumePlayerMana(player, -DamageTakenManaGainModifier * amount);
            }
            return amount * Modifier;
        }
        return amount;
    }


    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("allay_power"),
                new SerializableData()
                        .add("reverse_mana_percent", SerializableDataTypes.BOOLEAN, false)
                        .add("damage_dealt_modifier", SerializableDataTypes.FLOAT, 0.0f)
                        .add("damage_taken_modifier", SerializableDataTypes.FLOAT, 0.0f)
                        .add("damage_dealt_mana_gain_modifier", SerializableDataTypes.FLOAT, 0.0f)
                        .add("damage_taken_mana_gain_modifier", SerializableDataTypes.FLOAT, 0.0f),
                data -> (type, player) -> new AllayPower(type, player, data.getBoolean("reverse_mana_percent"), data.getFloat("damage_dealt_modifier"), data.getFloat("damage_taken_modifier"), data.getFloat("damage_dealt_mana_gain_modifier"), data.getFloat("damage_taken_mana_gain_modifier"))
        ).allowCondition();
    }
}
