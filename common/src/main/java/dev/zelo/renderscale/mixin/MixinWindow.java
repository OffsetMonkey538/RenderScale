package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.platform.Window;
import dev.zelo.renderscale.CommonClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Window.class)
public abstract class MixinWindow {
    @Inject(method = "getWidth", at = @At("RETURN"), cancellable = true)
    private void a(CallbackInfoReturnable<Integer> cir) {
        var value = renderScale$scale(cir.getReturnValueI());
        cir.setReturnValue(value);
    }

    @Inject(method = "getHeight", at = @At("RETURN"), cancellable = true)
    private void b(CallbackInfoReturnable<Integer> cir) {
        var value = renderScale$scale(cir.getReturnValueI());
        cir.setReturnValue(value);
    }

    @Inject(method = "getGuiScale", at = @At("RETURN"), cancellable = true)
    private void c(CallbackInfoReturnable<Integer> cir) {
        // It's NeoForges' fault for this null check
        if (CommonClass.getInstance() != null) {
            cir.setReturnValue((int) (cir.getReturnValueI() * (CommonClass.getInstance().getCurrentScaleFactor())));
        }
    }

    @Inject(method = "onFramebufferResize", at = @At("RETURN"))
    private void d(long window, int framebufferWidth, int framebufferHeight, CallbackInfo ci) {
        if (CommonClass.getInstance() != null) {
            CommonClass.getInstance().onResolutionChanged();
        }
    }

    @Inject(method = "refreshFramebufferSize", at = @At("RETURN"))
    private void e(CallbackInfo ci) {
        if (CommonClass.getInstance() != null) {
            CommonClass.getInstance().onResolutionChanged();
        }
    }

    @Unique
    private int renderScale$scale(int value) {
        if (CommonClass.getInstance() != null) {
            double scaleFactor = CommonClass.getInstance().getCurrentScaleFactor();
            return Math.max((int) (value * scaleFactor), 1);
        } else {
            return value;
        }
    }
}
