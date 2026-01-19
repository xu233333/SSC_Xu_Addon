package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import xu_mod.SSCXuAddon.Powers.FireRingAction;

public class Init_Event {
    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            FireRingAction.Tick();
        });
    }
}
