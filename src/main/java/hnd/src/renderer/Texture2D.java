package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLTexture2D;

import java.nio.ByteBuffer;

/**
 * The Texture2D abstract class represents a 2D texture object. It provides methods to create, bind, set data, get the renderer ID, and check if the texture is loaded.
 */
public abstract class Texture2D {
    /**
     * Creates a 2D texture object from the given image file path.
     *
     * @param path The image file path.
     * @return The created Texture2D object.
     */
    public static Texture2D create(String path) {
        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLTexture2D(path);
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Creates a 2D texture object with the given width and height.
     *
     * @param width  The width of the texture.
     * @param height The height of the texture.
     * @return The created Texture2D object.
     */
    public static Texture2D create(int width, int height) {
        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLTexture2D(width, height);
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Bind the texture to the specified texture slot.
     *
     * @param slot The texture slot to bind the texture to.
     */
    public abstract void bind(int slot);

    /**
     * Set the texture data.
     *
     * @param data The texture data as a ByteBuffer.
     * @param size The size of the data in bytes.
     */
    public abstract void setData(ByteBuffer data, int size);

    /**
     * Get the renderer ID of the texture.
     *
     * @return The renderer ID of the texture.
     */
    public abstract int getRendererID();

    /**
     * Check whether the texture has been loaded.
     *
     * @return True if the texture has been loaded, false otherwise.
     */
    public abstract boolean isLoaded();
}
