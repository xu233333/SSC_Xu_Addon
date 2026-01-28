package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import xu_mod.SSCXuAddon.command.SSC_Xu_Addon_Command;
import xu_mod.SSCXuAddon.data.item.tools.BloodClaw;
import xu_mod.SSCXuAddon.powers.FireRingAction;

public class Init_Event {
    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            FireRingAction.Tick();
        });
        AttackEntityCallback.EVENT.register(
                (player, world, hand, entity, hitResult) -> {
                    ItemStack mainHandStack = player.getMainHandStack();
                    if (Init_Item.BLOOD_CLAW.equals(mainHandStack.getItem())) {
                        float FinalBloodAmount = BloodClaw.addBloodAmount(mainHandStack, player.getRandom().nextFloat() * 7.5f);  // 0.0f -> 7.5f
                        if (FinalBloodAmount >= BloodClaw.MaxBloodAmount) {
                            player.giveItemStack(new ItemStack(Init_Item.BLOOD_GEM));
                            BloodClaw.setBloodAmount(mainHandStack, 0.0f);
                        }
                    }
                    return ActionResult.PASS;
                }
        );
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> SSC_Xu_Addon_Command.register(dispatcher)
        );
    }
}
