package xu_mod.SSCXuAddon.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "ssc_xu_addon_custom")
public class PlayerCustomConfig implements ConfigData {
    public PlayerCustomConfig() {}

    @ConfigEntry.Category("General")
    @Comment("Enable fake blind power. Default: false")
    public boolean enableFakeBlindPower = false;

}
