package dev.zelo.renderscale.mixin;

//import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
//import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.zelo.renderscale.CommonClass;
import dev.zelo.renderscale.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
//import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow private RenderTarget entityTarget;
//    @Shadow @Final private LevelTargetBundle targets;

//    @Unique
//    private FrameGraphBuilder multiLoader_Template$frameGraphBuilder;

//    @Unique
//    private Vector4f multiLoader_Template$Vector4f;

    // Fix for the entity outline shader
    // method is fabric
    // lambda is neoforge
//    @Inject(method = {"method_62215", "lambda$addSkyPass$12"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
//    private void onLoadEntityOutlineShader(CallbackInfo ci) {
//        FramePass framePass = multiLoader_Template$frameGraphBuilder.addPass("clear");
//        framePass.executes(() -> {
//            RenderSystem.clearColor(multiLoader_Template$Vector4f.x, multiLoader_Template$Vector4f.y, multiLoader_Template$Vector4f.z, 0.0F);
//            RenderSystem.clear(16640);
//        });
//        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
//    }
//
//    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"))
//    private FrameGraphBuilder a(FrameGraphBuilder instance) {
//        multiLoader_Template$frameGraphBuilder = instance;
//        return instance;
//    }

//    @Inject(method = {"method_62215", "lambda$addSkyPass$12"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;endBatch()V"))
//    @Inject(method = "renderSky", at = @At(value = "INVOKE"))
//    private void onLoadEntityOutlineShader(CallbackInfo ci) {
//        FramePass framePass = multiLoader_Template$frameGraphBuilder.addPass("clear");
//        framePass.executes(() -> {
//            RenderSystem.clearColor(multiLoader_Template$Vector4f.x, multiLoader_Template$Vector4f.y, multiLoader_Template$Vector4f.z, 0.0F);
//            RenderSystem.clear(16640);
//        });
//        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
//    }

    @Shadow public abstract void clear();

    @Shadow @Final private Minecraft minecraft;

    //    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"))
//    private FrameGraphBuilder a(FrameGraphBuilder instance) {
//        multiLoader_Template$frameGraphBuilder = instance;
//        return instance;
//    }
//    @Inject(method = "initOutline", at = @At("RETURN"))
//    private void a(CallbackInfo ci) {
//        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
//    }

    // 10 -- entities
    // 11 -- block entities
//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 22))
    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 16))
//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;"))
    private void a(CallbackInfo ci) {
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }


//    @ModifyArgs(method = "doEntityOutline", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(IIZ)V"))
//    private void a(Args args) {
//        int width = args.get(0);
//        int height = args.get(1);
//        float s = (float) Math.sqrt(CommonClass.getConfig().scale);
////        Constants.LOG.info(String.valueOf(width * s));
////        Constants.LOG.info(String.valueOf(s));
//        args.set(0, (int) (width * s));
//        args.set(1, (int) (height * s));
//    }

//    @ModifyArgs(method = "initOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;resize(II)V"))
//    private void a(Args args) {
////        int width = inverseScale(args.get(0));
//        int width = args.get(0);
////        int height = inverseScale(args.get(1));
//        int height = args.get(1);
//        float s = (float) CommonClass.getConfig().scale;
//        Constants.LOG.info(String.valueOf(width / s));
//        Constants.LOG.info(String.valueOf(s));
//        args.set(0, (int) (width / s));
//        args.set(1, (int) (height / s));
//    }

//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;getRenderDistance()F"))
//    private void a(CallbackInfo ci) {
//        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
//    }

//    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V"))
//    private void b(CallbackInfo ci) {
//        this.clear();
//    }
//
//    @ModifyVariable(method = "renderLevel", at = @At(value = "STORE"))
//    private Vector4f b(Vector4f value) {
//        multiLoader_Template$Vector4f = value;
//        return value;
//    }

//    @Redirect(method = "initOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;resize(II)V"))
//    private void onResize(PostChain instance, int rendertarget, int i) {
//        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
//    }

//    @ModifyArgs(method = "resize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;resize(II)V"))
//    private void y(Args args) {
//        int width = args.get(0);
//        int height = args.get(1);
////        float s = (float) CommonClass.getConfig().scale;
////        Constants.LOG.info(String.valueOf(width / s));
////        Constants.LOG.info(String.valueOf(s));
//        int sw = (int) (width * 2);
//        int sh = (int) (height * 2);
//        args.set(0, 1920);
//        args.set(1, 1080);
////        args.set(0, sw);
////        args.set(1, sh);
//    }

//    @ModifyArgs(method = "initOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;resize(II)V"))
//    private void x(Args args) {
//        int width = args.get(0);
//        int height = args.get(1);
////        float s = (float) CommonClass.getConfig().scale ;
////        Constants.LOG.info(String.valueOf(width / s));
////        Constants.LOG.info(String.valueOf(s));
//        args.set(0, (int) (width * 2));
//        args.set(1, (int) (height * 2));
//    }


    @Inject(method = "resize", at = @At("RETURN"))
    private void onOnResized(CallbackInfo ci) {
        if (entityTarget == null) return;
        CommonClass.getInstance().resizeMinecraftRenderTargetSize();
    }
}
