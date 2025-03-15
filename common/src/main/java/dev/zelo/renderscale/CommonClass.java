package dev.zelo.renderscale;

import com.mojang.blaze3d.pipeline.MainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.Window;
import dev.zelo.renderscale.config.RenderScaleConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.HashSet;
import java.util.Set;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {
    private static final Minecraft client = Minecraft.getInstance();
    @Nullable
    private RenderTarget renderTarget;

    @Nullable
    private RenderTarget clientRenderTarget;

    private Set<RenderTarget> minecraftRenderTargets;

//    @Nullable
//    private Post renderTarget;

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

//        client.levelRenderer.initOutline();
        minecraftRenderTargets.add(client.levelRenderer.entityTarget());
        minecraftRenderTargets.remove(null);
    }

    public void onResolutionChanged() {
        if (getWindow() == null) return;
        Constants.LOG.info("Size changed to {}x{} {}x{} {}x{}",
                getWindow().getWidth(), getWindow().getHeight(),
                getWindow().getScreenWidth(), getWindow().getScreenHeight(),
                getWindow().getGuiScaledWidth(), getWindow().getGuiScaledHeight());

        Window window = client.getWindow();
        updateRenderTargetSize();

        // TODO: idk why but we gotta do this to make the glow stay...
        client.levelRenderer.resize(window.getGuiScaledWidth(), window.getGuiScaledHeight());
//        client.resizeDisplay();
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
        this.resize(client.levelRenderer.entityEffect);
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

    public void resize(@Nullable PostChain postChain) {
        if (renderTarget == null) return;

        boolean prev = shouldScale;
        shouldScale = true;

        Window window = client.getWindow();
        // The problem is this resizes the rendering, not the scaling after the rendering
        float s = getConfig().scale;
        float inverseScale = 1 / s;


        // 0.5 -> 1 / 0.5 => 2
//        float inverseScale = 4;

//        postChain.resize((int) (window.getWidth() / getConfig().scale), (int) (window.getHeight() / getConfig().scale));
//        postChain.resize((int) (window.getWidth()), (int) (window.getHeight()));
//        postChain.screenTarget.resize((int) (window.getWidth()), (int) (window.getHeight()), Minecraft.ON_OSX);
//        postChain.fullSizedTargets.get(0).resize((int) (window.getWidth() / (s * s)), (int) (window.getHeight() / (s * s)),  Minecraft.ON_OSX);
//        postChain.fullSizedTargets.get(0).blitToScreen((int) (window.getWidth() * s * s), (int) (window.getHeight() * s * s),  Minecraft.ON_OSX);
//        postChain.fullSizedTargets.getFirst().resize((int) (window.getWidth() / 2), (int) (window.getHeight() / 2), Minecraft.ON_OSX);
//        for (RenderTarget rendertarget : postChain.fullSizedTargets) {
//        }
//        Constants.LOG.info("BRUH MOMENT");
        postChain.fullSizedTargets.getFirst().resize(window.getWidth(), window.getHeight(), Minecraft.ON_OSX);
//        postChain.shaderOrthoMatrix.scale(inverseScale, inverseScale, 1.0F);
        postChain.shaderOrthoMatrix = new Matrix4f().setOrtho(0.0F, (float)postChain.screenTarget.width * inverseScale, 0.0F, (float)postChain.screenTarget.height * inverseScale, 0.1F, 1000.0F);

        shouldScale = prev;
    }
}
