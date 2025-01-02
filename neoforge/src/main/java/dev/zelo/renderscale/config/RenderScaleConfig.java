package dev.zelo.renderscale.config;

import dev.zelo.renderscale.RenderScale;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

@Config(name = "renderscale")
public class RenderScaleConfig implements ConfigData {
    public float scale = 1.0f;
    public boolean nearest = true;

    public static ConfigHolder<RenderScaleConfig> init() {
        // Register config
        ConfigHolder<RenderScaleConfig> holder = AutoConfig.register(RenderScaleConfig.class, JanksonConfigSerializer::new);

        // Change resolution upon save!
        holder.registerSaveListener((manager, data) -> {
            RenderScale.getInstance().onResolutionChanged();
            return null;
        });

        return holder;
    }

    // Listen for when the server is reloading (i.e. /reload), and reload the config
    @SubscribeEvent
    public void onDatapackReload(AddReloadListenerEvent event) {
        AutoConfig.getConfigHolder(RenderScaleConfig.class).load();
    }
}