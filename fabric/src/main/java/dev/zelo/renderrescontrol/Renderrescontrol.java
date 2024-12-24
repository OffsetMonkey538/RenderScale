package dev.zelo.renderrescontrol;

import dev.zelo.renderrescontrol.config.RenderrescontrolConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ClientModInitializer;

public class Renderrescontrol implements ClientModInitializer {
    public static final ConfigHolder<RenderrescontrolConfig> CONFIG = RenderrescontrolConfig.init();

    @Override
    public void onInitializeClient() {}

    public static RenderrescontrolConfig getConfig() {
        return CONFIG.getConfig();
    }
}
