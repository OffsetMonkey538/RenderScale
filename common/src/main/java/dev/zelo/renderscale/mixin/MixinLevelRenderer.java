package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityTarget;

//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 16))
//    @Inject(method = "doEntityOutline", at = @At(value = "RETURN"))
    @Inject(method = "initOutline", at = @At(value = "RETURN"))
    private void a(CallbackInfo ci) {
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityTarget == null) return;
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }
}
