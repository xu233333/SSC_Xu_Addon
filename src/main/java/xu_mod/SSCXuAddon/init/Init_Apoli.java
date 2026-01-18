package xu_mod.SSCXuAddon.init;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import xu_mod.SSCXuAddon.Powers.LeveledManaPower;

public class Init_Apoli {

    private static void init_Power() {
        Init_Apoli.registerPower(LeveledManaPower.createFactory());
    }

    private static void init_Condition() {
        LeveledManaPower.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
    }

    private static void init_Action() {
        LeveledManaPower.registerConditions(Init_Apoli::registerEntityCondition);
    }


    public static void init() {
        init_Power();
        init_Condition();
        init_Action();
    }

    public static PowerFactory<?> registerPower(PowerFactory<?> powerFactory) {
        return Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    public static ActionFactory<Entity> registerEntityAction(ActionFactory<Entity> actionFactory) {
        return Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    public static ActionFactory<Pair<Entity, Entity>> registerBIEntityAction(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        return Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

    private static void registerEntityCondition(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
