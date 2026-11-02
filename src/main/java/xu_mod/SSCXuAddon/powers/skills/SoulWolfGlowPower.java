package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.power.EntityGlowPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.entity.minion.SoulWolfMinion;

public class SoulWolfGlowPower extends EntityGlowPower {

    private static final float[][] LEVEL_COLORS = {
            {0.2f, 0.9f, 0.2f},
            {0.2f, 0.5f, 1.0f},
            {0.65f, 0.3f, 1.0f},
            {1.0f, 0.7f, 0.1f}
    };

    private int renderLevel = 1;

    public SoulWolfGlowPower(PowerType<?> type, LivingEntity entity) {
        super(type, entity, null, null, false, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public boolean doesApply(Entity e) {
        if (!(e instanceof SoulWolfMinion wolf)) {
            return false;
        }
        if (wolf.getOwnerUuid() == null || !this.entity.getUuid().equals(wolf.getOwnerUuid())) {
            return false;
        }
        if (!this.isActive()) {
            return false;
        }
        this.renderLevel = Math.max(1, Math.min(4, wolf.getLevel()));
        return true;
    }

    @Override
    public float getRed() {
        return LEVEL_COLORS[renderLevel - 1][0];
    }

    @Override
    public float getGreen() {
        return LEVEL_COLORS[renderLevel - 1][1];
    }

    @Override
    public float getBlue() {
        return LEVEL_COLORS[renderLevel - 1][2];
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("soul_wolf_glow"),
                new SerializableData(),
                data -> (type, entity) -> new SoulWolfGlowPower(type, entity)
        ).allowCondition();
    }
}