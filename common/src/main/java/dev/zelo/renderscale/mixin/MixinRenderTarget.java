package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.zelo.renderscale.CommonClass;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(RenderTarget.class)
public abstract class MixinRenderTarget {
//    @Shadow public abstract void _resize(int width, int height, boolean clearError);

    @Redirect(method = "setFilterMode(IZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texParameter(III)V"))
    private void onSetTexFilter(int target, int pname, int param) {
        GlStateManager._texParameter(target, pname, CommonClass.getConfig().getFilter() ? GL11.GL_LINEAR : GL11.GL_NEAREST);
    }

//    @ModifyArgs(method = "resize", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;recordRenderCall(Lcom/mojang/blaze3d/pipeline/RenderCall;)V"))
//    private void onResize(Args args) {
//        int width = args.get(0);
//        int height = args.get(1);
////        args.set(0, width * CommonClass.getConfig().scale);
////        args.set(1, height * CommonClass.getConfig().scale);
//        args.set(0, 5);
//        args.set(1, 5);
//    }

//    @Redirect(method = "resize", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;_resize(IIZ)V"))
//    private void onResize(RenderTarget instance, int width, int height, boolean clearError) {
//        this._resize((int) width * 2, (int) height * 2, clearError);
//    }
}
