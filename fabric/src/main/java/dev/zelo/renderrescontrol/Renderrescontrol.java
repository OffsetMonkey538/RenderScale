package dev.zelo.renderrescontrol;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.zelo.renderrescontrol.config.RenderrescontrolConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Renderrescontrol implements ModInitializer {
    private static final Minecraft client = Minecraft.getInstance();
    @Nullable
    private RenderTarget renderTarget;

    @Nullable
    private RenderTarget clientRenderTarget;

    private Set<RenderTarget> minecraftRenderTargets;

    private static Renderrescontrol instance;
    private boolean shouldScale = false;
    public boolean hasRun = false;

    public static final ConfigHolder<RenderrescontrolConfig> CONFIG = RenderrescontrolConfig.init();

    @Override
    public void onInitialize() {
        instance = this;

        WorldRenderEvents.START.register(worldRenderContext -> {
            if (!hasRun) {
                resizeMinecraftRenderTargetSize();
                hasRun = true;
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (minecraft.level == null && hasRun) {
                hasRun = false;
            }
        });
    }

    public static Renderrescontrol getInstance() {
        return instance;
    }

    public static RenderrescontrolConfig getConfig() {
        return CONFIG.getConfig();
    }

    public void initMinecraftRenderTargets() {
        if (minecraftRenderTargets != null) {
            minecraftRenderTargets.clear();
        } else {
            minecraftRenderTargets = new HashSet<>();
        }

        minecraftRenderTargets.add(client.levelRenderer.entityTarget());
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
        resize(client.levelRenderer.entityTarget());
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

            renderTarget.blitToScreen(window.getWidth(), window.getHeight());
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
        renderTarget.resize(window.getWidth(), window.getHeight(), Minecraft.ON_OSX);

        shouldScale = prev;
    }
}
