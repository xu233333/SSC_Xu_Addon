package xu_mod.SSCXuAddon.config;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCustomConfigData {
    public static HashMap<UUID, PlayerCustomConfigData> playerCustomConfigDataMap = new HashMap<>();

    public boolean enableFakeBlindPower = false;
}
