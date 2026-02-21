package xu_mod.SSCXuAddon.init;

import net.onixary.shapeShifterCurseFabric.player_form.*;
import xu_mod.SSCXuAddon.data.form.*;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_Form {
    public static PlayerFormBase FamiliarFoxPurify = RegPlayerForms.registerPlayerForm(new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify")).setPhase(PlayerFormPhase.PHASE_3));
    public static PlayerFormGroup FamiliarFoxPurifyGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("familiar_fox_purify_form")).addForm(FamiliarFoxPurify, 3)));
    public static PlayerFormBase BatVampire = RegPlayerForms.registerPlayerForm(new BatVampire(SSCXuAddon.identifier("bat_vampire")).setPhase(PlayerFormPhase.PHASE_3).setHasSlowFall(true).setOverrideHandAnim(true));
    public static PlayerFormGroup BatVampireGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("bat_vampire_form")).addForm(BatVampire, 3)));
    public static PlayerFormBase FeralCatVF = RegPlayerForms.registerPlayerForm(new CatVF(SSCXuAddon.identifier("feral_cat_vf")).setPhase(PlayerFormPhase.PHASE_SP).setBodyType(PlayerFormBodyType.FERAL));
    public static PlayerFormGroup FeralCatVFGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("feral_cat_vf_form")).addForm(FeralCatVF, 5)));
    public static PlayerFormBase AllayEngineer = RegPlayerForms.registerPlayerForm(new AllayEngineer(SSCXuAddon.identifier("allay_engineer")).setPhase(PlayerFormPhase.PHASE_SP));
    public static PlayerFormGroup AllayEngineerGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("allay_engineer_form")).addForm(AllayEngineer, 5)));
    public static PlayerFormBase OcelotJungle = RegPlayerForms.registerPlayerForm(new OcelotJungle(SSCXuAddon.identifier("ocelot_jungle")).setPhase(PlayerFormPhase.PHASE_3));
    public static PlayerFormGroup OcelotJungleGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("ocelot_jungle_form")).addForm(OcelotJungle, 3)));

    public static void init() {}
}
