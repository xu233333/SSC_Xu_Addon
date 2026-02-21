package xu_mod.SSCXuAddon.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xu_mod.SSCXuAddon.init.Init_Apoli;
import xu_mod.SSCXuAddon.powers.AllayPower;
import xu_mod.SSCXuAddon.powers.LeveledManaModifyDamageDealtPower;
import xu_mod.SSCXuAddon.powers.SpeedDamageBoostPower;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageTaken(float originalValue, DamageSource source, float amount) {
        float newValue = originalValue;
        LivingEntity thisAsLiving = (LivingEntity)(Object)this;
        if(source.getAttacker() != null) {
            if (source.getAttacker() instanceof PlayerEntity && !source.isIn(DamageTypeTags.IS_PROJECTILE)) {  // 由于有火球术 所以排除远程伤害
                for (LeveledManaModifyDamageDealtPower power : PowerHolderComponent.getPowers(source.getAttacker(), LeveledManaModifyDamageDealtPower.class)) {
                    newValue = power.modifyDamageDealt(source, newValue, thisAsLiving);
                }
                for (AllayPower power : PowerHolderComponent.getPowers(source.getAttacker(), AllayPower.class)) {
                    newValue = power.modifyDamageDealt(source, newValue, thisAsLiving);
                }
                for (SpeedDamageBoostPower power : PowerHolderComponent.getPowers(source.getAttacker(), SpeedDamageBoostPower.class)) {
                    newValue = power.modifyDamageDealt(source, newValue, thisAsLiving);
                }
            }
        }
        for (AllayPower power : PowerHolderComponent.getPowers(thisAsLiving, AllayPower.class)) {
            newValue = power.modifyDamageTaken(source, newValue, source.getAttacker());
        }
        return newValue;
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("RETURN"), cancellable = true)
    private void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof IronGolemEntity) {
            if (cir.getReturnValueZ() && Init_Apoli.IronGolemFriendlyV1.isActive(target)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity realThis = (LivingEntity)(Object)this;
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity attackerLE) {
            if (Init_Apoli.IronGolemFriendlyV2.isActive(realThis)) {
                realThis.getWorld().getOtherEntities(realThis, realThis.getBoundingBox().expand(16, 5, 16), (entity -> entity instanceof IronGolemEntity)).forEach(entity -> {
                    IronGolemEntity golem = (IronGolemEntity) entity;
                    golem.setAngryAt(attacker.getUuid());
                    golem.setAngerTime(600);  // 原版1~2秒 现在30秒
                    golem.setTarget(attackerLE);
                    return;
                });
            }
        }
    }
}
