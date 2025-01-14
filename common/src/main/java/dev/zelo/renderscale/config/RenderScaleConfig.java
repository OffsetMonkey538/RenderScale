package dev.zelo.renderscale.config;

import dev.zelo.renderscale.CommonClass;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

@Config(name = "renderscale")
public class RenderScaleConfig implements ConfigData {
    public float scale = 1.0f;
    public boolean forceLinear = false;

    public static ConfigHolder<RenderScaleConfig> init() {
        // Register config
        ConfigHolder<RenderScaleConfig> holder = AutoConfig.register(RenderScaleConfig.class, JanksonConfigSerializer::new);

        // Change resolution upon save!
        holder.registerSaveListener((manager, data) -> {
            CommonClass.getInstance().onResolutionChanged();
            return null;
        });

        return holder;
    }

    // yes -> linear, no -> nearest
    public boolean getFilter() {
        return forceLinear || scale > 1.0;
    }
}