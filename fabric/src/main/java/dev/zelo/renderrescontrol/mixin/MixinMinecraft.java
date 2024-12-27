package dev.zelo.renderrescontrol.mixin;

import dev.zelo.renderrescontrol.Renderrescontrol;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(method = "<init>", at = @At(value = "NEW", target = "(II)Lcom/mojang/blaze3d/pipeline/MainTarget;"))
    private void onInitRenderTarget(GameConfig gameConfig, CallbackInfo ci) {
        Renderrescontrol mod = Renderrescontrol.getInstance();
    }
}
