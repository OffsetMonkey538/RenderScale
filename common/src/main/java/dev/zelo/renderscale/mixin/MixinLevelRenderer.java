package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityOutlineTarget;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;importExternal(Ljava/lang/String;Ljava/lang/Object;)Lcom/mojang/blaze3d/resource/ResourceHandle;"))
    private void onRenderWorldBegin(CallbackInfo callbackInfo) {
        if (this.entityOutlineTarget != null) {
            Minecraft instance = this.minecraft;

            double s = CommonClass.getConfig().scale;

            entityOutlineTarget.width = (int) (instance.getWindow().getWidth() * s);
            entityOutlineTarget.height = (int) (instance.getWindow().getHeight() * s);
        }
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityOutlineTarget == null) return;
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }
}
