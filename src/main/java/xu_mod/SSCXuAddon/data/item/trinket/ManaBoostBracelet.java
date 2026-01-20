package xu_mod.SSCXuAddon.data.item.trinket;

import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class ManaBoostBracelet extends TrinketItem {
    public ManaBoostBracelet(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.ssc_xu_addon.mana_boost_bracelet.tooltip").formatted(Formatting.YELLOW));
    }
}
