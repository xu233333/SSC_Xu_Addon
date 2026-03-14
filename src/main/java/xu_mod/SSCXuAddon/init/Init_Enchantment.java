package xu_mod.SSCXuAddon.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.data.enchantment.ShapeKiller;
import xu_mod.SSCXuAddon.data.enchantment.ShapeProtection;


public class Init_Enchantment {
    public static Enchantment SHAPE_KILLER = new ShapeKiller();
    public static Enchantment SHAPE_PROTECTOR = new ShapeProtection();

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, SSCXuAddon.identifier("shape_killer"), SHAPE_KILLER);
        Registry.register(Registries.ENCHANTMENT, SSCXuAddon.identifier("shape_protection"), SHAPE_PROTECTOR);
    }
}
