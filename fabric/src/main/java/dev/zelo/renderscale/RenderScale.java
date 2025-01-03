package dev.zelo.renderscale;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class RenderScale implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonClass.init();

        WorldRenderEvents.START.register(worldRenderContext -> {
            if (!CommonClass.getInstance().hasRun) {
                CommonClass.getInstance().resizeMinecraftRenderTargetSize();
                CommonClass.getInstance().hasRun = true;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (minecraft.level == null && CommonClass.getInstance().hasRun) {
                CommonClass.getInstance().hasRun = false;
            }
        });
    }
}
