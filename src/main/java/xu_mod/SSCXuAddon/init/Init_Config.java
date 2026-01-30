package xu_mod.SSCXuAddon.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import xu_mod.SSCXuAddon.config.PlayerCustomConfig;

public class Init_Config {

    public static PlayerCustomConfig playerCustomConfig;

    public static void init() {
        AutoConfig.register(PlayerCustomConfig.class, Toml4jConfigSerializer::new);  // 客户端配置
        playerCustomConfig = AutoConfig.getConfigHolder(PlayerCustomConfig.class).getConfig();
    }
}
