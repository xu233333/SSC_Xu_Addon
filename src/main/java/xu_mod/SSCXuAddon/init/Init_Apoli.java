package xu_mod.SSCXuAddon.init;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.powers.*;

public class Init_Apoli {
    public static final PowerType<?> IronGolemFriendlyV1 = new PowerTypeReference<>(SSCXuAddon.identifier("iron_golem_friendly_v1"));  // 攻击铁傀儡是否会还击
    public static final PowerType<?> IronGolemFriendlyV2 = new PowerTypeReference<>(SSCXuAddon.identifier("iron_golem_friendly_v2"));  // 被打后铁傀儡会不会支援

    private static void init_Power() {
        Init_Apoli.registerPower(LeveledManaPower.createFactory());
        Init_Apoli.registerPower(LeveledManaModifyDamageDealtPower.createFactory());
        Init_Apoli.registerPower(ManaToFoodPower.createFactory());
        Init_Apoli.registerPower(RecoveryOnKillPower.createFactory());
        Init_Apoli.registerPower(AutoHpToManaPower.createFactory());
        Init_Apoli.registerPower(FallFlyingBoostPower.createFactory());
        Init_Apoli.registerPower(FakeBlindPower.createFactory());
        Init_Apoli.registerPower(ManaTypePowerV2.createFactory());
        Init_Apoli.registerPower(AllayPower.createFactory());
    }

    private static void init_Condition() {
        LeveledManaPower.registerConditions(Init_Apoli::registerEntityCondition);
        SomeRandomConditionAndAction.registerConditions(Init_Apoli::registerEntityCondition);
    }

    private static void init_Action() {
        LeveledManaPower.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ManaUtilsApoliEX.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ShootFireBallAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        SomeRandomConditionAndAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        FireRingAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ShootBloodThornAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
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
