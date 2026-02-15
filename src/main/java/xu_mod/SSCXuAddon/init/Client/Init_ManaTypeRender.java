package xu_mod.SSCXuAddon.init.Client;

import net.onixary.shapeShifterCurseFabric.mana.ManaRegistriesClient;
import xu_mod.SSCXuAddon.data.manaType.AllayResourceRender;
import xu_mod.SSCXuAddon.data.manaType.BatBloodResourceRender;
import xu_mod.SSCXuAddon.data.manaType.FamiliarFoxPurifyManaRender;
import xu_mod.SSCXuAddon.init.Init_ManaType;

public class Init_ManaTypeRender {
    static {
        ManaRegistriesClient.registerManaTypeRender(Init_ManaType.FamiliarFoxPurifyMana, new FamiliarFoxPurifyManaRender());
        ManaRegistriesClient.registerManaTypeRender(Init_ManaType.BatBloodResource, new BatBloodResourceRender());
        ManaRegistriesClient.registerManaTypeRender(Init_ManaType.AllayResource, new AllayResourceRender());
    }

    public static void init() {}
}
