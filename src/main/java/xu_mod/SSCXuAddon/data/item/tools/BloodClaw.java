package xu_mod.SSCXuAddon.data.item.tools;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BloodClaw extends PickaxeItem {
    public static final String BloodAmountKey = "blood_amount";
    public static final float MaxBloodAmount = 100;

    public static float getBloodAmount(ItemStack stack) {
        if (stack.getOrCreateNbt().contains(BloodAmountKey)) {
            return stack.getOrCreateNbt().getFloat(BloodAmountKey);
        }
        return 0;
    }

    public static void setBloodAmount(ItemStack stack, float bloodAmount) {
        stack.getOrCreateNbt().putFloat(BloodAmountKey, bloodAmount);
    }

    public static float addBloodAmount(ItemStack stack, float bloodAmount) {
        setBloodAmount(stack, getBloodAmount(stack) + bloodAmount);
        return getBloodAmount(stack);
    }

    public BloodClaw(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.blood_claw.tooltip").formatted(Formatting.YELLOW));
        tooltip.add(Text.translatable("item.ssc_xu_addon.blood_claw.blood_amount", getBloodAmount(stack), MaxBloodAmount).formatted(Formatting.DARK_RED));
    }
}
