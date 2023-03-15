package hnd.src.renderer;


import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLRendererAPI;
import org.joml.Vector4f;

/**
 * The abstract class {@code RendererAPI} provides a common interface for different rendering APIs.
 * It defines methods to initialize the renderer, set clear color, clear the buffer, set viewport,
 * set line width, and draw indexed vertex arrays.
 * It also defines an enumerated type {@code API} to represent the supported rendering APIs.
 * <p>
 * This class contains a static field {@code api} that represents the default rendering API, which is
 * {@code API.OPENGL} and a method to retrieve the current rendering API.
 * </p>
 * <p>
 * The {@code create} method is used to create a renderer instance based on the current rendering API.
 * It returns null if the current rendering API is {@code API.NONE} or an unknown API.
 * </p>
 */
public abstract class RendererAPI {
    /**
     * The default rendering API.
     */
    private static final API api = API.OPENGL;

    /**
     * Returns the current rendering API.
     *
     * @return the current rendering API.
     */
    public static API getAPI() {
        return api;
    }

    /**
     * Creates a renderer instance based on the current rendering API.
     *
     * @return a renderer instance based on the current rendering API.
     */
    public static RendererAPI create() {
        switch (api) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLRendererAPI();
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Initializes the renderer.
     */
    public abstract void init();

    /**
     * Sets the clear color of the renderer.
     *
     * @param color the new clear color.
     */
    public abstract void setClearColor(Vector4f color);

    /**
     * Clears the buffer.
     */
    public abstract void clear();

    /**
     * Sets the viewport.
     *
     * @param x      the x-coordinate of the lower-left corner of the viewport.
     * @param y      the y-coordinate of the lower-left corner of the viewport.
     * @param width  the width of the viewport.
     * @param height the height of the viewport.
     */
    public abstract void setViewport(int x, int y, int width, int height);

    /**
     * Sets the line width.
     *
     * @param width the new line width.
     */
    public abstract void setLineWidth(float width);

    /**
     * Draws indexed vertex arrays.
     *
     * @param vertexArray the vertex array to draw.
     * @param indexCount  the number of indices to draw.
     */
    public abstract void drawIndexed(VertexArray vertexArray, int indexCount);

    /**
     * An enumerated type that represents the supported rendering APIs.
     */
    public enum API {
        NONE, OPENGL,
    }

}
