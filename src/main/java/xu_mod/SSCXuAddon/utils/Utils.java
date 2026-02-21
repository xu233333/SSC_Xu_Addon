package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.UUID;

public class Utils {
    public static final HashMap<UUID, Long> sprintingTime = new HashMap<>();

    public static void Tick(MinecraftServer server) {
        for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID uuid = player.getUuid();
            if (player.isSprinting()) {
                sprintingTime.put(uuid, sprintingTime.getOrDefault(uuid, 0L) + 1);
            } else {
                sprintingTime.put(uuid, 0L);
            }
        }
    }
}
