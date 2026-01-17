package xu_mod.client;

import net.fabricmc.api.ClientModInitializer;
import xu_mod.init.Client.Init;

public class SSCXuAddonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Init.init();
    }
}