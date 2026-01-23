package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import xu_mod.SSCXuAddon.command.SSC_Xu_Addon_Command;
import xu_mod.SSCXuAddon.powers.FireRingAction;

public class Init_Event {
    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            FireRingAction.Tick();
        });
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> SSC_Xu_Addon_Command.register(dispatcher)
        );
    }
}
