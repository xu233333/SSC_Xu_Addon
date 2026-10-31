package xu_mod.SSCXuAddon.init;

import net.onixary.shapeShifterCurseFabric.player_form.*;
import net.onixary.shapeShifterCurseFabric.player_form.utils.FormUtils;
import xu_mod.SSCXuAddon.data.form.*;
import xu_mod.SSCXuAddon.SSCXuAddon;

import static net.onixary.shapeShifterCurseFabric.player_form.NormalForm.NORMAL_SCALE_FUNC_BUILDER;

public class Init_Form {
    public static FormUtils.FlagData NoFormStoreStone = new FormUtils.FlagData("no_form_store_stone");

    // 用AI前也不思考一下什么是标准API 这么改明显会炸数据包的 可能Beta版已经发布了 先临时用这个方法吧 我修完再用正确的方式

    // public static IForm FamiliarFoxPurify = RegPlayerForms.registerPlayerForm(new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.45f,0.6f)));
    // public static IFormGroup FamiliarFoxPurifyGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("familiar_fox_purify_form")).registerForm(4, 1, FamiliarFoxPurify)));
    // public static IForm BatVampire = RegPlayerForms.registerPlayerForm(new BatVampire(SSCXuAddon.identifier("bat_vampire")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.5f, 0.6f)));
    // public static IFormGroup BatVampireGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("bat_vampire_form")).registerForm(4, 1, BatVampire)));
    // public static IForm FeralCatVF = RegPlayerForms.registerPlayerForm(new CatVF(SSCXuAddon.identifier("feral_cat_vf")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.55f,0.6f)));
    // public static IFormGroup FeralCatVFGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("feral_cat_vf_form")).registerForm(4, 1, FeralCatVF)));
    // public static IForm AllayEngineer = RegPlayerForms.registerPlayerForm(new AllayEngineer(SSCXuAddon.identifier("allay_engineer")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.35f,1.0f)));
    // public static IFormGroup AllayEngineerGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("allay_engineer_form")).registerForm(4, 1, AllayEngineer)));
    // public static IForm OcelotJungle = RegPlayerForms.registerPlayerForm(new OcelotJungle(SSCXuAddon.identifier("ocelot_jungle")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.75f, 0.6f)));
    // public static IFormGroup OcelotJungleGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("ocelot_jungle_form")).registerForm(4, 1, OcelotJungle)));
    // public static IForm AxolotlSeaKing = RegPlayerForms.registerPlayerForm(new AxolotlSeaKing(SSCXuAddon.identifier("axolotl_sea_king")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f, 1.0f)));
    // public static IFormGroup AxolotlSeaKingGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("axolotl_sea_king_form")).registerForm(4, 1, AxolotlSeaKing)));
    // public static IForm SpiderUndead = RegPlayerForms.registerPlayerForm(new SpiderUndead(SSCXuAddon.identifier("spider_undead")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f,1.0f)));
    // public static IFormGroup SpiderUndeadGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("spider_undead_form")).registerForm(4, 1, SpiderUndead)));
    // public static IForm AnubisWolfSoulkeeper = RegPlayerForms.registerPlayerForm(new AnubisWolfSoulkeeper(SSCXuAddon.identifier("anubis_wolf_soulkeeper")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.NORMAL).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f,1.0f)).perkTree(Init_Perk.T_AnubisWolfPerkTree);
    // public static IFormGroup AnubisWolfSoulkeeperGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("anubis_wolf_soulkeeper_form")).registerForm(4, 1, AnubisWolfSoulkeeper)));

    public static IForm FamiliarFoxPurify = RegPlayerForms.registerPlayerForm(new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScale(0.45f,0.6f));
    public static IFormGroup FamiliarFoxPurifyGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("familiar_fox_purify_form")).registerForm(4, 1, FamiliarFoxPurify)));
    public static IForm BatVampire = RegPlayerForms.registerPlayerForm(new BatVampire(SSCXuAddon.identifier("bat_vampire")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.5f, 0.6f)));
    public static IFormGroup BatVampireGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("bat_vampire_form")).registerForm(4, 1, BatVampire)));
    public static IForm FeralCatVF = RegPlayerForms.registerPlayerForm(new CatVF(SSCXuAddon.identifier("feral_cat_vf")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScale(0.55f,0.6f));
    public static IFormGroup FeralCatVFGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("feral_cat_vf_form")).registerForm(4, 1, FeralCatVF)));
    public static IForm AllayEngineer = RegPlayerForms.registerPlayerForm(new AllayEngineer(SSCXuAddon.identifier("allay_engineer")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.35f,1.0f)));
    public static IFormGroup AllayEngineerGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("allay_engineer_form")).registerForm(4, 1, AllayEngineer)));
    public static IForm OcelotJungle = RegPlayerForms.registerPlayerForm(new OcelotJungle(SSCXuAddon.identifier("ocelot_jungle")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.FERAL).applyScale(0.75f, 0.6f));
    public static IFormGroup OcelotJungleGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("ocelot_jungle_form")).registerForm(4, 1, OcelotJungle)));
    public static IForm AxolotlSeaKing = RegPlayerForms.registerPlayerForm(new AxolotlSeaKing(SSCXuAddon.identifier("axolotl_sea_king")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f, 1.0f)));
    public static IFormGroup AxolotlSeaKingGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("axolotl_sea_king_form")).registerForm(4, 1, AxolotlSeaKing)));
    public static IForm SpiderUndead = RegPlayerForms.registerPlayerForm(new SpiderUndead(SSCXuAddon.identifier("spider_undead")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f,1.0f)));
    public static IFormGroup SpiderUndeadGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("spider_undead_form")).registerForm(4, 1, SpiderUndead)));
    public static IForm AnubisWolfSoulkeeper = RegPlayerForms.registerPlayerForm(new AnubisWolfSoulkeeper(SSCXuAddon.identifier("anubis_wolf_soulkeeper")).formFlag(FormUtils.FinalForm, FormUtils.InhibitorImmune, FormUtils.NoInstinct, FormUtils.NoCursedMoonEffect).bodyType(PlayerFormBodyType.NORMAL).applyScaleFunc(NORMAL_SCALE_FUNC_BUILDER.apply(0.9f,1.0f)).perkTree(Init_Perk.T_AnubisWolfPerkTree));
    public static IFormGroup AnubisWolfSoulkeeperGroup = RegPlayerForms.registerPlayerFormGroup((new NormalGroup(SSCXuAddon.identifier("anubis_wolf_soulkeeper_form")).registerForm(4, 1, AnubisWolfSoulkeeper)));

    static {
        ((NormalForm) RegPlayerForms.ORIGINAL_BEFORE_ENABLE).appendFlag(NoFormStoreStone);
    }

    public static void init() {}
}
