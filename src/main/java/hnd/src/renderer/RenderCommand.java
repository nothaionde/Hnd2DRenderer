package hnd.src.renderer;

import org.joml.Vector4f;

/**
 * A static class that provides methods for rendering graphics using a specific renderer API.
 */
public class RenderCommand {

    private static final RendererAPI rendererAPI = RendererAPI.create();

    private RenderCommand() {

    }

    /**
     * Initializes the renderer API. This method must be called before any other methods in this class can be used.
     */
    public static void init() {
        assert rendererAPI != null;
        rendererAPI.init();
    }

    /**
     * Sets the viewport for rendering. The viewport determines the region of the window that is used for rendering.
     *
     * @param x      the x-coordinate of the lower-left corner of the viewport
     * @param y      the y-coordinate of the lower-left corner of the viewport
     * @param width  the width of the viewport
     * @param height the height of the viewport
     */
    public static void setViewport(int x, int y, int width, int height) {
        assert rendererAPI != null;
        rendererAPI.setViewport(x, y, width, height);
    }

    /**
     * Sets the clear color for the color buffer. The clear color is used to clear the color buffer when the
     * {@link #clear()} method is called.
     *
     * @param color the clear color to use
     */
    public static void setClearColor(Vector4f color) {
        assert rendererAPI != null;
        rendererAPI.setClearColor(color);
    }

    /**
     * Clears the color and depth buffer. The clear color is set using the {@link #setClearColor(Vector4f)} method.
     */
    public static void clear() {
        assert rendererAPI != null;
        rendererAPI.clear();
    }

    /**
     * Draws the specified number of indices from the specified vertex array.
     *
     * @param vertexArray the vertex array to draw
     * @param indexCount  the number of indices to draw
     */
    public static void drawIndexed(VertexArray vertexArray, int indexCount) {
        assert rendererAPI != null;
        rendererAPI.drawIndexed(vertexArray, indexCount);
    }
}
