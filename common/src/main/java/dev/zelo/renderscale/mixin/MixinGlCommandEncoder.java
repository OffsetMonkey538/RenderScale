package dev.zelo.renderscale.mixin;

import com.jogamp.opengl.GL;
import com.mojang.blaze3d.opengl.*;
import com.mojang.blaze3d.textures.GpuTexture;
import dev.zelo.renderscale.accessors.GICommandEncoderThing;
//import dev.zelo.renderscale.accessors.MixinDirectStateAccessThing;
import org.lwjgl.opengl.ARBDirectStateAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GlCommandEncoder.class)
public abstract class MixinGlCommandEncoder implements GICommandEncoderThing {
    @Shadow
    private boolean inRenderPass;

//    @Shadow
//    @Final
//    private GlDevice device;

    @Shadow
    @Final
    private int readFbo;

    @Shadow
    @Final
    private int drawFbo;

    @Unique
    public void renderScale$copyAndResizeTexture(GpuTexture source, GpuTexture destination,
                                                 int mipLevel, int destX, int destY,
                                                 int sourceX, int sourceY,
                                                 int sourceWidth, int sourceHeight,
                                                 int destWidth, int destHeight) {
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
                boolean isDepth = source.getFormat().hasDepthAspect();
                int sourceId = ((GlTexture) source).glId();
                int destId = ((GlTexture) destination).glId();

                // Bind source to read framebuffer
//                this.device.directStateAccess().bindFrameBufferTextures(
//                        this.readFbo, isDepth ? 0 : sourceId, isDepth ? sourceId : 0, 0, 0);

                ARBDirectStateAccess.glNamedFramebufferTexture(this.readFbo, 36064, isDepth ? 0 : sourceId, 0);
                ARBDirectStateAccess.glNamedFramebufferTexture(this.readFbo, 36096, isDepth ? sourceId : 0, 0);
//                if (0 != 0) {
//                    GlStateManager._glBindFramebuffer(m, i);
//                }

                // Bind destination to draw framebuffer
//                this.device.directStateAccess().bindFrameBufferTextures(
//                        this.drawFbo, isDepth ? 0 : destId, isDepth ? destId : 0, 0, 0);

                ARBDirectStateAccess.glNamedFramebufferTexture(this.drawFbo, 36064, isDepth ? 0 : destId, 0);
                ARBDirectStateAccess.glNamedFramebufferTexture(this.drawFbo, 36096, isDepth ? destId : 0, 0);
//                if (m != 0) {
//                    GlStateManager._glBindFramebuffer(m, i);
//                }

                // Blit with resize
//                ((MixinDirectStateAccessThing)(Object) this.device.directStateAccess()).renderScale$blitFrameBuffers(
//                this.device.directStateAccess().blitFrameBuffers(
//                        sourceX, sourceY, sourceWidth, sourceHeight,
//                        destX, destY, destWidth, destHeight,
//                        isDepth ? 256 : 16384,
//                        9729); // GL_LINEAR for smoother scaling

                ARBDirectStateAccess.glBlitNamedFramebuffer(this.readFbo, this.drawFbo, sourceX, sourceY, sourceWidth, sourceHeight,
                        destX, destY, destWidth, destHeight,
                        isDepth ? 256 : 16384,
// TODO: CommonClass.getConfig().getFilter() ? GL.GL_LINEAR : GL.GL_NEAREST
                        GL.GL_NEAREST);
//                        GL.GL_LINEAR);
            }
        }
    }
}