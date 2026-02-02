package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import xu_mod.SSCXuAddon.init.Init_Form;

import java.util.function.Predicate;

public enum ExtraReputationTypes {
    CAT_VF((player -> Init_Form.FeralCatVF.equals(RegPlayerFormComponent.PLAYER_FORM.get(player).getCurrentForm())));

    private final Predicate<PlayerEntity> gossipEnablePredicate;

    public boolean isGossipEnable(PlayerEntity player) {
        return gossipEnablePredicate.test(player);
    }

    private ExtraReputationTypes(Predicate<PlayerEntity> gossipEnablePredicate) {
        this.gossipEnablePredicate = gossipEnablePredicate;
    }
}
