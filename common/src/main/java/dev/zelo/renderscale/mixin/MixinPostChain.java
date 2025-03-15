package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import dev.zelo.renderscale.CommonClass;
import net.minecraft.client.renderer.PostChain;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PostChain.class)
public class MixinPostChain {
    @Shadow private int screenWidth;
    @Shadow private Matrix4f shaderOrthoMatrix;

//    @Redirect(method = "resize", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/PostChain;screenWidth:I", opcode = 181))
//    private void onResize(PostChain instance, int value) {
//        screenWidth = (int) (value * CommonClass.getInstance().getCurrentScaleFactor());
//        screenWidth = (int) (value * 10);
//        screenWidth = 0;//(int) (width / CommonClass.getInstance().getCurrentScaleFactor());
//    }

//    @Redirect(method = "updateOrthoMatrix", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;setOrtho(FFFFFF)Lorg/joml/Matrix4f;"))
//    private Matrix4f onUpdateOrthoMatrix(Matrix4f instance, float left, float right, float bottom, float top, float near, float far) {
//        return instance.setOrtho(left, right / 2, bottom, top / 2, near, far);
//    }

//    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;viewHeight:I", opcode = 181))
//    private void onViewHeight(RenderTarget instance, int value) {
//        instance.viewHeight = (int) (value * 2);
//    }

//    @ModifyArgs(method = "resize", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;resize(IIZ)V"))
//    private void onResize(Args args) {
//        int width = args.get(0);
//        int height = args.get(1);
////        Constants.LOG.info(String.valueOf(CommonClass.getInstance().getCurrentScaleFactor()));
//
////        args.set(0, (int) (width * 2));
//        args.set(0, (int) (width * 2));
////        args.set(0, 0);
//
////            args.set(1, (int) (height * 2));
//        args.set(1, (int) (height * 2));
    @Shadow @Final private String name;

    ////        args.set(1, 0);
//    }

    private double inverseScale() {
        return name.equals("minecraft:shaders/post/entity_outline.json") ? 1 / CommonClass.getConfig().scale : 1;
    }

    @Redirect(method = "resize", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;resize(IIZ)V"))
    private void a(RenderTarget instance, int width, int height, boolean clearError) {
        instance.resize((int) (width / inverseScale()), (int) (height / inverseScale()), clearError);
    }

//    @Redirect(method = "resize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostPass;setOrthoMatrix(Lorg/joml/Matrix4f;)V"))
//    private void b(PostPass instance, Matrix4f shaderOrthoMatrix) {
//        instance.setOrthoMatrix(this.shaderOrthoMatrix.scale(1.1f, 1f,1));
//    }

    @Inject(method = "updateOrthoMatrix", at = @At("TAIL"))
    private void onUpdateOrthoMatrix(CallbackInfo ci) {
        this.shaderOrthoMatrix = this.shaderOrthoMatrix.scale((float) inverseScale(), (float) inverseScale(), 1.0F);
//        this.shaderOrthoMatrix.scale(2.0F, 2.0F, 1.0F);
    }

    // TODO: @ModifyArgs DOES NOT WORK!!

//    @@Redirec
}
