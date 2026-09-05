package xu_mod.SSCXuAddon.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import net.onixary.shapeShifterCurseFabric.util.Accessory.AccessoryUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xu_mod.SSCXuAddon.data.item.UndeadEssence;
import xu_mod.SSCXuAddon.data.item.trinket.NineLiveCharm;
import xu_mod.SSCXuAddon.init.Init_Apoli;
import xu_mod.SSCXuAddon.init.Init_Item;
import xu_mod.SSCXuAddon.powers.AllayPower;
import xu_mod.SSCXuAddon.powers.LeveledManaModifyDamageDealtPower;
import xu_mod.SSCXuAddon.powers.MinionShieldPower;
import xu_mod.SSCXuAddon.powers.SpeedDamageBoostPower;
import xu_mod.SSCXuAddon.utils.ShieldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (MinionShieldPower power : PowerHolderComponent.getPowers(thisAsLiving, MinionShieldPower.class)) {
            newValue = power.modifyDamageTaken(source, newValue, source.getAttacker());
        }
        if (thisAsLiving instanceof PlayerEntity player) {
            newValue = ShieldUtils.calcShield(player, newValue);
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

    @Inject(method = "tryUseTotem", at = @At("RETURN"), cancellable = true, order = 750)
    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        if (!cir.getReturnValue()) {
            if ((Object)this instanceof PlayerEntity player) {
                Map<Pair<@Nullable String, String>, List<ItemStack>> accessoryMap = AccessoryUtils.getEntitySlots(player, "auto");
                if (accessoryMap == null) {
                    return;
                }
                for (List<ItemStack> itemStacks : accessoryMap.values()) {
                    for (ItemStack itemStack : itemStacks) {
                        if (itemStack.getItem() instanceof NineLiveCharm && NineLiveCharm.CanTrigger(player, itemStack)) {
                            NineLiveCharm.OnTrigger(player, itemStack);
                            cir.setReturnValue(true);
                            return;
                        }
                    }
                }
                // 不死精粹
                if (player.getStackInHand(Hand.OFF_HAND).getItem() instanceof UndeadEssence && !player.getItemCooldownManager().isCoolingDown(Init_Item.UNDEAD_ESSENCE)) {
                    UndeadEssence.useForTotem(player, player.getStackInHand(Hand.OFF_HAND));
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }

    // 互联对 LootTableLoadingCallback 支持不太行 这种还是用mixin比较好
    @Inject(method = "onDeath", at = @At(value = "HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        World world = entity.getWorld();

        if (world.isClient) return;
        Entity attacker = source.getAttacker();
        if (attacker instanceof ServerPlayerEntity) {
            if (entity instanceof CatEntity) {
                if (entity.getRandom().nextInt(100) < 1) {  // 1%
                    // 特殊掉落物 发个光
                    ItemEntity itemEntity = new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Init_Item.CHARM_OF_NINE_LIVE));
                    itemEntity.setGlowing(true);
                    world.spawnEntity(itemEntity);
                }
            }
        }
    }
}
