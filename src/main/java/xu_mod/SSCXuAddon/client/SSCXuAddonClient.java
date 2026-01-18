package xu_mod.SSCXuAddon.client;

import net.fabricmc.api.ClientModInitializer;
import xu_mod.SSCXuAddon.init.Client.Init;

public class SSCXuAddonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Init.init();
    }
}