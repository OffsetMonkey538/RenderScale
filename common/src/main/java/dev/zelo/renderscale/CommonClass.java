package dev.zelo.renderscale;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
import dev.zelo.renderscale.config.RenderScaleConfig;
import dev.zelo.renderscale.accessors.GICommandEncoderThing;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {
    private static final Minecraft client = Minecraft.getInstance();
    private GpuTexture x;

    private Set<RenderTarget> minecraftRenderTargets;

    private static CommonClass instance;
    private boolean shouldScale = false;
    public boolean hasRun = false;

    public static final ConfigHolder<RenderScaleConfig> CONFIG = RenderScaleConfig.init();

    public static void init() {
        instance = new CommonClass();
    }

    public static CommonClass getInstance() {
        return instance;
    }

    public static RenderScaleConfig getConfig() {
        return CONFIG.getConfig();
    }

    public void initMinecraftRenderTargets() {
        if (minecraftRenderTargets != null) {
            minecraftRenderTargets.clear();
        } else {
            minecraftRenderTargets = new HashSet<>();
        }

        minecraftRenderTargets.add(client.levelRenderer.entityOutlineTarget());
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
        resize(client.levelRenderer.entityOutlineTarget());
        resizeMinecraftRenderTargetSize();
    }

    public void resizeMinecraftRenderTargetSize() {
        initMinecraftRenderTargets();
        minecraftRenderTargets.forEach(this::resize);
    }

    public void setShouldScale(boolean shouldScale) {
        Window window = client.getWindow();
        int width = window.getWidth();
        int height = window.getHeight();

//        double scale = getConfig().scale;
        double scale = 0.5f;

        RenderTarget rt = client.getMainRenderTarget();

        if (shouldScale) {
            rt.resize((int) (width * scale), (int) (height * scale));
            x = RenderSystem.getDevice().createTexture("RenderScale Swap", TextureFormat.RGBA8, (int) (width * scale), (int) (height * scale), 1);
        } else {
            RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(
                    rt.getColorTexture(), x,
                    0, 0, 0, 0, 0,
                    (int) (width * scale), (int) (height * scale)
            );

            rt.resize(width, height);

            ((GICommandEncoderThing) RenderSystem.getDevice().createCommandEncoder()).renderScale$copyAndResizeTexture(
                    x, rt.getColorTexture(),
                    0, 0, 0, 0, 0,
                    (int) (width * scale), (int) (height * scale),
                    width, height
            );

            x.close();
        }
    }

    public double getCurrentScaleFactor() {
//        return shouldScale ? getConfig().scale : 1;
        return shouldScale ? 0.5 : 1;
//        return 0.5;
    }

    private Window getWindow() {
        return client.getWindow();
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
