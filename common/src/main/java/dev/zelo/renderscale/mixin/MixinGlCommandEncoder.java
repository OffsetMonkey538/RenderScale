package dev.zelo.renderscale.mixin;

import com.mojang.blaze3d.opengl.*;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.zelo.renderscale.CommonClass;
import dev.zelo.renderscale.accessors.GICommandEncoderThing;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL30C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GlCommandEncoder.class)
public abstract class MixinGlCommandEncoder implements GICommandEncoderThing {
    @Shadow
    private boolean inRenderPass;
    @Shadow
    @Final
    private int readFbo;

    @Shadow
    @Final
    private int drawFbo;

    @Shadow
    @Final
    private GlDevice device;

    @Shadow
    public abstract void copyTextureToTexture(GpuTexture source, GpuTexture destination, int mipLevel, int x, int y, int sourceX, int sourceY, int width, int height);

    @Unique
    public void renderScale$copyAndResizeTexture(GpuTexture source, GpuTexture destination,
                                                 int mipLevel, int destX, int destY,
                                                 int sourceX, int sourceY,
                                                 int sourceWidth, int sourceHeight,
                                                 int destWidth, int destHeight, boolean isDepth) {
        if (this.inRenderPass) {
            throw new IllegalStateException("Close the existing render pass before performing additional commands");
        } else if (mipLevel >= 0 && mipLevel < source.getMipLevels() && mipLevel < destination.getMipLevels()) {
            if (destX + destWidth > destination.getWidth(mipLevel) || destY + destHeight > destination.getHeight(mipLevel)) {
                throw new IllegalArgumentException("Destination rectangle exceeds texture bounds");
            } else if (sourceX + sourceWidth > source.getWidth(mipLevel) || sourceY + sourceHeight > source.getHeight(mipLevel)) {
                throw new IllegalArgumentException("Source rectangle exceeds texture bounds");
            } else if (source.isClosed() || destination.isClosed()) {
                throw new IllegalStateException("Source or destination texture is closed");
            } else {
                int p = ((GlTexture) source).glId();
                int q = ((GlTexture) destination).glId();

                MixinDirectStateAccess dsa = ((MixinDirectStateAccess) device.directStateAccess());

                // TODO: I *think* in 1.21.8 we can use a minecraft function for this again
                // Test
                GlStateManager.clearGlErrors();
                GlStateManager._disableScissorTest();


                //  Bind and attach the source textures
                GlStateManager._glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, this.readFbo);
//                GlStateManager._glFramebufferTexture2D(GL30C.GL_READ_FRAMEBUFFER, isDepth ? GL30C.GL_DEPTH_ATTACHMENT : GL30C.GL_COLOR_ATTACHMENT0, GL11C.GL_TEXTURE_2D, sourceId, 0);

                // Bind and attach the destination texture
                GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, this.drawFbo);
//                GlStateManager._glFramebufferTexture2D(GL30C.GL_DRAW_FRAMEBUFFER, isDepth ? GL30C.GL_DEPTH_ATTACHMENT : GL30C.GL_COLOR_ATTACHMENT0, GL11C.GL_TEXTURE_2D, destId, 0);

                boolean bl = source.getFormat().hasDepthAspect();

                // Test 2
//                dsa.invokeBindFrameBufferTextures(this.readFbo, sourceId, isDepth ? sourceId : 0, 0, GL30C.GL_READ_FRAMEBUFFER);
//                dsa.invokeBindFrameBufferTextures(this.drawFbo, isDepth ? destId : 0, destId, 0, GL30C.GL_DRAW_FRAMEBUFFER);
                dsa.invokeBindFrameBufferTextures(this.readFbo, bl ? 0 : p, bl ? p : 0, 0, 0);
                dsa.invokeBindFrameBufferTextures(this.drawFbo, bl ? 0 : q, bl ? q : 0, 0, 0);


                // Resize
                int mask = isDepth ? GL11C.GL_DEPTH_BUFFER_BIT : GL11C.GL_COLOR_BUFFER_BIT;
                int filter = CommonClass.getConfig().getFilter() ? GL11C.GL_LINEAR : GL11C.GL_NEAREST;

                // Force filter as nearest if this is a depth texture
                if (isDepth) filter = GL11C.GL_NEAREST;

//                dsa.invokeBindFrameBufferTextures(this.readFbo, sourceId, destId, 0, GL30C.GL_READ_FRAMEBUFFER, isDepth);
                dsa.invokeBlitFrameBuffers(this.readFbo, this.drawFbo, sourceX, sourceY, sourceX + sourceWidth, sourceY + sourceHeight,
                        destX, destY, destX + destWidth, destY + destHeight,
                        mask, filter);
            }
        }
    }
}