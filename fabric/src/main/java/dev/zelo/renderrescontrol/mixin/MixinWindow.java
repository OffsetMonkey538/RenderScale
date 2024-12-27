package dev.zelo.renderrescontrol.mixin;

import com.mojang.blaze3d.platform.Window;
import dev.zelo.renderrescontrol.Renderrescontrol;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Window.class)
public abstract class MixinWindow {
    @Inject(method = "getWidth", at = @At("RETURN"), cancellable = true)
    private void a(CallbackInfoReturnable<Integer> cir) {
        var value = scale(cir.getReturnValueI());
        cir.setReturnValue(value);
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    private void b(CallbackInfoReturnable<Integer> cir) {
        var value = scale(cir.getReturnValueI());
        cir.setReturnValue(value);
    }

    @Inject(method = "getGuiScale", at = @At("RETURN"), cancellable = true)
    private void c(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(cir.getReturnValueD() * (Renderrescontrol.getInstance().getCurrentScaleFactor()));
    }

    @Inject(method = "onFramebufferResize", at = @At("RETURN"))
    private void d(long window, int framebufferWidth, int framebufferHeight, CallbackInfo ci) {
        Renderrescontrol.getInstance().onResolutionChanged();
    }

    @Inject(method = "refreshFramebufferSize", at = @At("RETURN"))
    private void e(CallbackInfo ci) {
        Renderrescontrol.getInstance().onResolutionChanged();
    }

    private int scale(int value) {
        double scaleFactor = Renderrescontrol.getInstance().getCurrentScaleFactor();
        return Math.max(Mth.ceil(((double) value) * scaleFactor), 1);
    }
}
