package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.ActiveCooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_CCA;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class LeveledManaPower extends ActiveCooldownPower {
    private int ManaLevel = 0;
    private final int ToggleManaLevelMax;
    private final int ToggleManaLevelMin;
    private final int FallBackManaLevel;
    private final HashMap<Integer, Predicate<Entity>> ManaLevelConditions = new HashMap<>();
    private final HashMap<Integer, Integer> ManaLevelFallBack = new HashMap<>();

    public int getManaLevel() {
        return ManaLevel;
    }

    public void SetManaLevel(int ManaLevel) {
        this.ManaLevel = ManaLevel;
        updateManaLevel(this.entity, this.ManaLevel);
    }

    public void addManaLevelCondition(int ManaLevel, Predicate<Entity> condition, int FallBackLevel) {
        ManaLevelConditions.put(ManaLevel, condition);
        ManaLevelFallBack.put(ManaLevel, FallBackLevel);
    }

    public static void updateManaLevel(LivingEntity entity, int ManaLevel) {
        if (entity instanceof PlayerEntity player) {
            Init_CCA.AddonData.get(player).setManaLevel(ManaLevel);
            Init_CCA.AddonData.sync(player);
        }
    }

    public void ToggleManaLevel() {
        if (this.ManaLevel < this.ToggleManaLevelMin || this.ManaLevel > this.ToggleManaLevelMax) {
            return;
        }
        int finalManaLevel = this.FallBackManaLevel;
        for (int i = 0; i <= ToggleManaLevelMax; i++) {
            int newManaLevel = this.ManaLevel + 1;
            if (newManaLevel > ToggleManaLevelMax) {
                newManaLevel = ToggleManaLevelMin;
            }
            if (ManaLevelConditions.get(newManaLevel) == null || ManaLevelConditions.get(newManaLevel).test(this.entity)) {
                finalManaLevel = newManaLevel;
                break;
            }
        }
        this.ManaLevel = finalManaLevel;
        if (this.entity instanceof ServerPlayerEntity player) {
            player.sendMessage(Text.translatable("message.ssc_xu_addon.power.toggle_mana_level", this.ManaLevel), true);
        }
        updateManaLevel(this.entity, this.ManaLevel);
    }

    @Override
    public void onUse() {
        if(canUse()) {
            this.ToggleManaLevel();
        }
    }

    public LeveledManaPower(PowerType<?> type, LivingEntity entity, int ToggleManaLevelMin, int ToggleManaLevelMax, int FallBackManaLevel) {
        super(type, entity, 1, HudRender.DONT_RENDER, null);
        this.ToggleManaLevelMax = ToggleManaLevelMax;
        this.ToggleManaLevelMin = ToggleManaLevelMin;
        this.ManaLevel = FallBackManaLevel;
        this.FallBackManaLevel = FallBackManaLevel;
        this.setTicking();
    }

    private boolean TestNowManaLevelValid() {
        return ManaLevelConditions.get(this.ManaLevel) == null || ManaLevelConditions.get(this.ManaLevel).test(this.entity);
    }

    private int getFallBackManaLevel() {
        int finalManaLevel = this.ManaLevelFallBack.getOrDefault(this.ManaLevel, this.FallBackManaLevel);
        if (finalManaLevel == -1) {
            return this.FallBackManaLevel;
        }
        return finalManaLevel;
    }

    @Override
    public void tick() {
        if (this.entity instanceof PlayerEntity player) {
            if (!this.TestNowManaLevelValid()) {
                this.SetManaLevel(this.getFallBackManaLevel());
            }
        }
    }


    @Override
    public NbtElement toTag() {
        NbtCompound powerNBT = new NbtCompound();
        powerNBT.putLong("last_use_time", this.lastUseTime);
        powerNBT.putInt("mana_level", this.ManaLevel);
        return powerNBT;
    }

    @Override
    public void fromTag(NbtElement tag) {
        if (tag instanceof NbtCompound powerNBT) {
            this.lastUseTime = powerNBT.getLong("last_use_time");
            this.ManaLevel = powerNBT.getInt("mana_level");
            updateManaLevel(this.entity, this.ManaLevel);
        }
    }

    public static PowerFactory<?> createFactory() {
        return new PowerFactory<>(
                SSCXuAddon.identifier("leveled_mana"),
                new SerializableData()
                        .add("toggle_mana_level_min", SerializableDataTypes.INT, 1)
                        .add("toggle_mana_level_max", SerializableDataTypes.INT, 3)
                        .add("mana_level_0_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("mana_level_0_fallback_level", SerializableDataTypes.INT, -1)
                        .add("mana_level_1_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("mana_level_1_fallback_level", SerializableDataTypes.INT, -1)
                        .add("mana_level_2_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("mana_level_2_fallback_level", SerializableDataTypes.INT, -1)
                        .add("mana_level_3_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("mana_level_3_fallback_level", SerializableDataTypes.INT, -1)
                        .add("mana_level_4_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("mana_level_4_fallback_level", SerializableDataTypes.INT, -1)
                        .add("fallback_mana_level", SerializableDataTypes.INT, 1)
                        .add("key", ApoliDataTypes.BACKWARDS_COMPATIBLE_KEY, new Active.Key()),
                data -> (type, player) -> {
                    LeveledManaPower power = new LeveledManaPower(type, player,
                            data.getInt("toggle_mana_level_min"),
                            data.getInt("toggle_mana_level_max"),
                            data.getInt("fallback_mana_level")
                    );
                    power.addManaLevelCondition(0, data.get("mana_level_0_condition"), data.getInt("mana_level_0_fallback_level"));
                    power.addManaLevelCondition(1, data.get("mana_level_1_condition"), data.getInt("mana_level_1_fallback_level"));
                    power.addManaLevelCondition(2, data.get("mana_level_2_condition"), data.getInt("mana_level_2_fallback_level"));
                    power.addManaLevelCondition(3, data.get("mana_level_3_condition"), data.getInt("mana_level_3_fallback_level"));
                    power.addManaLevelCondition(4, data.get("mana_level_4_condition"), data.getInt("mana_level_4_fallback_level"));
                    power.setKey((Active.Key)data.get("key"));
                    return power;
                }
        );
    }

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("set_mana_level"),
                new SerializableData()
                        .add("mana_level", SerializableDataTypes.INT, 1),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity player) {
                        PowerHolderComponent.getPowers(player, LeveledManaPower.class).forEach(power -> power.SetManaLevel(data.getInt("mana_level")));
                    }
                }
        ));
        ActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("leveled_mana_action"),
                new SerializableData()
                        .add("level_0", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_1", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_2", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_3", ApoliDataTypes.ENTITY_ACTION, null)
                        .add("level_4", ApoliDataTypes.ENTITY_ACTION, null),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity player) {
                        int nowManaLevel = Init_CCA.AddonData.get(player).getManaLevel();
                        String nowManaLevelString = "level_" + nowManaLevel;
                        data.ifPresent(nowManaLevelString, action -> {
                            if (action != null) {
                                ((ActionFactory<Entity>.Instance) action).accept(player);
                            }
                        });
                    }
                }
        ));
        BIActionRegister.accept(new ActionFactory<>(
                SSCXuAddon.identifier("bi_leveled_mana_action"),
                new SerializableData()
                        .add("level_0", ApoliDataTypes.BIENTITY_ACTION, null)
                        .add("level_1", ApoliDataTypes.BIENTITY_ACTION, null)
                        .add("level_2", ApoliDataTypes.BIENTITY_ACTION, null)
                        .add("level_3", ApoliDataTypes.BIENTITY_ACTION, null)
                        .add("level_4", ApoliDataTypes.BIENTITY_ACTION, null),
                (data, entityPair) -> {
                    if (entityPair.getRight() instanceof PlayerEntity player) {
                        int nowManaLevel = Init_CCA.AddonData.get(player).getManaLevel();
                        String nowManaLevelString = "level_" + nowManaLevel;
                        data.ifPresent(nowManaLevelString, action -> {
                            if (action != null) {
                                ((ActionFactory<Pair<Entity, Entity>>.Instance) action).accept(entityPair);
                            }
                        });
                    }
                }
        ));

    }

    public static void registerConditions(Consumer<ConditionFactory<Entity>> ConditionRegister) {
        ConditionRegister.accept(new ConditionFactory<>(
                SSCXuAddon.identifier("is_mana_level"),
                new SerializableData()
                        .add("mana_level", SerializableDataTypes.INT, 1),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity player) {
                        return Init_CCA.AddonData.get(player).getManaLevel() == data.getInt("mana_level");
                    }
                    return false;
                }
        ));
        ConditionRegister.accept(new ConditionFactory<>(
                SSCXuAddon.identifier("leveled_mana_condition"),
                new SerializableData()
                        .add("level_0", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("level_1", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("level_2", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("level_3", ApoliDataTypes.ENTITY_CONDITION, null)
                        .add("level_4", ApoliDataTypes.ENTITY_CONDITION, null),
                (data, entity) -> {
                    if (entity instanceof PlayerEntity player) {
                        int nowManaLevel = Init_CCA.AddonData.get(player).getManaLevel();
                        String nowManaLevelString = "level_" + nowManaLevel;
                        if (data.isPresent(nowManaLevelString) && data.get(nowManaLevelString) != null) {
                            return ((ConditionFactory<Entity>.Instance)data.get(nowManaLevelString)).test(player);
                        }
                    }
                    return false;
                })
        );
    }
}
