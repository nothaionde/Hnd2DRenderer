package hnd.src.renderer.framebuffer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLFramebuffer;
import hnd.src.renderer.Renderer;

/**
 * The abstract base class for framebuffers.
 */
public abstract class Framebuffer {
    /**
     * Creates a new framebuffer with the specified specification.
     *
     * @param specification the specification for the framebuffer
     * @return the new framebuffer
     */
    public static Framebuffer create(FramebufferSpecification specification) {
        switch (Renderer.getAPI()) {
            case NONE -> {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL -> {
                return new OpenGLFramebuffer(specification);
            }
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Binds the framebuffer.
     */
    public abstract void bind();

    /**
     * Unbinds the framebuffer.
     */
    public abstract void unbind();

    /**
     * Gets the specification for the framebuffer.
     *
     * @return the specification for the framebuffer
     */
    public abstract FramebufferSpecification getSpecification();

    /**
     * Gets the renderer ID of the color attachment for the framebuffer.
     *
     * @return the renderer ID of the color attachment
     */
    public abstract int getColorAttachmentRendererID();

    /**
     * Resizes the framebuffer to the specified width and height.
     *
     * @param width  the new width of the framebuffer
     * @param height the new height of the framebuffer
     */
    public abstract void resize(int width, int height);
}
