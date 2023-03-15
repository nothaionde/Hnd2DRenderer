package hnd.src.renderer;

/**
 * The Renderer class provides methods for initializing and configuring the rendering system.
 */
public class Renderer {

    /**
     * Returns the currently active rendering API.
     *
     * @return the currently active rendering API
     */
    public static RendererAPI.API getAPI() {
        return RendererAPI.getAPI();
    }

    /**
     * Initializes the rendering system and any required resources.
     */
    public static void init() {
        RenderCommand.init();
        Renderer2D.init();
    }

    /**
     * Called when the window is resized.
     *
     * @param width  the new width of the window
     * @param height the new height of the window
     */
    public static void onWindowResize(int width, int height) {
        RenderCommand.setViewport(0, 0, width, height);
    }
}