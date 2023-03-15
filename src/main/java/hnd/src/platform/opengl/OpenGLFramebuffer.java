
package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.framebuffer.Framebuffer;
import hnd.src.renderer.framebuffer.FramebufferSpecification;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL45;

/**
 * An implementation of the {@link Framebuffer} class using OpenGL.
 */
public class OpenGLFramebuffer extends Framebuffer {

    private static final int TOTAL_TEXTURES = 4;
    private final int[] rendererID = {0};
    private final int[] colorAttachments = {0};
    private final int[] depthAttachments = {0};
    private final FramebufferSpecification specification;

    /**
     * Constructor for the OpenGLFramebuffer class.
     *
     * @param specification The specification for this framebuffer.
     */
    public OpenGLFramebuffer(FramebufferSpecification specification) {
        this.specification = specification;
        invalidate();
    }

    /**
     * Disposes of the OpenGLFramebuffer object and its associated resources.
     */
    private void dispose() {
        GL30.glDeleteFramebuffers(rendererID);
    }

    /**
     * Invalidates the current framebuffer object and creates a new one with the given specifications.
     */
    private void invalidate() {
        GL45.glCreateFramebuffers(rendererID);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, rendererID[0]);

        GL45.glCreateTextures(GL11.GL_TEXTURE_2D, colorAttachments);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachments[0]);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8,
                specification.width, specification.height, 0,
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachments[0], 0);

        GL45.glCreateTextures(GL11.GL_TEXTURE_2D, depthAttachments);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthAttachments[0]);
        GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL30.GL_DEPTH24_STENCIL8, specification.width, specification.height);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthAttachments[0], 0);

        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            Logger.error("Framebuffer is incomplete!");
        }

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Binds this framebuffer object.
     */
    @Override
    public void bind() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, rendererID[0]);
    }

    /**
     * Unbinds this framebuffer object.
     */
    @Override
    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    }

    /**
     * Returns the specification for this framebuffer object.
     *
     * @return The specification for this framebuffer object.
     */
    @Override
    public FramebufferSpecification getSpecification() {
        return specification;
    }

    /**
     * Returns the renderer ID of the color attachment.
     *
     * @return The renderer ID of the color attachment.
     */
    @Override
    public int getColorAttachmentRendererID() {
        return colorAttachments[0];
    }

    /**
     * Resizes the framebuffer to the given width and height.
     *
     * @param width  The new width of the framebuffer.
     * @param height The new height of the framebuffer.
     */
    @Override
    public void resize(int width, int height) {
        specification.width = width;
        specification.height = height;
    }
}