package dev.zelo.renderrescontrol.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zelo.renderrescontrol.Constants;
import dev.zelo.renderrescontrol.Renderrescontrol;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(at = @At("HEAD"), method = "renderLevel")
    private void onRenderWorldBegin(CallbackInfo callbackInfo) {
        if(!Renderrescontrol.getInstance().hasRun) {
            Renderrescontrol.getInstance().hasRun = true;
            // TODO: Really hacky way to wait for the game to start rendering. This HAS TO be improved.
            new Thread("Renderrescontrol") {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Constants.LOG.error("Thread interrupted", e);
                    }
                    Renderrescontrol.getInstance().onResolutionChanged();
                }
            }.start();
        }

        Renderrescontrol.getInstance().setShouldScale(true);
    }

    @Inject(at = @At("RETURN"), method = "renderLevel")
    private void onRenderWorldEnd(CallbackInfo callbackInfo) {
        Renderrescontrol.getInstance().setShouldScale(false);
    }

//    @Inject(method = "resize", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;resize(II)V"))
//    private void onResize(int width, int height, CallbackInfo ci) {
//        this.minecraft.levelRenderer.resize(width / 2, height / 2);
//    }

//    @ModifyArgs(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;viewport(IIII)V"))
//    private void a(Args args) {
//        args.set(2, 200);
//        args.set(3, 200);
//    }

//    @Inject(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(Lnet/minecraft/client/DeltaTracker;)V"))
//    private void c(CallbackInfo ci) {
//        RenderSystem.viewport(0, 0, minecraft.getWindow().getWidth() / 2, minecraft.getWindow().getHeight() / 2);
////        RenderSystem.viewport(0, 0, 10, 10);
//    }

//    @Redirect(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;viewport(IIII)V"))
//    private void e(int p_69950_, int p_69951_, int p_69952_, int p_69953_) {
////         nothing lmao
//    }
//
//    @Redirect(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Overlay;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
//    private void e(Overlay instance, GuiGraphics guiGraphics, int i, int d, float v) {
////         nothing lmao
//    }

//    @Inject(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"))
//    @Inject(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;tryTakeScreenshotIfNeeded()V"))
//    @Inject(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
//    @Inject(method = "render", remap = false, at = @At(value = "HEAD"))
//    private void b(CallbackInfo ci) {
////        RenderSystem.viewport(0, 0, minecraft.getWindow().getWidth() / 2, minecraft.getWindow().getHeight() / 2);
//        RenderSystem.viewport(0, 0, 200, 200);
//    }

//    @Inject(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V", shift = At.Shift.AFTER))
//    private void d(CallbackInfo ci) {
////        RenderSystem.viewport(0, 0, minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
////        RenderSystem.viewport(0, 0, 10, 10);
//    }

//    @ModifyArg(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;viewport(IIII)V"), index = 2)
//    private int modifyViewportX(int x) {
//        return (int) (x / Renderrescontrol.getConfig().scale);
////        return 5;
//    }
//
//    @ModifyArg(method = "render", remap = false, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;viewport(IIII)V"), index = 3)
//    private int modifyViewportY(int x) {
//        return (int) (x / Renderrescontrol.getConfig().scale);
////        return 5;
//    }
}
