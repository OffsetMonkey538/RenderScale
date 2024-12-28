package dev.zelo.renderrescontrol.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.zelo.renderrescontrol.Renderrescontrol;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderTarget.class)
public abstract class MixinRenderTarget {
    @Redirect(method = "setFilterMode(IZ)V", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texParameter(III)V"))
    private void onSetTexFilter(int target, int pname, int param) {
        GlStateManager._texParameter(target, pname, Renderrescontrol.getConfig().nearest ? GL11.GL_NEAREST : GL11.GL_LINEAR);
    }
}
