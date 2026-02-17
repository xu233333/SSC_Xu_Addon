package xu_mod.SSCXuAddon.utils.Inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class WhiteListedSlot extends Slot {
    private final Predicate<ItemStack> whitelist;

    public WhiteListedSlot(Inventory inventory, int index, int x, int y, Predicate<ItemStack> whitelist) {
        super(inventory, index, x, y);
        this.whitelist = whitelist;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return whitelist.test(stack) && super.canInsert(stack);
    }
}
