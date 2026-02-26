package xu_mod.SSCXuAddon.data.form;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AbstractAnimStateController;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AnimStateControllerDP.RideAnimController;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AnimStateControllerDP.WithSneakAnimController;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AnimStateEnum;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AnimSystem;
import net.onixary.shapeShifterCurseFabric.player_animation.v3.AnimUtils;
import net.onixary.shapeShifterCurseFabric.player_form.forms.Form_FeralBase;
import net.onixary.shapeShifterCurseFabric.player_form.forms.Form_Ocelot3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OcelotJungle extends Form_Ocelot3 {
    // 豹猫(丛林刺客) | Ocelot (Jungle Assassin)

    public OcelotJungle(Identifier formID) {
        super(formID);
    }


    private static final AnimUtils.AnimationHolderData ANIM_RUN = new AnimUtils.AnimationHolderData(ShapeShifterCurseFabric.identifier("form_feral_common_run"), 3.3f);
    public static final AbstractAnimStateController SPRINT_CONTROLLER = new WithSneakAnimController(ANIM_RUN, Form_FeralBase.ANIM_WALK);

    public @Nullable AbstractAnimStateController getAnimStateController(PlayerEntity player, AnimSystem.AnimSystemData animSystemData, @NotNull Identifier animStateID) {
        @Nullable AnimStateEnum animStateEnum = AnimStateEnum.getStateEnum(animStateID);
        if (animStateEnum != null) {
            switch (animStateEnum) {
                case ANIM_STATE_SLEEP:
                    return Form_FeralBase.SLEEP_CONTROLLER;
                case ANIM_STATE_CLIMB:
                    return Form_FeralBase.CLIMB_CONTROLLER;
                case ANIM_STATE_FALL:
                    return Form_FeralBase.FALL_CONTROLLER;
                case ANIM_STATE_JUMP:
                    return Form_FeralBase.JUMP_CONTROLLER;
                case ANIM_STATE_RIDE:
                    return RIDE_CONTROLLER;
                case ANIM_STATE_SWIM:
                    return Form_FeralBase.SWIM_CONTROLLER;
                case ANIM_STATE_USE_ITEM:
                    return Form_FeralBase.USE_ITEM_CONTROLLER;
                case ANIM_STATE_WALK:
                    return Form_FeralBase.WALK_CONTROLLER;
                case ANIM_STATE_SPRINT:
                    return SPRINT_CONTROLLER;
                case ANIM_STATE_IDLE:
                    return Form_FeralBase.IDLE_CONTROLLER;
                case ANIM_STATE_MINING:
                    return Form_FeralBase.MINING_CONTROLLER;
                case ANIM_STATE_ATTACK:
                    return Form_FeralBase.ATTACK_CONTROLLER;
                case ANIM_STATE_FLYING:
                case ANIM_STATE_FALL_FLYING:
                    return Form_FeralBase.FALL_FLYING_CONTROLLER;
                default:
                    return Form_FeralBase.IDLE_CONTROLLER;
            }
        }
        return super.getAnimStateController(player, animSystemData, animStateID);
    }
}
