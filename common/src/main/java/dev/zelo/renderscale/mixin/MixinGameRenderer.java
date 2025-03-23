package dev.zelo.renderscale.mixin;

import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void onRenderWorldBegin(CallbackInfo callbackInfo) {
        CommonClass.getInstance().setShouldScale(true);
    }

    @Inject(method = "renderLevel", at = @At(value = "RETURN"))
    private void onRenderWorldEnd(CallbackInfo callbackInfo) {
        CommonClass.getInstance().setShouldScale(false);
    }
}
