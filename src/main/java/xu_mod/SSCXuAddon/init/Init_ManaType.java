package xu_mod.SSCXuAddon.init;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.cursed_moon.CursedMoon;
import net.onixary.shapeShifterCurseFabric.mana.ManaHandler;
import net.onixary.shapeShifterCurseFabric.mana.ManaRegistries;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.Powers.LeveledManaPower;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_ManaType {
    public static Identifier MC_IsNight = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_night"),
            (player) -> CursedMoon.isNight(player.getWorld())
    );

    public static Identifier MC_IsCursedMoonDay = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_cursed_moon_day"),
            (player) -> CursedMoon.isCursedMoon(player.getWorld())
    );

    // 正常 500/5
    // 夜晚 500/4
    // 诅咒之月白天 500/3
    // 诅咒之月夜晚 500/2
    public static Identifier FamiliarFoxPurifyMana = ManaRegistries.registerManaType(
            SSCXuAddon.identifier("familiar_fox_purify_mana"),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(500d, 1.0d, 0d)
                            )
                    )
            ),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(0.25d, 1.0d, 0d)  // 5 per sec
                            )
                    ),
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("night_down"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    MC_IsNight,
                                    new ManaUtils.Modifier(-0.05d, 1.0d, 0d)  // -1 per sec
                            )
                    ),
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("curse_moon_down"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    MC_IsCursedMoonDay,
                                    new ManaUtils.Modifier(-0.10d, 1.0d, 0d)  // -2 per sec
                            )
                    )

            ),
            new ManaHandler().setOnServerManaEmpty(((manaComponent, player) -> {
                PowerHolderComponent.getPowers(player, LeveledManaPower.class).forEach(power -> power.SetManaLevel(0));
            }))
    );

    public static void init() {
    }
}
