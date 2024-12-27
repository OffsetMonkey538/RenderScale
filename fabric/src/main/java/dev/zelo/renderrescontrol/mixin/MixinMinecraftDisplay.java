//package dev.zelo.renderrescontrol.mixin;
//
//import net.minecraft.client.Minecraft;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//
//@Mixin(Minecraft.class)
//public class MixinMinecraftDisplay {
//    @ModifyArg(method = "resizeDisplay", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;resize(IIZ)V"), index = 0)
//    private int b(int x) {
//        return (int) (x / 2);
//    }
//}
