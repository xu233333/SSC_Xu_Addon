package xu_mod.SSCXuAddon.init;

import xu_mod.SSCXuAddon.command.SSC_Xu_Addon_Command;
import xu_mod.SSCXuAddon.network.ModPacketsServer;

public class Init {
    public static void init() {
        Init_Config.init();
        Init_Form.init();
        Init_Item.init();
        Init_Entity.init();
        Init_ManaType.init();
        Init_Apoli.init();
        Init_Event.init();
        ModPacketsServer.register();
    }
}
