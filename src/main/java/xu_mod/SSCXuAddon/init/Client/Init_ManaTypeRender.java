package xu_mod.SSCXuAddon.init.Client;

import net.onixary.shapeShifterCurseFabric.mana.ManaRegistriesClient;
import xu_mod.SSCXuAddon.data.manaType.FamiliarFoxPurifyManaRender;
import xu_mod.SSCXuAddon.init.Init_ManaType;

public class Init_ManaTypeRender {
    static {
        ManaRegistriesClient.registerManaTypeRender(Init_ManaType.FamiliarFoxPurifyMana, new FamiliarFoxPurifyManaRender());
    }

    public static void init() {}
}
