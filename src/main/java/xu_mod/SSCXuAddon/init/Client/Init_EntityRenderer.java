package xu_mod.SSCXuAddon.init.Client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import xu_mod.SSCXuAddon.data.entity.projectiles.BloodThornEntItyRenderer;
import xu_mod.SSCXuAddon.init.Init_Entity;

@Environment(EnvType.CLIENT)
public class Init_EntityRenderer {
    static {
        EntityRendererRegistry.register(Init_Entity.BLOOD_THORN, BloodThornEntItyRenderer::new);
    }

    public static void init() {}
}
