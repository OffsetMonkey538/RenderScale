package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.zelo.renderscale.CommonClass;
import dev.zelo.renderscale.accessors.GICommandEncoderThing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    // TODO: Do we need this?
    private void onRenderWorldBegin(CallbackInfo callbackInfo) {
//        if (!CommonClass.getInstance().hasRun) {
//            CommonClass.getInstance().hasRun = true;
//            waitingForResolutionChange = true;
//        }
//
//        if (waitingForResolutionChange) {
//            waitFrameCounter++;
//            if (waitFrameCounter >= 5) {
//                waitingForResolutionChange = false;
//                CommonClass.getInstance().onResolutionChanged();
//            }
//        }

        CommonClass.getInstance().setShouldScale(true);
    }

//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"))
    @Inject(method = "renderLevel", at = @At(value = "RETURN"))
    private void onRenderWorldEnd(CallbackInfo callbackInfo) {
        CommonClass.getInstance().setShouldScale(false);
    }
}
