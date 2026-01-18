package xu_mod.SSCXuAddon.init;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import xu_mod.SSCXuAddon.Powers.LeveledManaModifyDamageDealtPower;
import xu_mod.SSCXuAddon.Powers.LeveledManaPower;
import xu_mod.SSCXuAddon.Powers.ManaUtilsApoliEX;

public class Init_Apoli {

    private static void init_Power() {
        Init_Apoli.registerPower(LeveledManaPower.createFactory());
        Init_Apoli.registerPower(LeveledManaModifyDamageDealtPower.createFactory());
    }

    private static void init_Condition() {
        LeveledManaPower.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ManaUtilsApoliEX.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
    }

    private static void init_Action() {
        LeveledManaPower.registerConditions(Init_Apoli::registerEntityCondition);
    }


    public static void init() {
        init_Power();
        init_Condition();
        init_Action();
    }

    public static void registerPower(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    public static void registerEntityAction(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    public static void registerBIEntityAction(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    private static void registerEntityCondition(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
