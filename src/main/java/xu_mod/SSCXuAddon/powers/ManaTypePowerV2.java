package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.additional_power.ManaTypePower;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import org.jetbrains.annotations.Nullable;
import xu_mod.SSCXuAddon.SSCXuAddon;

// 当初写的时候忘了流配置项了 当时SSC作者说要获得Power时满魔力 我就直接硬编码了
public class ManaTypePowerV2 extends ManaTypePower {
    private @Nullable Identifier manaType = null;
    private Identifier manaSource = null;
    private boolean DefaultMax;
    private boolean DefaultMin;

    public ManaTypePowerV2(PowerType<?> type, LivingEntity entity, Identifier manaType, Identifier manaSource, boolean DefaultMax, boolean DefaultMin) {
        super(type, entity, manaType, manaSource);
        this.DefaultMax = DefaultMax;
        this.DefaultMin = DefaultMin;
        this.manaType = manaType;
        if (manaSource == null) {
            this.manaSource = type.getIdentifier();
        } else {
            this.manaSource = manaSource;
        }
    }

    @Override
    public void onGained() {
        LivingEntity var2 = this.entity;
        if (var2 instanceof ServerPlayerEntity playerEntity) {
            if (this.manaType != null) {
                if (!ManaUtils.isManaTypeExists(playerEntity, this.manaType, this.manaSource)) {
                    ManaUtils.gainManaTypeID(playerEntity, this.manaType, this.manaSource);
                }
                if (this.DefaultMax) {
                    ManaUtils.gainPlayerMana(playerEntity, Double.MAX_VALUE / 16);
                }
                if (this.DefaultMin) {
                    ManaUtils.setPlayerMana(playerEntity, 0.0f);
                }
            }
        }

    }


    @Override
    public void onRespawn() {
        LivingEntity var2 = this.entity;
        if (var2 instanceof ServerPlayerEntity playerEntity) {
            if (this.manaType != null) {
                if (!ManaUtils.isManaTypeExists(playerEntity, this.manaType, this.manaSource)) {
                    ManaUtils.gainManaTypeID(playerEntity, this.manaType, this.manaSource);
                }
                if (this.DefaultMax) {
                    ManaUtils.gainPlayerMana(playerEntity, Double.MAX_VALUE / 16);
                }
                if (this.DefaultMin) {
                    ManaUtils.setPlayerMana(playerEntity, 0.0f);
                }
            }
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("mana_type_power_pro"),
                new SerializableData()
                        .add("mana_type", SerializableDataTypes.IDENTIFIER, null)
                        .add("mana_source", SerializableDataTypes.IDENTIFIER, null)
                        .add("default_max", SerializableDataTypes.BOOLEAN, false)
                        .add("default_min", SerializableDataTypes.BOOLEAN, false),
                (data) -> (type, entity) ->
                        new ManaTypePowerV2(type, entity, data.get("mana_type"), data.get("mana_source"), data.get("default_max"), data.get("default_min"))
        );
    }
}
