package xu_mod.SSCXuAddon.init;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.command.SSC_Xu_Addon_Command;
import xu_mod.SSCXuAddon.data.cca.AddonDataComponent;
import xu_mod.SSCXuAddon.data.item.tools.BloodClaw;
import xu_mod.SSCXuAddon.network.ModPacketsServer;
import xu_mod.SSCXuAddon.powers.FireRingAction;

import java.util.LinkedList;
import java.util.List;

public class Init_Event {
    public static final List<Identifier> NeedResetCooldownWhenRespawn = new LinkedList<>();

    static {
        NeedResetCooldownWhenRespawn.add(SSCXuAddon.identifier("feral_cat_undying"));
    }

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
        ServerPlayConnectionEvents.JOIN.register(
                (handler, sender, server) -> {
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        ModPacketsServer.sendPlayerLogin(handler.player);
                    }).start();
                }
        );
        ServerPlayerEvents.AFTER_RESPAWN.register(
                (oldPlayer, newPlayer, alive) -> {
                    AddonDataComponent addonDataComponent = Init_CCA.AddonData.get(newPlayer);
                    addonDataComponent.triggerCooldown(SSCXuAddon.identifier("respawn"));
                    for (Identifier cooldown : NeedResetCooldownWhenRespawn) {
                        addonDataComponent.resetCooldown(cooldown);
                    }
                }
        );
    }
}
