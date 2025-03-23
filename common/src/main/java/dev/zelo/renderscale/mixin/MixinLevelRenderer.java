package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityOutlineTarget;

    // The NEW and IMPROVED fix for the entity outline shader!
    // TODO: Un-hardcode this for release
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getMainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;"))
    private RenderTarget redirectGetMainRenderTarget(Minecraft instance) {
        entityOutlineTarget.width = instance.getWindow().getWidth() / 2;
        entityOutlineTarget.height = instance.getWindow().getHeight() / 2;
        return entityOutlineTarget;
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityOutlineTarget == null) return;
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }
}
