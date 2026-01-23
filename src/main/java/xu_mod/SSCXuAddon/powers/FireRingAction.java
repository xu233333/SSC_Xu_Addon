package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import xu_mod.SSCXuAddon.SSCXuAddon;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;

public class FireRingAction {
    public static class FireRingData {
        public final Vec3d RingMiddle;
        public final float Config_RingStart;
        public final float Config_RingRadius;
        public final int Config_RingHeight;
        public final float Config_RingSpeed;
        public final float Config_DamageRingSize;
        public float RingRadius;
        public PlayerEntity RingOwner;
        public float TickRate = 5;
        private float nowTick = 0;
        public boolean shouldExist = true;
        public final float RingDamage;

        public FireRingData(PlayerEntity RingOwner, float Config_RingStart, float Config_RingRadius, int Config_RingHeight, float Config_RingSpeed, float RingDamage, float DamageRingSize, float TickRate) {
            this.RingOwner = RingOwner;
            this.RingMiddle = RingOwner.getPos();
            this.Config_RingStart = Config_RingStart;
            this.Config_RingRadius = Config_RingRadius;
            this.Config_RingHeight = Config_RingHeight;
            this.Config_RingSpeed = Config_RingSpeed;
            this.RingRadius = Config_RingStart;
            this.RingDamage = RingDamage;
            this.Config_DamageRingSize = DamageRingSize;
            this.TickRate = TickRate;
        }


        public static Set<Entity> CreateRingParticle(ServerPlayerEntity player, ParticleEffect particleEffect, Vec3d RingMiddle, float RingRadius, int RingHeight, float RingDamageSize) {
            HashSet<Entity> entities = new HashSet<>();
            ServerWorld world = player.getServerWorld();
            float speed = 0.04f;
            Vec3d spread = new Vec3d(0.5, 0.5, 0.5);
            int sampleCount = (int)(2 * Math.PI * (double) RingRadius);
            Vec3d delta = spread.multiply(player.getWidth(), player.getEyeHeight(player.getPose()), player.getWidth());
            for (int i = 0; i < sampleCount; i++) {
                double angle = 2 * Math.PI * i / sampleCount;
                double xOffset = (double) RingRadius * Math.cos(angle);
                double zOffset = (double) RingRadius * Math.sin(angle);
                for (ServerPlayerEntity otherPlayer : world.getPlayers()) {
                    for (float yExtra = -RingHeight, yStep = 0.5f; yExtra < RingHeight; yExtra += yStep) {
                        Vec3d particlePos = RingMiddle.add(xOffset, yExtra, zOffset);
                        world.spawnParticles(
                                otherPlayer,
                                particleEffect,
                                true,
                                particlePos.getX(),
                                particlePos.getY(),
                                particlePos.getZ(),
                                1,
                                delta.getX(),
                                delta.getY(),
                                delta.getZ(),
                                speed
                        );
                        entities.addAll(world.getOtherEntities(
                                player,
                                new Box(particlePos.add(-RingDamageSize, 0, -RingDamageSize), particlePos.add(RingDamageSize, 0.5f, RingDamageSize)),
                                entity -> true)
                        );
                    }
                }
            }
            return entities;
        }

        public void Tick() {
            if (!(RingOwner instanceof ServerPlayerEntity)) {
                shouldExist = false;
                return;
            }
            if (nowTick >= TickRate) {
                nowTick = 0;
                Set<Entity> affectEntities = CreateRingParticle((ServerPlayerEntity)RingOwner, ParticleTypes.FLAME, RingMiddle, RingRadius, Config_RingHeight, Config_DamageRingSize);
                for (Entity entity : affectEntities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.damage(RingOwner.getWorld().getDamageSources().playerAttack(RingOwner), RingDamage);
                        livingEntity.takeKnockback(0.5f, RingMiddle.getX() - livingEntity.getX(), RingMiddle.getZ() - livingEntity.getZ());
                        livingEntity.setFireTicks(livingEntity.getFireTicks() + 25);
                    }
                }
                if (RingRadius >= Config_RingRadius) {
                    shouldExist = false;
                }
            }
            nowTick += 1;
            RingRadius += Config_RingSpeed;
        }
    }

    public static LinkedList<FireRingData> FireRingList = new LinkedList<>();

    public static void Tick() {
        for (FireRingData ringData : FireRingList) {
            ringData.Tick();
        }
        FireRingList.removeIf(ringData -> !ringData.shouldExist);
    }

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("fire_ring"),
                new SerializableData()
                        .add("ring_damage", SerializableDataTypes.FLOAT, 2.0f)
                        .add("ring_radius", SerializableDataTypes.FLOAT, 2.0f)
                        .add("ring_speed", SerializableDataTypes.FLOAT, 0.04f)
                        .add("ring_start_radius", SerializableDataTypes.FLOAT, 0.0f)
                        .add("ring_height", SerializableDataTypes.INT, 3)
                        .add("ring_damage_size", SerializableDataTypes.FLOAT, 1.0f)
                        .add("ring_tick_rate", SerializableDataTypes.INT, 5)
                        .add("owner_action", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> {
                    if (entity instanceof ServerPlayerEntity player) {
                        FireRingList.add(
                                new FireRingData(
                                        player,
                                        data.getFloat("ring_start_radius"),
                                        data.getFloat("ring_radius"),
                                        data.getInt("ring_height"),
                                        data.getFloat("ring_speed"),
                                        data.getFloat("ring_damage"),
                                        data.getFloat("ring_damage_size"),
                                        data.getInt("ring_tick_rate")
                                )
                        );
                        ActionFactory<Entity>.Instance ownerAction = data.get("owner_action");
                        if (ownerAction != null) {
                            ownerAction.accept(player);
                        }
                    }
                }
        ));
    }
}
