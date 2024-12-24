package dev.zelo.renderrescontrol.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

@Config(name = "renderrescontrol")
public class RenderrescontrolConfig implements ConfigData {
    public float scale = 1.0f;
    public boolean nearest = true;

    public static ConfigHolder<RenderrescontrolConfig> init() {
        // Register config
        ConfigHolder<RenderrescontrolConfig> holder = AutoConfig.register(RenderrescontrolConfig.class, JanksonConfigSerializer::new);

        // Listen for when the server is reloading (i.e. /reload), and reload the config
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((s, m) ->
                AutoConfig.getConfigHolder(RenderrescontrolConfig.class).load());

        return holder;
    }
}