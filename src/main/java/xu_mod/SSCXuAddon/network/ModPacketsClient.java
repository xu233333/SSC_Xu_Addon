package xu_mod.SSCXuAddon.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import xu_mod.SSCXuAddon.init.Init_Config;

public class ModPacketsClient {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(ModPackets.playerLogin, ModPacketsClient::onPlayerLogin);
    }

    public static void sendUpdateCustomPlayerConfigPacket() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(Init_Config.playerCustomConfig.enableFakeBlindPower);
        ClientPlayNetworking.send(ModPackets.syncPlayerCustomConfig, buf);
    }

    private static void onPlayerLogin(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            ModPacketsClient.sendUpdateCustomPlayerConfigPacket();
        }).start();
    }
}
