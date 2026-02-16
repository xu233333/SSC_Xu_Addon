package xu_mod.SSCXuAddon.init;

import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.cursed_moon.CursedMoon;
import net.onixary.shapeShifterCurseFabric.mana.ManaHandler;
import net.onixary.shapeShifterCurseFabric.mana.ManaRegistries;
import net.onixary.shapeShifterCurseFabric.mana.ManaUtils;
import xu_mod.SSCXuAddon.powers.LeveledManaPower;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_ManaType {
    public static Identifier MC_IsNight = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_night"),
            (player) -> CursedMoon.isNight(player.getWorld())
    );

    public static Identifier MC_IsDay = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_day"),
            (player) -> !CursedMoon.isNight(player.getWorld())
    );

    public static Identifier MC_IsCursedMoonDay = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_cursed_moon_day"),
            (player) -> CursedMoon.isCursedMoon(player.getWorld())
    );

    public static Identifier MC_IsNotCursedMoonDay = ManaRegistries.registerManaConditionType(
            ShapeShifterCurseFabric.identifier("is_cursed_moon_day"),
            (player) -> !CursedMoon.isCursedMoon(player.getWorld())
    );

    public static Identifier MC_UnderSun = ManaRegistries.registerManaConditionType(
            SSCXuAddon.identifier("under_sun"),
            (player) -> {
                if (!player.getWorld().isDay()) {
                    return false;
                }
                if (player.getWorld().isRaining() || player.getWorld().isThundering()) {
                    return false;
                }
                BlockPos blockPos = player.getVehicle() instanceof BoatEntity ? (BlockPos.ofFloored(player.getX(), (double) Math.round(player.getY()), player.getZ())).up() : BlockPos.ofFloored(player.getX(), (double) Math.round(player.getY()), player.getZ());
                return player.getWorld().isSkyVisible(blockPos);
            }
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

    // 白天 600/-1
    // 夜晚 600/-0.5
    // 诅咒之月白天 600/-0.75
    // 诅咒之月夜晚 600/-0.25
    public static Identifier BatBloodResource = ManaRegistries.registerManaType(
            SSCXuAddon.identifier("bat_blood_resource"),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(600d, 1.0d, 0d)
                            )
                    )
            ),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(-0.0125d, 1.0d, 0d)  // -0.25 per sec
                            )
                    ),
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("day_up"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    MC_IsDay,
                                    new ManaUtils.Modifier(-0.025d, 1.0d, 0d)  // -0.5 per sec
                            )
                    ),
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("not_curse_moon_up"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    MC_IsNotCursedMoonDay,
                                    new ManaUtils.Modifier(-0.0125d, 1.0d, 0d)  // -0.25 per sec
                            )
                    ),
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("under_sun"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    MC_UnderSun,
                                    new ManaUtils.Modifier(-0.050d, 1.0d, 0d)  // -1.0 per sec
                            )
                    )
            ),
            new ManaHandler().setOnServerManaEmpty(((manaComponent, player) -> {
                // 最差的转换比例 自动转换比率 5% 1 -> 10 (50t) | 2% 2 -> 17.5 (50t) | 0% 10 -> 75 (1t)
                player.lastDamageTaken = 0.0f;  // 取消无敌帧
                player.damage(player.getWorld().getDamageSources().starve(), 10);
                manaComponent.gainMana(75);
            }))
    );

    public static Identifier AllayResource = ManaRegistries.registerManaType(
            SSCXuAddon.identifier("allay_resource"),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(240d, 1.0d, 0d)
                            )
                    )
            ),
            new ManaUtils.ModifierList(
                    new Pair<Identifier, Pair<Identifier, ManaUtils.Modifier>>(
                            SSCXuAddon.identifier("base_value"),
                            new Pair<Identifier, ManaUtils.Modifier>(
                                    ManaRegistries.MC_AlwaysTrue,
                                    new ManaUtils.Modifier(-0.1d, 1.0d, 0d)  // -2 per sec
                            )
                    )
            ),
            new ManaHandler().setOnServerManaTick(((manaComponent, player) -> {
                if (player.age % 10 != 0 || !player.isAlive()) {
                    return;
                }
                if (manaComponent.Mana > manaComponent.MaxManaClient - 2) {  //  不知道为什么 我的那套触发机制无法稳定触发 之后试试修一下
                    player.lastDamageTaken = 0.0f;  // 取消无敌帧 防止骗伤
                    player.damage(player.getWorld().getDamageSources().outOfWorld(), 5);
                    player.lastDamageTaken = 0.0f;  // 取消无敌帧 Again 防止用这个伤害骗伤
                    manaComponent.consumeMana(manaComponent.MaxManaClient * 0.1d);
                }
            }))
    );

    public static void init() {
    }
}
