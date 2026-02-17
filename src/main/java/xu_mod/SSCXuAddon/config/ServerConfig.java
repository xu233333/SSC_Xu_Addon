package xu_mod.SSCXuAddon.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "ssc_xu_addon_server")
public class ServerConfig implements ConfigData {
    public ServerConfig() {}

    @ConfigEntry.Category("General")
    @Comment("Enable space bag item. Default: false")
    public boolean enableSpaceBag = true;
}
