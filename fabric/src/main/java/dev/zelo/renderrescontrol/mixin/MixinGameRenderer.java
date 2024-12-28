package dev.zelo.renderrescontrol.mixin;

import dev.zelo.renderrescontrol.Constants;
import dev.zelo.renderrescontrol.Renderrescontrol;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Unique
    private boolean waitingForResolutionChange = false;

    @Unique
    private int waitFrameCounter = 0;

    @Inject(at = @At("HEAD"), method = "renderLevel")
    // TODO: Do we need this?
    private void onRenderWorldBegin(CallbackInfo callbackInfo) {
        if (!Renderrescontrol.getInstance().hasRun) {
            Renderrescontrol.getInstance().hasRun = true;
            waitingForResolutionChange = true;
        }

        if (waitingForResolutionChange) {
            waitFrameCounter++;
            if (waitFrameCounter >= 5) {
                waitingForResolutionChange = false;
                Renderrescontrol.getInstance().onResolutionChanged();
            }
        }

        Renderrescontrol.getInstance().setShouldScale(true);
    }

    @Inject(at = @At("RETURN"), method = "renderLevel")
    private void onRenderWorldEnd(CallbackInfo callbackInfo) {
        Renderrescontrol.getInstance().setShouldScale(false);
    }
}
