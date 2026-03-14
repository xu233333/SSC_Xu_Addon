package xu_mod.SSCXuAddon.data.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.ability.RegPlayerFormComponent;
import xu_mod.SSCXuAddon.utils.Utils;

public class ShapeProtection extends Enchantment {
    public ShapeProtection() {
        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 10 + (level - 1) * 8;
    }

    public int getMaxPower(int level) {
        return this.getMinPower(level) + 20;
    }

    public int getMaxLevel() {
        return 4;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return super.isAcceptableItem(stack);
    }

    public static float getEntityProtectModifier(Entity target) {
        if (Utils.IsTransformativeMob(target)) {
            return 2.0f;
        }
        if (target instanceof PlayerEntity player) {
            PlayerFormBase form = RegPlayerFormComponent.PLAYER_FORM.get(player).getCurrentForm();
            switch (form.getPhase()) {
                case PHASE_0 -> {
                    return 0.5f;
                }
                case PHASE_1 -> {
                    return 1.0f;
                }
                case PHASE_2 -> {
                    return 1.5f;
                }
                case PHASE_3 -> {
                    return 2.0f;
                }
                case PHASE_SP -> {
                    return 1.0f;
                }
                case PHASE_CLEAR -> {
                    return 0.0f;
                }
            }
        }
        return 0.0f;
    }


    public int getProtectionAmount(int level, DamageSource source) {
        Entity attacker = source.getAttacker();
        return Math.round(getEntityProtectModifier(attacker) * level);
    }
}
