package xu_mod.SSCXuAddon.init;

import net.minecraft.util.Identifier;
import net.onixary.shapeShifterCurseFabric.ShapeShifterCurseFabric;
import net.onixary.shapeShifterCurseFabric.perk.*;
import net.onixary.shapeShifterCurseFabric.util.util.SpriteMap;
import net.onixary.shapeShifterCurseFabric.util.util.cost.BaseCost;
import net.onixary.shapeShifterCurseFabric.util.util.cost.RegCostType;
import xu_mod.SSCXuAddon.SSCXuAddon;
import xu_mod.SSCXuAddon.utils.PerkDepend.VerticalDependent;

public class Init_Perk {

    public static final SpriteMap PerkSpriteMap = new SpriteMap(SSCXuAddon.identifier("textures/perk/textures.png"), 512, 512, 0, 0, 16, 16);

    public static final Identifier P_AnubisWolfSoulTier1 = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_soul_tier_1"))
            .setIcon(PerkSpriteMap.bi(0, 1)).cost(new BaseCost(RegCostType.COST_XP, 500))
            .addPower(SSCXuAddon.identifier("perks/form_anubis_wolf_soulkeeper_soul_tier_1")).removePower());

    public static final Identifier P_AnubisWolfSoulTier2 = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_soul_tier_2"))
            .setIcon(PerkSpriteMap.bi(1, 1)).cost(new BaseCost(RegCostType.COST_XP, 750))
            .addPower(SSCXuAddon.identifier("perks/form_anubis_wolf_soulkeeper_soul_tier_2")).removePower());

    public static final Identifier P_AnubisWolfSoulTier3 = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_soul_tier_3"))
            .setIcon(PerkSpriteMap.bi(2, 1)).cost(new BaseCost(RegCostType.COST_XP, 1000))
            .addPower(SSCXuAddon.identifier("perks/form_anubis_wolf_soulkeeper_soul_tier_3")).removePower());

    public static final Identifier P_AnubisWolfSoulTier4 = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_soul_tier_4"))
            .setIcon(PerkSpriteMap.bi(3, 1)).cost(new BaseCost(RegCostType.COST_XP, 1250))
            .addPower(SSCXuAddon.identifier("perks/form_anubis_wolf_soulkeeper_soul_tier_4")).removePower());

    public static final Identifier P_AnubisWolfSoulEcho = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_echo"))
            .setIcon(PerkSpriteMap.bi(4, 1)).cost(new BaseCost(RegCostType.COST_XP, 500))
            .addPower().removePower());

    public static final Identifier P_AnubisWolfSoulWolf4 = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_wolf_4"))
            .setIcon(PerkSpriteMap.bi(5, 1)).cost(new BaseCost(RegCostType.COST_XP, 1000))
            .addPower().removePower());

    public static final Identifier P_AnubisWolfSoulGuard = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_guard"))
            .setIcon(PerkSpriteMap.bi(6, 1)).cost(new BaseCost(RegCostType.COST_XP, 750))
            .addPower(SSCXuAddon.identifier("perks/form_anubis_wolf_soulkeeper_guard")).removePower(SSCXuAddon.identifier("form_anubis_wolf_soulkeeper_lose_soul")));

    public static final Identifier P_AnubisWolfSoulMerge = RegPerks.registerPerkCommon(new NormalPerk(SSCXuAddon.identifier("anubis_wolf_soulkeeper_merge"))
            .setIcon(PerkSpriteMap.bi(7, 1)).cost(new BaseCost(RegCostType.COST_XP, 1250))
            .addPower(SSCXuAddon.identifier("form_anubis_wolf_soulkeeper_skill1_1")).removePower());

    // 我的拓展 按照我对Perk的理解来 RootPerk还是起飞吧
    // 又得改API了 没想到这一方面 (
    public static final PerkTree __T_AnubisWolfPerkTree = new PerkTree(ShapeShifterCurseFabric.identifier("anubis_wolf_soulkeeper_perk_tree"))
            .addNode(P_AnubisWolfSoulTier1, 1, 0)
            .addNode(P_AnubisWolfSoulTier2, 2, 0, new NormalDependent(P_AnubisWolfSoulTier1))
            .addNode(P_AnubisWolfSoulTier3, 3, 0, new NormalDependent(P_AnubisWolfSoulTier2))
            .addNode(P_AnubisWolfSoulTier4, 4, 0, new NormalDependent(P_AnubisWolfSoulTier3))
            .addNode(P_AnubisWolfSoulEcho, 1, 50, new VerticalDependent(P_AnubisWolfSoulTier1))
            .addNode(P_AnubisWolfSoulWolf4, 3, 50, new VerticalDependent(P_AnubisWolfSoulTier3), new NormalDependent(NormalDependent.LineConnectMode.MIDDLE, P_AnubisWolfSoulEcho))
            .addNode(P_AnubisWolfSoulGuard, 2, -50, new VerticalDependent(P_AnubisWolfSoulTier2))
            .addNode(P_AnubisWolfSoulMerge, 4, -50, new VerticalDependent(P_AnubisWolfSoulTier4), new NormalDependent(NormalDependent.LineConnectMode.MIDDLE, P_AnubisWolfSoulGuard));

    static {
        __T_AnubisWolfPerkTree.perkLowestTier_r = 1;
    }

    public static final Identifier T_AnubisWolfPerkTree = RegPerks.registerPerkTree(__T_AnubisWolfPerkTree);

    public static void init() {

    }
}
