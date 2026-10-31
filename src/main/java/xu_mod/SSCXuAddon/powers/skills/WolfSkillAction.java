package xu_mod.SSCXuAddon.powers.skills;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.minion.IPlayerEntityMinion;
import xu_mod.SSCXuAddon.data.entity.minion.SoulWolfMinion;
import xu_mod.SSCXuAddon.init.Init_Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class WolfSkillAction {

    private static final double MANA_DISPLAY_OFFSET = -6.0d;

    public static void judgment(SerializableData.Instance data, Entity owner) {
        if (!(owner instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!(player.getWorld() instanceof ServerWorld world)) {
            return;
        }

        double soul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        if (soul < 0) {
            return;
        }

        double range = data.getDouble("range");
        double healthCostPercent = data.getDouble("health_cost_percent");
        double damageCapMultiplier = data.getDouble("damage_cap_multiplier");
        double trueDamagePercent = data.getDouble("true_damage_percent");
        double soulNormalPercent = data.getDouble("soul_normal_percent");
        double soulFlatMultiplier = data.getDouble("soul_flat_multiplier");
        double countBase = data.getDouble("count_factor_base");
        double countWeight = data.getDouble("count_factor_weight");
        double countSlope = data.getDouble("count_factor_slope");
        double distanceWeight = data.getDouble("distance_factor_weight");
        double knockback = data.getDouble("knockback");

        Box box = player.getBoundingBox().expand(range);
        List<LivingEntity> targets = world.getEntitiesByClass(LivingEntity.class, box,
                e -> e != player && e.isAlive() && e.distanceTo(player) <= range && !isOwnerMinion(player, e));
        if (targets.isEmpty()) {
            return;
        }

        float currentHealth = player.getHealth();
        float cost = (float) (currentHealth * healthCostPercent);
        if (currentHealth - cost < 1.0f) {
            cost = currentHealth - 1.0f;
        }
        if (cost <= 0) {
            return;
        }
        player.setHealth(currentHealth - cost);

        double cap = cost * damageCapMultiplier;
        int count = targets.size();
        double countFactor = countBase + countWeight / (1.0d + countSlope * (count - 1));

        for (LivingEntity target : targets) {
            if (!target.isAlive()) {
                continue;
            }
            double distance = target.distanceTo(player);
            double distanceFactor = ((range - distance) / range) * distanceWeight + (1.0d - distanceWeight);

            double baseTrue = trueDamagePercent * target.getMaxHealth();
            double baseNormal = soulNormalPercent * soul * target.getMaxHealth();
            double totalY1 = baseTrue + baseNormal;

            double trueDamage;
            double normalDamage;
            if (totalY1 > cap) {
                double scale = cap / totalY1;
                trueDamage = baseTrue * scale;
                normalDamage = baseNormal * scale;
            } else {
                trueDamage = baseTrue;
                normalDamage = baseNormal;
            }

            double y2 = soul * soulFlatMultiplier * distanceFactor * countFactor;
            double totalNormal = normalDamage + y2;

            Vec3d dir = target.getPos().subtract(player.getPos());
            Vec3d flat = new Vec3d(dir.x, 0, dir.z);
            if (flat.lengthSquared() > 1.0E-6) {
                flat = flat.normalize();
                target.takeKnockback(knockback, -flat.x, -flat.z);
            }

            if (totalNormal > 0) {
                target.damage(world.getDamageSources().playerAttack(player), (float) totalNormal);
            }
            if (trueDamage > 0 && target.isAlive()) {
                float newHealth = (float) (target.getHealth() - trueDamage);
                target.setHealth(Math.max(0.0f, newHealth));
            }
        }

        world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, player.getSoundCategory(), 1.0f, 0.5f);
        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_FIRE_AMBIENT, player.getSoundCategory(), 1.0f, 0.5f);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, player.getSoundCategory(), 1.0f, 0.5f);
        world.spawnParticles(player, ParticleTypes.SOUL_FIRE_FLAME, true,
                player.getX(), player.getY() + 1.0d, player.getZ(), 40, 2.0d, 1.0d, 2.0d, 0.1d);

        ActionFactory<Entity>.Instance ownerAction = data.get("owner_action");
        if (ownerAction != null) {
            ownerAction.accept(owner);
        }
    }

    private static boolean isOwnerMinion(ServerPlayerEntity player, LivingEntity entity) {
        return entity instanceof SoulWolfMinion wolf && player.getUuid().equals(wolf.getOwnerUuid());
    }

    public static void merge(SerializableData.Instance data, Entity owner) {
        if (!(owner instanceof ServerPlayerEntity player)) {
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
        int sumLevel = 0;
        for (SoulWolfMinion wolf : wolves) {
            sumLevel += wolf.level;
        }

        double rawSoul = ManaUtils.getPlayerMana(player) + MANA_DISPLAY_OFFSET;
        int soul = (int) Math.floor(rawSoul);
        double rawMaxSoul = ManaUtils.getPlayerMaxMana(player) + MANA_DISPLAY_OFFSET;
        int floorMaxSoul = (int) Math.floor(rawMaxSoul);

        int owed = Math.max(0, -soul);
        int positiveSoul = Math.max(0, soul);
        int total = positiveSoul + sumLevel;
        int soulAfter = Math.min(total, floorMaxSoul);
        int overflow = total - soulAfter;
        int points = overflow + sumLevel / 2 - 2 * owed;
        if (points < 0) {
            points = 0;
        }

        for (SoulWolfMinion wolf : wolves) {
            world.playSound(null, wolf.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, player.getSoundCategory(), 0.6f, 1.2f);
            playerMinion.shape_shifter_curse$removeMinion(SoulWolfMinion.MinionID, wolf.getUuid());
            wolf.discard();
        }

        ManaUtils.gainPlayerMana(player, sumLevel);

        int soulLockTicks = points * 20;
        int absorptionLevel = points / 4;
        int strengthLevel = points / 6;
        int saturationTick = points * 20;

        if (soulLockTicks > 0) {
            player.removeStatusEffect(Init_Effect.SOUL_LOCK);
            player.addStatusEffect(new StatusEffectInstance(Init_Effect.SOUL_LOCK, soulLockTicks, 0, false, true));
        }
        if (absorptionLevel > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 2400, absorptionLevel - 1, false, true));
        }
        if (strengthLevel > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, strengthLevel - 1, false, true));
        }
        if (saturationTick > 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, saturationTick, 0, false, true));
        }

        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_WITHER_SPAWN, player.getSoundCategory(), 0.5f, 1.5f);
        world.spawnParticles(player, ParticleTypes.SOUL_FIRE_FLAME, true, player.getX(), player.getY() + 1.0d, player.getZ(), 30, 1.0d, 1.0d, 1.0d, 0.1d);

        ActionFactory<Entity>.Instance ownerAction = data.get("owner_action");
        if (ownerAction != null) {
            ownerAction.accept(owner);
        }
    }

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                xu_mod.SSCXuAddon.SSCXuAddon.identifier("wolf_merge"),
                new SerializableData()
                        .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> merge(data, entity)
        ));
        ActionRegister.accept(new ActionFactory<>(
                xu_mod.SSCXuAddon.SSCXuAddon.identifier("wolf_judgment"),
                new SerializableData()
                        .add("range", SerializableDataTypes.DOUBLE, 5.0d)
                        .add("health_cost_percent", SerializableDataTypes.DOUBLE, 0.25d)
                        .add("damage_cap_multiplier", SerializableDataTypes.DOUBLE, 10.0d)
                        .add("true_damage_percent", SerializableDataTypes.DOUBLE, 0.25d)
                        .add("soul_normal_percent", SerializableDataTypes.DOUBLE, 0.01d)
                        .add("soul_flat_multiplier", SerializableDataTypes.DOUBLE, 2.0d)
                        .add("count_factor_base", SerializableDataTypes.DOUBLE, 0.25d)
                        .add("count_factor_weight", SerializableDataTypes.DOUBLE, 0.75d)
                        .add("count_factor_slope", SerializableDataTypes.DOUBLE, 0.5d)
                        .add("distance_factor_weight", SerializableDataTypes.DOUBLE, 0.5d)
                        .add("knockback", SerializableDataTypes.DOUBLE, 0.5d)
                        .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> judgment(data, entity)
        ));
    }
}