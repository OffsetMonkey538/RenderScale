//package dev.zelo.renderrescontrol.mixin;
//
//import com.mojang.blaze3d.pipeline.MainTarget;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//
//@Mixin(MainTarget.class)
//public abstract class MixinMainTarget {
//    @Shadow protected abstract void createFrameBuffer(int p_166142_, int p_166143_);
//
//    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/MainTarget;createFrameBuffer(II)V"))
//    private void createFrameBufferRedirect(MainTarget mainTarget, int width, int height) {
//        // Call the original method with the width and height multiplied by 2
//        this.createFrameBuffer(200, 200);
//    }
//}
