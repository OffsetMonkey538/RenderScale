package dev.zelo.renderrescontrol.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zelo.renderrescontrol.Renderrescontrol;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityTarget;

    @Inject(method = "initOutline", at = @At("RETURN"))
    private void onLoadEntityOutlineShader(CallbackInfo ci) {
        Renderrescontrol.getInstance().resizeMinecraftRenderTargetSize();
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityTarget == null) return;
        Renderrescontrol.getInstance().resizeMinecraftRenderTargetSize();
    }
}
