package dev.zelo.renderrescontrol.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;

@Mixin(RenderTarget.class)
public abstract class MixinRenderTarget {
//    @Redirect(method = "*", at = @At(value = "INVOKE", target = ""))
//    @Redirect(method = "createBuffers", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V"))
//    private void onTexImage(int p_84210_, int p_84211_, int p_84212_, int p_84213_, int p_84214_, int p_84215_, int p_84216_, int p_84217_, IntBuffer p_84218_) {
//        GlStateManager._texImage2D(p_84210_, p_84211_, p_84212_, p_84213_, p_84214_, p_84215_, p_84216_, p_84217_, p_84218_);
//    }
}
