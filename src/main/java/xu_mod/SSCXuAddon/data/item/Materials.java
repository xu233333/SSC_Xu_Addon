package xu_mod.SSCXuAddon.data.item;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import xu_mod.SSCXuAddon.init.Init_Item;

public enum Materials implements ToolMaterial {
    BLOOD_CLAW(1407, 3.0f, 4.0f, 3, 15, Ingredient.ofItems(Init_Item.BLOOD_GEM));  // 伤害高(大于钻石爪) 挖掘慢 附魔能力高

    private final int durability;
    private final float miningSpeedMultiplier;
    private final float attackDamage;
    private final int miningLevel;
    private final int enchantability;
    private final Ingredient repairIngredient;

    Materials(int durability, float miningSpeedMultiplier, float attackDamage, int miningLevel, int enchantability, Ingredient repairIngredient) {
        this.durability = durability;
        this.miningSpeedMultiplier = miningSpeedMultiplier;
        this.attackDamage = attackDamage;
        this.miningLevel = miningLevel;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeedMultiplier;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getMiningLevel() {
        return this.miningLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }
}
