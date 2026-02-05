package xu_mod.SSCXuAddon.mixin.integration;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.onixary.shapeShifterCurseFabric.player_form_render.FurRenderFeature;
import net.onixary.shapeShifterCurseFabric.player_form_render.OriginFurModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FurRenderFeature.class, remap = false)
public abstract class GlowFixMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow
    protected abstract void ProcessModel(OriginFurModel m, PlayerEntityRenderer eR, T entity, float limbAngle, float limbDistance, float headYaw, float headPitch);

    static {
        System.out.println("SSC-XuAddon: GlowFixMixin loaded");
    }

    public GlowFixMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/onixary/shapeShifterCurseFabric/player_form_render/OriginalFurClient$OriginFur;render(Lnet/minecraft/client/util/math/MatrixStack;Lmod/azure/azurelib/core/animatable/GeoAnimatable;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/VertexConsumer;I)V", ordinal = 1), cancellable = true)
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci, @Local(name = "m") OriginFurModel model) {
        if (entity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
            PlayerEntityRenderer eR = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayerEntity);
            this.ProcessModel(model, eR, entity, limbAngle, limbDistance, headYaw, headPitch);
        }
    }
}
