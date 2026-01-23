package xu_mod.SSCXuAddon.data.entity.projectiles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import xu_mod.SSCXuAddon.SSCXuAddon;

@Environment(EnvType.CLIENT)
public class BloodThornEntItyRenderer extends ProjectileEntityRenderer<BloodThornEntity> {
    public static final Identifier TEXTURE = SSCXuAddon.identifier("textures/entity/projectiles/blood_thorn.png");

    public BloodThornEntItyRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(BloodThornEntity arrowEntity) {
        return TEXTURE;
    }
}
