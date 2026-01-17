package xu_mod;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xu_mod.init.Init;

public class SSCXuAddon implements ModInitializer {
	public static final String MOD_ID = "ssc_xu_addon";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Init.init();
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}