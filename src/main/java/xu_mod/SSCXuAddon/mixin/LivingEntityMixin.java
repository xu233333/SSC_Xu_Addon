package xu_mod.SSCXuAddon.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xu_mod.SSCXuAddon.Powers.LeveledManaModifyDamageDealtPower;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageTaken(float originalValue, DamageSource source, float amount) {
        float newValue = originalValue;
        LivingEntity thisAsLiving = (LivingEntity)(Object)this;
        if(source.getAttacker() != null) {
            if (source.getAttacker() instanceof PlayerEntity) {
                for (LeveledManaModifyDamageDealtPower power : PowerHolderComponent.getPowers(source.getAttacker(), LeveledManaModifyDamageDealtPower.class)) {
                    newValue = power.modifyDamageDealt(source, newValue, thisAsLiving);
                }
            }
        }
        return newValue;
    }
}
