package xu_mod.SSCXuAddon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import net.onixary.shapeShifterCurseFabric.mana.RegManaComponent;
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
                        .then(CommandManager.literal("clear_mana_attributes").requires(cs -> cs.hasPermissionLevel(2))
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .then(CommandManager.literal("mana_type")
                                                .executes(SSC_Xu_Addon_Command::clearManaAttributes_MT)
                                        )
                                        .then(CommandManager.literal("player")
                                                .executes(SSC_Xu_Addon_Command::clearManaAttributes_P)
                                        )
                                )
                        )
                        .then(CommandManager.literal("test").requires(cs -> cs.hasPermissionLevel(2))
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

    private static int clearManaAttributes_MT(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity target = commandContext.getSource().getPlayer();
        if (target == null) {
            return 0;
        }
        ManaUtils.getManaComponent(target).MaxManaModifier.clear();
        ManaUtils.getManaComponent(target).ManaRegenModifier.clear();
        RegManaComponent.MANA.sync(target);
        return 0;
    }

    private static int clearManaAttributes_P(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity target = commandContext.getSource().getPlayer();
        if (target == null) {
            return 0;
        }
        ManaUtils.getManaComponent(target).MaxManaModifierPlayerSide.clear();
        ManaUtils.getManaComponent(target).ManaRegenModifierPlayerSide.clear();
        RegManaComponent.MANA.sync(target);
        return 0;
    }

    private static int test(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        PlayerEntity owner = commandContext.getSource().getPlayer();
        if (owner != null) {
            BloodThornEntity bloodThornEntity = new BloodThornEntity(owner, 5.0f, new Vec3d(0d,0d,0d));
            bloodThornEntity.setDamage(10.0f, 0.1f, 8f);
            owner.getWorld().spawnEntity(bloodThornEntity);
        }
        return 0;
    }
}
