package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.init.Init_CCA;

public class InventoryMenuUtils {
    public static void openPlayerSpaceBag(PlayerEntity player, int SlotLevel) {
        Inventory SpaceBagInventory = Init_CCA.AddonData.get(player).getSpaceBag();
        SimpleNamedScreenHandlerFactory factory = new SimpleNamedScreenHandlerFactory((syncId, inventory, playerEntity) -> {
            ScreenHandler screenHandler = null;
            switch (SlotLevel) {
                case 1 -> {
                    screenHandler = new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, inventory, SpaceBagInventory, 1);
                }
                case 2 -> {
                    screenHandler = new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X2, syncId, inventory, SpaceBagInventory, 2);
                }
                case 3 -> {
                    screenHandler = new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X3, syncId, inventory, SpaceBagInventory, 3);
                }
                default -> {
                    SSCXuAddon.LOGGER.error("openPlayerSpaceBag: SlotLevel is not 1, 2 or 3");
                }
            }
            return screenHandler;
        }, Text.empty());
        player.openHandledScreen(factory);
    }
}
