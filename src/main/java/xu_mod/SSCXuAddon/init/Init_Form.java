package xu_mod.SSCXuAddon.init;

import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormGroup;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormPhase;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import xu_mod.SSCXuAddon.data.form.BatVampire;
import xu_mod.SSCXuAddon.data.form.FamiliarFoxPurify;
import xu_mod.SSCXuAddon.SSCXuAddon;

public class Init_Form {
    public static PlayerFormBase FamiliarFoxPurify = RegPlayerForms.registerPlayerForm(new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify")).setPhase(PlayerFormPhase.PHASE_3));
    public static PlayerFormGroup FamiliarFoxPurifyGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("familiar_fox_purify_form")).addForm(FamiliarFoxPurify, 3)));
    public static PlayerFormBase BatVampire = RegPlayerForms.registerPlayerForm(new BatVampire(SSCXuAddon.identifier("bat_vampire")).setPhase(PlayerFormPhase.PHASE_3).setHasSlowFall(true).setOverrideHandAnim(true));
    public static PlayerFormGroup BatVampireGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("bat_vampire_form")).addForm(BatVampire, 3)));

    public static void init() {}
}
