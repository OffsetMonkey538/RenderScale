package dev.zelo.renderscale.mixin.compat.iris;

import dev.zelo.renderscale.CommonClass;
import net.irisshaders.iris.apiimpl.IrisApiV0ConfigImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(IrisApiV0ConfigImpl.class)
public abstract class MixinIrisApiV0ConfigImpl {
    @Inject(method = "setShadersEnabledAndApply", at = @At("TAIL"), remap = false)
    private void setShadersEnabledAndApply(CallbackInfo ci) {
        // This is for the irisScale override setting
        CommonClass.getInstance().resizeRenderTarget();
    }
}
