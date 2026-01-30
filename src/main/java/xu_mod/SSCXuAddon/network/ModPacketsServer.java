package xu_mod.SSCXuAddon.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import xu_mod.SSCXuAddon.config.PlayerCustomConfigData;

import java.util.UUID;

public class ModPacketsServer {
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                ModPackets.syncPlayerCustomConfig,
                ModPacketsServer::onSyncCustomPlayerConfig
        );
    }

    private static void onSyncCustomPlayerConfig(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        UUID PlayerUUID = playerEntity.getUuid();
        boolean enableFakeBlindPower = packetByteBuf.readBoolean();
        minecraftServer.execute(() -> {
            PlayerCustomConfigData.playerCustomConfigDataMap.computeIfAbsent(PlayerUUID, k -> new PlayerCustomConfigData()).enableFakeBlindPower = enableFakeBlindPower;
        });
    }

    public static void sendPlayerLogin(ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, ModPackets.playerLogin, PacketByteBufs.create());
    }
}
