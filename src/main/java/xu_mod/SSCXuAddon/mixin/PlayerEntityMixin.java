package xu_mod.SSCXuAddon.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xu_mod.SSCXuAddon.init.Init_Apoli;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private void ForceSprint(PlayerEntity instance, boolean b, Operation<Void> original) {
        if (Init_Apoli.NoStopSprintWhileAttack.isActive(instance)) {
            return;
        }
        original.call(instance, b);
    }
}
