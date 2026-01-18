package xu_mod.SSCXuAddon.Powers;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.cca.AddonDataComponent;
import xu_mod.SSCXuAddon.init.Init_CCA;

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
