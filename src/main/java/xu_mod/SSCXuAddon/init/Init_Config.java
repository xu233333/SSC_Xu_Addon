package xu_mod.SSCXuAddon.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import xu_mod.SSCXuAddon.config.PlayerCustomConfig;
import xu_mod.SSCXuAddon.config.ServerConfig;

public class Init_Config {

    public static ServerConfig serverConfig;
    public static PlayerCustomConfig playerCustomConfig;

    public static void init() {
        AutoConfig.register(ServerConfig.class, Toml4jConfigSerializer::new);  // 玩家自定义配置
        serverConfig = AutoConfig.getConfigHolder(ServerConfig.class).getConfig();
        AutoConfig.register(PlayerCustomConfig.class, Toml4jConfigSerializer::new);  // 玩家自定义配置
        playerCustomConfig = AutoConfig.getConfigHolder(PlayerCustomConfig.class).getConfig();
    }
}
