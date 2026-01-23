package xu_mod.SSCXuAddon.powers;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;

import java.util.function.Consumer;

// 等我有空就把这里的Actions合进主仓库 我觉得还是有点通用性
public class ManaUtilsApoliEX {

    public static void registerActions(Consumer<ActionFactory<Entity>> ActionRegister, Consumer<ActionFactory<Pair<Entity, Entity>>> BIActionRegister) {
        ActionRegister.accept(new ActionFactory<Entity>(
                SSCXuAddon.identifier("set_mana_percent"),
                new SerializableData()
                        .add("mana_percent", SerializableDataTypes.DOUBLE, 0.0d),
                (data, e) -> {
                    if (e instanceof ServerPlayerEntity playerEntity) {
                        double mana_percent = data.get("mana_percent");
                        double mana_max = ManaUtils.getPlayerMaxMana(playerEntity);
                        double mana = mana_max * mana_percent;
                        ManaUtils.setPlayerMana(playerEntity, mana);
                    }
                })
        );
        ActionRegister.accept(new ActionFactory<Entity>(
                SSCXuAddon.identifier("gain_mana_percent"),
                new SerializableData()
                        .add("mana_percent", SerializableDataTypes.DOUBLE, 0.0d),
                (data, e) -> {
                    if (e instanceof ServerPlayerEntity playerEntity) {
                        double mana_percent = data.get("mana_percent");
                        double mana_max = ManaUtils.getPlayerMaxMana(playerEntity);
                        double mana = mana_max * mana_percent;
                        ManaUtils.gainPlayerMana(playerEntity, mana);
                    }
                })
        );
        ActionRegister.accept(new ActionFactory<Entity>(
                SSCXuAddon.identifier("consume_mana_percent"),
                new SerializableData()
                        .add("mana_percent", SerializableDataTypes.DOUBLE, 0.0d),
                (data, e) -> {
                    if (e instanceof ServerPlayerEntity playerEntity) {
                        double mana_percent = data.get("mana_percent");
                        double mana_max = ManaUtils.getPlayerMaxMana(playerEntity);
                        double mana = mana_max * mana_percent;
                        ManaUtils.consumePlayerMana(playerEntity, mana);
                    }
                })
        );
    }
}
