package xu_mod.init;

import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormGroup;
import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormPhase;
import net.onixary.shapeShifterCurseFabric.player_form.RegPlayerForms;
import xu_mod.data.form.FamiliarFoxPurify;
import xu_mod.SSCXuAddon;

public class InitForm {
    public static PlayerFormBase FamiliarFoxPurify = RegPlayerForms.registerPlayerForm(new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify")).setPhase(PlayerFormPhase.PHASE_3));
    public static PlayerFormGroup FamiliarFoxPurifyGroup = RegPlayerForms.registerPlayerFormGroup((new PlayerFormGroup(SSCXuAddon.identifier("familiar_fox_purify_form")).addForm(FamiliarFoxPurify, 3)));

    public static void init() {}
}
