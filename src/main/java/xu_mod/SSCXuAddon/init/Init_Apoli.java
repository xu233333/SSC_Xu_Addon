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
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.powers.*;
import xu_mod.SSCXuAddon.powers.skills.WolfSoulAction;
import xu_mod.SSCXuAddon.powers.magic.MagicUtils;
import xu_mod.SSCXuAddon.powers.skills.OcelotRush1Power;
import xu_mod.SSCXuAddon.powers.skills.WolfSkillAction;

public class Init_Apoli {
    public static final PowerType<?> IronGolemFriendlyV1 = new PowerTypeReference<>(SSCXuAddon.identifier("iron_golem_friendly_v1"));  // 攻击铁傀儡是否会还击
    public static final PowerType<?> IronGolemFriendlyV2 = new PowerTypeReference<>(SSCXuAddon.identifier("iron_golem_friendly_v2"));  // 被打后铁傀儡会不会支援
    public static final PowerType<?> NoStopSprintWhileAttack = new PowerTypeReference<>(SSCXuAddon.identifier("no_stop_sprint_while_attack"));  // 攻击时不会停止冲刺
    public static final PowerType<?> WaterBreathing = new PowerTypeReference<>(SSCXuAddon.identifier("water_breathing"));  // 水下呼吸
    public static final PowerType<?> ZombieFriendly = new PowerTypeReference<>(SSCXuAddon.identifier("zombie_friendly"));  // 僵尸友善
    public static final PowerType<?> SkeletonFriendly = new PowerTypeReference<>(SSCXuAddon.identifier("skeleton_friendly"));  // 骷髅友善

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
        Init_Apoli.registerPower(SpeedDamageBoostPower.createFactory());
        Init_Apoli.registerPower(JumpClashPower.createFactory());
        Init_Apoli.registerPower(ManaAttributePro.createFactory());
        Init_Apoli.registerPower(AutoFeedPower.createFactory());
        Init_Apoli.registerPower(MinionShieldPower.createFactory());
        Init_Apoli.registerPower(SpiderMinionShieldPower.createFactory());
        Init_Apoli.registerPower(WolfSoulPower.createFactory());

        // Skill Power
        Init_Apoli.registerPower(OcelotRush1Power.createFactory());
        AxolotlPower.registerPower(Init_Apoli::registerPower);
    }

    private static void init_Condition() {
        LeveledManaPower.registerConditions(Init_Apoli::registerEntityCondition);
        SomeRandomConditionAndAction.registerConditions(Init_Apoli::registerEntityCondition);
        AxolotlPower.registerConditions(Init_Apoli::registerEntityCondition);
        MagicUtils.registerConditions(Init_Apoli::registerEntityCondition);
    }

    private static void init_Action() {
        LeveledManaPower.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ManaUtilsApoliEX.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ShootFireBallAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        SomeRandomConditionAndAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        FireRingAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        ShootBloodThornAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        AxolotlPower.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        WebBridgeAction.registerAction(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        MinionActions.registerAction(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        MagicUtils.registerAction(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        WolfSkillAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
        WolfSoulAction.registerActions(Init_Apoli::registerEntityAction, Init_Apoli::registerBIEntityAction);
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
