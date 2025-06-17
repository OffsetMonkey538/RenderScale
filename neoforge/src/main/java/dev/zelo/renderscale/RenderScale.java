package dev.zelo.renderscale;

import dev.zelo.renderscale.config.RenderScaleConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.lwjgl.glfw.GLFW;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class RenderScale {
    private static final Minecraft client = Minecraft.getInstance();

    // TODO: Consider using Lazy? (https://docs.neoforged.net/docs/misc/keymappings/#checking-a-keymapping)
    private static final KeyMapping keyBinding = new KeyMapping("key.renderscale.options", GLFW.GLFW_KEY_O, "key.renderscale.category");

    public RenderScale(IEventBus eventBus, ModContainer modContainer) {
        CommonClass.init();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> RenderScale.getConfigScreen(screen));

        eventBus.register(this);

        NeoForge.EVENT_BUS.addListener(this::onWorldRenderStart);
        NeoForge.EVENT_BUS.addListener(this::onClientTickEnd);
        NeoForge.EVENT_BUS.addListener(this::onDatapackReload);
    }

    public static Screen getConfigScreen(Screen parent) {
        return AutoConfig.getConfigScreen(RenderScaleConfig.class, parent).get();
    }

    public void onWorldRenderStart(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL && !CommonClass.getInstance().hasRun) {
            CommonClass.getInstance().resizeMinecraftRenderTargetSize();
            CommonClass.getInstance().hasRun = true;
        }
    }

    public void onClientTickEnd(ClientTickEvent.Post event) {
        if (client.level == null && CommonClass.getInstance().hasRun) {
            CommonClass.getInstance().hasRun = false;
        }

        while (keyBinding.consumeClick()) {
            client.setScreen(AutoConfig.getConfigScreen(RenderScaleConfig.class, client.screen).get());
        }
    }

    public void onDatapackReload(AddReloadListenerEvent event) {
        AutoConfig.getConfigHolder(RenderScaleConfig.class).load();
    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(keyBinding);
    }
}

