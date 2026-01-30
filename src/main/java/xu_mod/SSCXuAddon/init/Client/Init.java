package xu_mod.SSCXuAddon.init.Client;

import xu_mod.SSCXuAddon.network.ModPacketsClient;

public class Init {
    public static void init() {
        Init_EntityRenderer.init();
        Init_ManaTypeRender.init();
        ModPacketsClient.register();
    }
}
