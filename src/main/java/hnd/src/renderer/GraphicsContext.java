package hnd.src.renderer;


import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLContext;

/**
 * The abstract base class for graphics contexts.
 * <p>
 * A graphics context is responsible for managing the rendering surface
 * and other resources required for rendering. It provides methods for
 * initializing the context and swapping buffers.
 */
public abstract class GraphicsContext {

    /**
     * Creates a new graphics context for the specified window.
     *
     * @param windowPtr the window pointer for the window to create the context for
     * @return a new graphics context instance, or null if creation failed
     */
    public static GraphicsContext create(long windowPtr) {
        switch (Renderer.getAPI()) {
            case NONE:
                Logger.error("RendererAPI.NONE is currently not supported!");
                return null;
            case OPENGL:
                return new OpenGLContext(windowPtr);
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Initializes the graphics context.
     * <p>
     * This method should be called after creating the context and before
     * any rendering is performed.
     */
    public abstract void init();

    /**
     * Swaps the front and back buffers of the graphics context.
     * <p>
     * This method should be called after rendering is complete to update
     * the screen with the rendered content.
     */
    public abstract void swapBuffers();

}