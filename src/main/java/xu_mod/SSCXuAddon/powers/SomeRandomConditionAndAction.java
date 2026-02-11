package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.VillageGossipType;
import net.onixary.shapeShifterCurseFabric.minion.MinionRegister;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.cca.AddonDataComponent;
import xu_mod.SSCXuAddon.init.Init_CCA;
import xu_mod.SSCXuAddon.utils.ExtraReputationTypes;
import xu_mod.SSCXuAddon.utils.Interface.IVillagerEntityReputationEX;
import xu_mod.SSCXuAddon.utils.InventoryMenuUtils;
import xu_mod.SSCXuAddon.utils.RaycastUtils;

import java.util.function.Consumer;

public class SomeRandomConditionAndAction {
    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(
                new ActionFactory<>(
                        SSCXuAddon.identifier("trigger_cooldown"),
                        new SerializableData()
                                .add("cooldown_id", SerializableDataTypes.IDENTIFIER, null),
                        (data, entity) -> {
                            Identifier cooldown_id = data.get("cooldown_id");
                            if (entity instanceof PlayerEntity playerEntity && cooldown_id != null) {
                                AddonDataComponent addonDataComponent = Init_CCA.AddonData.get(playerEntity);
                                addonDataComponent.triggerCooldown(cooldown_id);
                            }
                        }
                )
        );
        ActionRegister.accept(
                new ActionFactory<>(
                        SSCXuAddon.identifier("trigger_cooldown_cost_hp"),
                        new SerializableData()
                                .add("cooldown_id", SerializableDataTypes.IDENTIFIER, null)
                                .add("hp_cost_tier1", SerializableDataTypes.INT, 0)
                                .add("hp_cost_tier1_value", SerializableDataTypes.FLOAT, 0.0f)
                                .add("hp_cost_tier2", SerializableDataTypes.INT, 0)
                                .add("hp_cost_tier2_value", SerializableDataTypes.FLOAT, 0.0f)
                                .add("hp_cost_tier3", SerializableDataTypes.INT, 0)
                                .add("hp_cost_tier3_value", SerializableDataTypes.FLOAT, 0.0f)
                                .add("hp_cost_tier4", SerializableDataTypes.INT, 0)
                                .add("hp_cost_tier4_value", SerializableDataTypes.FLOAT, 0.0f),
                        (data, entity) -> {
                            Identifier cooldown_id = data.get("cooldown_id");
                            int hp_cost_tier1 = data.getInt("hp_cost_tier1");
                            int hp_cost_tier2 = data.getInt("hp_cost_tier2");
                            int hp_cost_tier3 = data.getInt("hp_cost_tier3");
                            int hp_cost_tier4 = data.getInt("hp_cost_tier4");
                            float hp_cost_tier1_value = data.getFloat("hp_cost_tier1_value");
                            float hp_cost_tier2_value = data.getFloat("hp_cost_tier2_value");
                            float hp_cost_tier3_value = data.getFloat("hp_cost_tier3_value");
                            float hp_cost_tier4_value = data.getFloat("hp_cost_tier4_value");
                            if (entity instanceof PlayerEntity playerEntity && cooldown_id != null) {
                                AddonDataComponent addonDataComponent = Init_CCA.AddonData.get(playerEntity);
                                double PastCooldown = playerEntity.getWorld().getTime() - addonDataComponent.getCooldown(cooldown_id, -1728000L);
                                if (PastCooldown < hp_cost_tier4 && hp_cost_tier4_value > 0.0f) {
                                    playerEntity.damage(playerEntity.getDamageSources().magic(), hp_cost_tier4_value);
                                } else if (PastCooldown < hp_cost_tier3 && hp_cost_tier3_value > 0.0f) {
                                    playerEntity.damage(playerEntity.getDamageSources().magic(), hp_cost_tier3_value);
                                } else if (PastCooldown < hp_cost_tier2 && hp_cost_tier2_value > 0.0f) {
                                    playerEntity.damage(playerEntity.getDamageSources().magic(), hp_cost_tier2_value);
                                } else if (PastCooldown < hp_cost_tier1 && hp_cost_tier1_value > 0.0f) {
                                    playerEntity.damage(playerEntity.getDamageSources().magic(), hp_cost_tier1_value);
                                }
                                addonDataComponent.triggerCooldown(cooldown_id);
                            }
                        }
                )
        );
        ActionRegister.accept(
                new ActionFactory<>(
                        SSCXuAddon.identifier("reset_cooldown"),
                        new SerializableData()
                                .add("cooldown_id", SerializableDataTypes.IDENTIFIER, null),
                        (data, entity) -> {
                            Identifier cooldown_id = data.get("cooldown_id");
                            if (entity instanceof PlayerEntity playerEntity && cooldown_id != null) {
                                AddonDataComponent addonDataComponent = Init_CCA.AddonData.get(playerEntity);
                                addonDataComponent.resetCooldown(cooldown_id);
                            }
                        }
                )
        );
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("area_enemy_forget"),
                new SerializableData()
                        .add("range", SerializableDataTypes.FLOAT, 40.0f)
                        .add("only_self", SerializableDataTypes.BOOLEAN, false),
                (data, entity) -> {
                    float range = data.getFloat("range");
                    boolean onlySelf = data.getBoolean("only_self");
                    if (entity instanceof PlayerEntity player) {
                        player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(range), e -> (e instanceof MobEntity mob && (!onlySelf || (mob.getTarget() != null && mob.getTarget().equals(player))))).forEach(otherEntity -> {
                            MobEntity mobEntity = (MobEntity) otherEntity;
                            mobEntity.setTarget(null);
                            mobEntity.targetSelector.getGoals().forEach(
                                    PrioritizedGoal::stop
                            );
                            mobEntity.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
                        });
                    }
                }
        ));

        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("random_teleport"),
                new SerializableData()
                        .add("range", SerializableDataTypes.INT, 8)
                        .add("min_entity_range", SerializableDataTypes.FLOAT, 3.0f)
                        .add("max_try", SerializableDataTypes.INT, 8),
                (data, entity) -> {
                    int range = data.getInt("range");
                    float minEntityRange = data.getFloat("min_entity_range");
                    int maxTry = data.getInt("max_try");
                    if (entity.getWorld().isClient) {
                        return;
                    }
                    if (entity instanceof PlayerEntity player) {
                        Random random = player.getRandom();
                        for (int i = 0; i < maxTry; i++) {
                            BlockPos pos = MinionRegister.getNearbyEmptySpace(player.getWorld(), random, player.getBlockPos(), range, 3, 2, 3);
                            if (pos != null) {
                                Vec3d posVec = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                                if (player.getWorld().getOtherEntities(player, new Box(posVec.add(-minEntityRange, -minEntityRange, -minEntityRange), posVec.add(minEntityRange, minEntityRange, minEntityRange)), e -> e instanceof MobEntity && !(e instanceof PlayerEntity)).isEmpty()) {
                                    player.teleport(posVec.getX(), posVec.getY(), posVec.getZ());
                                    break;
                                }
                            }
                        }
                        BlockPos pos = MinionRegister.getNearbyEmptySpace(player.getWorld(), random, player.getBlockPos(), range, 3, 2, 8);
                        if (pos != null) {
                            player.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        }
                    }
                }
        ));

        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("teleport"),
                new SerializableData()
                        .add("range", SerializableDataTypes.DOUBLE, 8.0d)
                        .add("actor_action", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> {
                    double range = data.getDouble("range");
                    ActionFactory<Entity>.Instance actorAction = data.get("actor_action");
                    if (entity.getWorld().isClient) {
                        return;
                    }
                    if (entity instanceof PlayerEntity player) {
                        Vec3d targetPos = RaycastUtils.getPlayerTargetPos(player, range);
                        player.teleport(targetPos.getX(), targetPos.getY(), targetPos.getZ());
                        if (actorAction != null) {
                            actorAction.accept(player);
                        }
                    }
                }
        ));
        BIActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("add_gossip_trade"),
                new SerializableData()
                        .add("value", SerializableDataTypes.INT, 0),
                (data, entityPair) -> {
                    int value = data.getInt("value");
                    Entity actor = entityPair.getLeft();
                    Entity target = entityPair.getRight();
                    if (actor instanceof PlayerEntity player && target instanceof VillagerEntity villager) {
                        villager.getGossip().startGossip(player.getUuid(), VillageGossipType.TRADING, value);
                    }
                }
        ));

        BIActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("add_gossip_extra"),
                new SerializableData()
                        .add("extra_reputation_type", SerializableDataTypes.STRING, null)
                        .add("gossip_base", SerializableDataTypes.DOUBLE, 0.0d)
                        .add("gossip_target", SerializableDataTypes.DOUBLE, 100.0d)
                        .add("gossip_add_factor", SerializableDataTypes.DOUBLE, 0.15d),
                (data, entityPair) -> {
                    String extraReputationType = data.get("extra_reputation_type");
                    if (extraReputationType == null) {
                        return;
                    }
                    ExtraReputationTypes extraReputationTypes;
                    try {
                        extraReputationTypes = ExtraReputationTypes.valueOf(extraReputationType);
                    } catch (IllegalArgumentException e) {
                        return;
                    }
                    double gossipBase = data.getDouble("gossip_base");
                    double gossipTarget = data.getDouble("gossip_target");
                    double gossipAddFactor = data.getDouble("gossip_add_factor");
                    Entity actor = entityPair.getLeft();
                    Entity target = entityPair.getRight();
                    if (actor instanceof PlayerEntity player && target instanceof IVillagerEntityReputationEX villager) {
                        double extra_gossip = villager.SSC_Xu_Addon$getExtraReputation(player.getUuid(), extraReputationTypes);
                        extra_gossip += Math.max(gossipBase, gossipAddFactor * (gossipTarget - extra_gossip));
                        villager.SSC_Xu_Addon$setExtraReputation(player.getUuid(), extraReputationTypes, extra_gossip);
                    }
                })
        );
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("restock_trade"),
                new SerializableData(),
                (data, entity) -> {
                    if (entity instanceof VillagerEntity villager) {
                        villager.restock();
                    }
                }
        ));
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("open_player_space_bag"),
                new SerializableData()
                        .add("slot_level", SerializableDataTypes.INT, 2),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity player) {
                        int slotLevel = data.getInt("slot_level");
                        if (slotLevel == 1 || slotLevel == 2 || slotLevel == 3) {
                            InventoryMenuUtils.openPlayerSpaceBag(player, slotLevel);
                        }
                    }
                }
        ));
    }


    public static void registerConditions(Consumer<ConditionFactory<Entity>> ConditionRegister) {
        ConditionRegister.accept(
                new ConditionFactory<>(
                        SSCXuAddon.identifier("is_not_cooldown"),
                        new SerializableData()
                                .add("cooldown_id", SerializableDataTypes.IDENTIFIER, null)
                                .add("cooldown_time", SerializableDataTypes.INT, 0),
                        (data, entity) -> {
                            Identifier cooldown_id = data.get("cooldown_id");
                            int cooldown_time = data.get("cooldown_time");
                            if (entity instanceof PlayerEntity playerEntity && cooldown_id != null) {
                                AddonDataComponent addonDataComponent = Init_CCA.AddonData.get(playerEntity);
                                return addonDataComponent.isNotInCooldown(cooldown_id, cooldown_time);
                            }
                            return false;
                        }
                )
        );
    }
}
