package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.minion.IPlayerEntityMinion;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.entity.minion.SoulWolfMinion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class WolfSoulPower extends Power {

    private static final double MANA_DISPLAY_OFFSET = -6.0d;
    private static final int LIFESTEAL_COOLDOWN = 10;
    private static final float MIN_LIFESTEAL_DAMAGE = 2.0f;

    private static final UUID ATTACK_UUID = UUID.fromString("7c3e9f2a-4b8d-4e1c-9a6f-2d5b8e3f1a4c");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("a1f4c8d6-2e9b-4f7a-b3c5-8d6e1f2a9b3c");
    private static final UUID MOVE_SPEED_UUID = UUID.fromString("d8b2e6f4-1c7a-4a9e-8f3b-5c2d9e1f7a4b");

    private final int lifestealSoulPerX;
    private final double lifestealPerX;

    private final SoulAttribute attack;
    private final SoulAttribute attackSpeed;
    private final SoulAttribute moveSpeed;
    private final List<SoulAttribute> attributes;

    private int lastLifestealTick = -100;

    public WolfSoulPower(PowerType<?> type, LivingEntity entity, SerializableData.Instance data) {
        super(type, entity);
        this.lifestealSoulPerX = data.get("lifesteal_soul_per_x");
        this.lifestealPerX = data.get("lifesteal_per_x");

        this.attack = new SoulAttribute(
                EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTACK_UUID, "wolf_soul_attack",
                EntityAttributeModifier.Operation.ADDITION,
                data.get("positive_attack_bonus"), data.get("negative_attack_bonus"));
        this.attackSpeed = new SoulAttribute(
                EntityAttributes.GENERIC_ATTACK_SPEED, ATTACK_SPEED_UUID, "wolf_soul_attack_speed",
                EntityAttributeModifier.Operation.MULTIPLY_BASE,
                data.get("positive_attack_speed_bonus"), data.get("negative_attack_speed_bonus"));
        this.moveSpeed = new SoulAttribute(
                EntityAttributes.GENERIC_MOVEMENT_SPEED, MOVE_SPEED_UUID, "wolf_soul_move_speed",
                EntityAttributeModifier.Operation.MULTIPLY_BASE,
                data.get("positive_move_speed_bonus"), data.get("negative_move_speed_bonus"));
        this.attributes = List.of(this.attack, this.attackSpeed, this.moveSpeed);
        this.setTicking(true);
    }

    @Override
    public void onAdded() {
        super.onAdded();
        if (entity.getWorld().isClient) {
            return;
        }
        updateAll(true);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (entity.getWorld().isClient) {
            return;
        }
        for (SoulAttribute slot : attributes) {
            removeModifier(slot);
            slot.lastValue = Double.NaN;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getWorld().isClient) {
            return;
        }
        updateAll(false);
    }

    public void onHit(DamageSource source, float amount, LivingEntity target) {
        if (entity.getWorld().isClient) {
            return;
        }
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        if (amount < MIN_LIFESTEAL_DAMAGE) {
            return;
        }
        double lifesteal = getCurrentLifesteal();
        if (lifesteal <= 0) {
            return;
        }
        int currentTick = player.age;
        if (currentTick - lastLifestealTick < LIFESTEAL_COOLDOWN) {
            return;
        }
        lastLifestealTick = currentTick;

        float missing = player.getMaxHealth() - player.getHealth();
        if (missing <= 0) {
            onOverflowHeal(player, (float) lifesteal);
            return;
        }
        if (lifesteal >= missing) {
            player.heal(missing);
            onOverflowHeal(player, (float) (lifesteal - missing));
        } else {
            player.heal((float) lifesteal);
        }
    }

    protected void onOverflowHeal(PlayerEntity player, float overflow) {
        if (overflow <= 0) {
            return;
        }
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return;
        }
        if (!(player instanceof IPlayerEntityMinion playerMinion)) {
            return;
        }
        List<SoulWolfMinion> wolves = new ArrayList<>();
        for (UUID uuid : playerMinion.shape_shifter_curse$getMinionsByMinionID(SoulWolfMinion.MinionID)) {
            Entity e = world.getEntity(uuid);
            if (e instanceof SoulWolfMinion wolf && wolf.isAlive()) {
                wolves.add(wolf);
            }
        }
        if (wolves.isEmpty()) {
            return;
        }
        wolves.sort(Comparator.comparingDouble(w -> w.getHealth() / Math.max(1.0f, w.getMaxHealth())));
        float remaining = overflow;
        for (SoulWolfMinion wolf : wolves) {
            if (remaining <= 0) break;
            float missing = wolf.getMaxHealth() - wolf.getHealth();
            if (missing <= 0) continue;
            float toHeal = Math.min(remaining, missing);
            wolf.heal(toHeal);
            remaining -= toHeal;
        }
    }

    private void updateAll(boolean force) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        double soul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        for (SoulAttribute slot : attributes) {
            updateModifier(slot, calc(soul, slot.positiveBonus, slot.negativeBonus), force);
        }
    }

    private void updateModifier(SoulAttribute slot, double value, boolean force) {
        if (!force && !Double.isNaN(slot.lastValue) && Math.abs(value - slot.lastValue) <= 1e-6) {
            return;
        }
        EntityAttributeInstance instance = entity.getAttributeInstance(slot.attribute);
        if (instance == null) {
            return;
        }
        EntityAttributeModifier entityAttributeModifier = instance.getModifier(slot.uuid);
        if (entityAttributeModifier != null) {
            instance.removeModifier(slot.uuid);
        }
        if (value != 0) {
            instance.addTemporaryModifier(new EntityAttributeModifier(slot.uuid, slot.name, value, slot.operation));
        }
        slot.lastValue = value;
    }

    private void removeModifier(SoulAttribute slot) {
        EntityAttributeInstance instance = entity.getAttributeInstance(slot.attribute);
        if (instance != null) {
            EntityAttributeModifier entityAttributeModifier = instance.getModifier(slot.uuid);
            if (entityAttributeModifier != null) {
                instance.removeModifier(slot.uuid);
            }
        }
    }

    private static double calc(double soul, double positiveBonus, double negativeBonus) {
        if (soul > 0) {
            return Math.floor(soul) * positiveBonus;
        }
        if (soul < 0) {
            return Math.floor(-soul) * negativeBonus;
        }
        return 0;
    }

    public double getCurrentLifesteal() {
        if (!(entity instanceof PlayerEntity player)) {
            return 0;
        }
        double soul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        if (soul <= 0 || lifestealSoulPerX <= 0) {
            return 0;
        }
        return Math.floor(soul / lifestealSoulPerX) * lifestealPerX;
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("wolf_soul"),
                new SerializableData()
                        .add("positive_attack_bonus", SerializableDataTypes.DOUBLE, 0.5d)
                        .add("positive_attack_speed_bonus", SerializableDataTypes.DOUBLE, 0d)
                        .add("positive_move_speed_bonus", SerializableDataTypes.DOUBLE, 0d)
                        .add("negative_attack_bonus", SerializableDataTypes.DOUBLE, -0.5d)
                        .add("negative_attack_speed_bonus", SerializableDataTypes.DOUBLE, -0.025d)
                        .add("negative_move_speed_bonus", SerializableDataTypes.DOUBLE, -0.02d)
                        .add("lifesteal_soul_per_x", SerializableDataTypes.INT, 6)
                        .add("lifesteal_per_x", SerializableDataTypes.DOUBLE, 0.5d),
                data -> (powerType, entity) -> new WolfSoulPower(powerType, entity, data)
        ).allowCondition();
    }

    private static final class SoulAttribute {
        private final EntityAttribute attribute;
        private final UUID uuid;
        private final String name;
        private final EntityAttributeModifier.Operation operation;
        private final double positiveBonus;
        private final double negativeBonus;
        private double lastValue = Double.NaN;

        private SoulAttribute(EntityAttribute attribute, UUID uuid, String name, EntityAttributeModifier.Operation operation, double positiveBonus, double negativeBonus) {
            this.attribute = attribute;
            this.uuid = uuid;
            this.name = name;
            this.operation = operation;
            this.positiveBonus = positiveBonus;
            this.negativeBonus = negativeBonus;
        }
    }
}