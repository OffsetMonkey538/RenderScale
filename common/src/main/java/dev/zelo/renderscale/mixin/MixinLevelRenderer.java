package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityOutlineTarget;
    @Shadow @Final private LevelTargetBundle targets;

    @Inject(method = "method_62215", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
    private void onLoadEntityOutlineShader(CallbackInfo ci) {
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
        FramePass framePass = multiLoader_Template$frameGraphBuilder.addPass("clear");
        this.targets.main = framePass.readsAndWrites(this.targets.main);
        framePass.executes(() -> {
            RenderSystem.clearColor(multiLoader_Template$Vector4f.x, multiLoader_Template$Vector4f.y, multiLoader_Template$Vector4f.z, 0.0F);
            RenderSystem.clear(16640);
        });
    }

    @Unique
    private FrameGraphBuilder multiLoader_Template$frameGraphBuilder;

    @Unique
    private Vector4f multiLoader_Template$Vector4f;

    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"))
    private FrameGraphBuilder a(FrameGraphBuilder instance) {
        multiLoader_Template$frameGraphBuilder = instance;
        return instance;
    }

    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"))
    private Vector4f b(Vector4f value) {
        multiLoader_Template$Vector4f = value;
        return value;
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityOutlineTarget == null) return;
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }
}
