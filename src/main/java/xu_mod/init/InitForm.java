package xu_mod.init;

import net.onixary.shapeShifterCurseFabric.player_form.PlayerFormBase;
import xu_mod.data.form.FamiliarFoxPurify;
import xu_mod.SSCXuAddon;

public class InitForm {
    public static PlayerFormBase FamiliarFoxPurify = new FamiliarFoxPurify(SSCXuAddon.identifier("familiar_fox_purify"));

    public static void init() {}
}
