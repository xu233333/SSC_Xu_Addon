package xu_mod.SSCXuAddon.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.onixary.shapeShifterCurseFabric.form_giving_custom_entity.axolotl.TransformativeAxolotlEntity;
import net.onixary.shapeShifterCurseFabric.form_giving_custom_entity.bat.TransformativeBatEntity;
import net.onixary.shapeShifterCurseFabric.form_giving_custom_entity.ocelot.TransformativeOcelotEntity;
import net.onixary.shapeShifterCurseFabric.form_giving_custom_entity.wolf.TransformativeWolfEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Utils {
    // 纯服务端数据 如果客户端调用 只会获得一个空HashMap
    public static final HashMap<UUID, Long> sprintingTime = new HashMap<>();
    private static final HashMap<UUID, Vec3d> playerLastPos = new HashMap<>();
    public static final HashMap<UUID, Double> playerSpeed = new HashMap<>();
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
            if (playerLastPos.containsKey(uuid)) {
                playerSpeed.put(uuid, player.getPos().distanceTo(playerLastPos.get(uuid)));
            }
            playerLastPos.put(uuid, player.getPos());
        }
    }

    public static boolean IsTransformativeMob(Entity entity) {
        // 有空给主线的每个咒文生物加一个接口
        // 这几天感冒 等我状态好一些再写成可拓展的方法吧 比如List 这么写有点难看
        if (entity instanceof TransformativeAxolotlEntity) {
            return true;
        }
        if (entity instanceof TransformativeBatEntity) {
            return true;
        }
        if (entity instanceof TransformativeOcelotEntity) {
            return true;
        }
        if (entity instanceof TransformativeWolfEntity) {
            return true;
        }
        return false;
    }
}
