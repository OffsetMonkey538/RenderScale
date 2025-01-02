package dev.zelo.renderscale;


import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.zelo.renderscale.config.RenderScaleConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Mod(Constants.MOD_ID)
public class RenderScale {
    private static final Minecraft client = Minecraft.getInstance();
    @Nullable
    private RenderTarget renderTarget;

    @Nullable
    private RenderTarget clientRenderTarget;

    private Set<RenderTarget> minecraftRenderTargets;

    private static RenderScale instance;
    private boolean shouldScale = false;
    public boolean hasRun = false;

    public static final ConfigHolder<RenderScaleConfig> CONFIG = RenderScaleConfig.init();

    public RenderScale(IEventBus eventBus, ModContainer modContainer) {
        instance = this;

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> RenderScale.getConfigScreen(screen));
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        NeoForge.EVENT_BUS.addListener(this::onWorldRenderStart);
        NeoForge.EVENT_BUS.addListener(this::onClientTickEnd);
    }

    public static RenderScale getInstance() {
        return instance;
    }

    public static RenderScaleConfig getConfig() {
        return CONFIG.getConfig();
    }

    public static Screen getConfigScreen(Screen parent) {
        return AutoConfig.getConfigScreen(RenderScaleConfig.class, parent).get();
    }

    public void onWorldRenderStart(RenderLevelStageEvent event) {
        if (!hasRun) {
            resizeMinecraftRenderTargetSize();
            hasRun = true;
        }
    }

    public void onClientTickEnd(ClientTickEvent.Post event) {
        if (client.level == null && hasRun) {
            hasRun = false;
        }
    }

    public void initMinecraftRenderTargets() {
        if (minecraftRenderTargets != null) {
            minecraftRenderTargets.clear();
        } else {
            minecraftRenderTargets = new HashSet<>();
        }

        minecraftRenderTargets.add(client.levelRenderer.entityOutlineTarget());
        minecraftRenderTargets.add(client.levelRenderer.getTranslucentTarget());
        minecraftRenderTargets.add(client.levelRenderer.getItemEntityTarget());
        minecraftRenderTargets.add(client.levelRenderer.getParticlesTarget());
        minecraftRenderTargets.add(client.levelRenderer.getWeatherTarget());
        minecraftRenderTargets.add(client.levelRenderer.getCloudsTarget());
        minecraftRenderTargets.remove(null);
    }

    public void onResolutionChanged() {
        if (getWindow() == null) return;
        Constants.LOG.info("Size changed to {}x{} {}x{} {}x{}",
                getWindow().getWidth(), getWindow().getHeight(),
                getWindow().getScreenWidth(), getWindow().getScreenHeight(),
                getWindow().getGuiScaledWidth(), getWindow().getGuiScaledHeight());

        updateRenderTargetSize();

    }

    public void updateRenderTargetSize() {
        if (renderTarget == null) return;

        resize(renderTarget);
        resize(client.levelRenderer.entityOutlineTarget());
        resizeMinecraftRenderTargetSize();
    }

    public void resizeMinecraftRenderTargetSize() {
        initMinecraftRenderTargets();
        minecraftRenderTargets.forEach(this::resize);
    }

    public void setShouldScale(boolean shouldScale) {
        if (this.shouldScale == shouldScale) return;

        Window window = client.getWindow();
        if (renderTarget == null) {
            this.shouldScale = true;
            renderTarget = new MainTarget(window.getWidth(), window.getHeight());
        }

        this.shouldScale = shouldScale;

        if (shouldScale) {
            clientRenderTarget = client.getMainRenderTarget();

            setClientRenderTarget(renderTarget);
            renderTarget.bindWrite(true);
        } else {
            setClientRenderTarget(clientRenderTarget);
            client.getMainRenderTarget().bindWrite(true);

            renderTarget.blitAndBlendToScreen(window.getWidth(), window.getHeight());
        }
    }

    public double getCurrentScaleFactor() {
        return shouldScale ? getConfig().scale : 1;
    }

    private Window getWindow() {
        return client.getWindow();
    }

    private void setClientRenderTarget(RenderTarget renderTarget) {
        client.mainRenderTarget = renderTarget;
    }

    public void resize(@Nullable RenderTarget renderTarget) {
        if (renderTarget == null) return;

        boolean prev = shouldScale;
        shouldScale = true;

        Window window = client.getWindow();
        renderTarget.resize(window.getWidth(), window.getHeight());

        shouldScale = prev;
    }
}

