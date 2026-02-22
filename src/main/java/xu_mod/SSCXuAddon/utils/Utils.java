package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.UUID;

public class Utils {
    // 纯服务端数据 如果客户端调用 只会获得一个空HashMap
    public static final HashMap<UUID, Long> sprintingTime = new HashMap<>();
    public static final HashMap<UUID, Integer> exhaustionTime = new HashMap<>();

    public static long MaxSprintingTime = 300;

    public static void Tick(MinecraftServer server) {
        for (PlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID uuid = player.getUuid();
            if (player.isSprinting()) {
                sprintingTime.put(uuid, Math.min(MaxSprintingTime, sprintingTime.getOrDefault(uuid, 0L) + 1));
            } else {
                sprintingTime.put(uuid, 0L);
            }
            int playerExhaustionTime = exhaustionTime.getOrDefault(uuid, 0);
            if (playerExhaustionTime > 0) {
                exhaustionTime.put(uuid, playerExhaustionTime - 1);
            } else if (playerExhaustionTime < 0) {
                exhaustionTime.put(uuid, 0);
            }
        }
    }
}
