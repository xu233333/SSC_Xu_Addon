package xu_mod.SSCXuAddon.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.mana.ManaRegistries;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_ManaType {
    public static Identifier FamiliarFoxPurifyMana = ManaRegistries.registerManaType(
            SSCXuAddon.identifier("familiar_fox_purify_mana"),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            ShapeShifterCurseFabric.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(500d, 1.0d, 0d)
                            )
                    )
            ),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            ShapeShifterCurseFabric.identifier("cursed_moon"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(0.1d, 1.0d, 0d)  // 2/sec
                            )
                    )
            ),
            ManaRegistries.EMPTY_MANA_HANDLER
    );

    public static void init() {
    }
}
