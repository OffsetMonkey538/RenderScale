package dev.zelo.renderscale;

import dev.zelo.renderscale.config.RenderScaleConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class RenderScale implements ClientModInitializer {
    private static KeyMapping keyBinding;
    @Override
    public void onInitializeClient() {
        CommonClass.init();
        KeyMapping.Category category = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("renderscale", "category"));
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.renderscale.options", GLFW.GLFW_KEY_O, category));

        // https://github.com/FabricMC/fabric/pull/4875 bruh
//        WorldRenderEvents.START.register(worldRenderContext -> {
//            if (!CommonClass.getInstance().hasRun) {
//                CommonClass.getInstance().resizeRenderTarget();
//                CommonClass.getInstance().hasRun = true;
//            }
//        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (minecraft.level == null && CommonClass.getInstance().hasRun) {
                CommonClass.getInstance().hasRun = false;
            }

            while (keyBinding.consumeClick()) {
                minecraft.setScreen(AutoConfig.getConfigScreen(RenderScaleConfig.class, minecraft.screen).get());
            }
        });
    }
}
