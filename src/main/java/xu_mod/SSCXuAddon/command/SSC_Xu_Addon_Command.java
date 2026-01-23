package xu_mod.SSCXuAddon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.data.entity.projectiles.BloodThornEntity;


public class SSC_Xu_Addon_Command {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("ssc_xu_addon").requires(cs -> cs.hasPermissionLevel(2))
                        .then(CommandManager.literal("set_mana")
                                .then(CommandManager.argument("mana", IntegerArgumentType.integer())
                                        .executes(SSC_Xu_Addon_Command::setMana)
                                )
                        )
                        .then(CommandManager.literal("test")
                                .executes(SSC_Xu_Addon_Command::test)
                        )
        );
    }

    private static int setMana(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity owner = commandContext.getSource().getPlayer();
        int mana = IntegerArgumentType.getInteger(commandContext, "mana");
        if (owner != null) {
            ManaUtils.setPlayerMana(owner, mana);
        }
        return 0;
    }

    private static int test(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity owner = commandContext.getSource().getPlayer();
        if (owner != null) {
            BloodThornEntity bloodThornEntity = new BloodThornEntity(owner, 1.0f, new Vec3d(0d,0d,0d));
            owner.getWorld().spawnEntity(bloodThornEntity);
        }
        return 0;
    }
}
