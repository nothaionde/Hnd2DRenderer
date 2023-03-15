package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLIndexBuffer;

/**
 * An abstract class representing an index buffer, which stores indices for rendering.
 * This class provides a factory method for creating index buffers based on the current
 * renderer API. The concrete implementation of this class will depend on the current
 * renderer API, and must implement the `getCount()` and `bind()` methods.
 */
public abstract class IndexBuffer {

    /**
     * Creates a new index buffer based on the current renderer API.
     *
     * @param indices an array of integers representing the indices
     * @param size    the size of the index buffer, in bytes
     * @return a new index buffer, or `null` if the current renderer API is not supported
     */
    public static IndexBuffer create(int[] indices, int size) {

        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLIndexBuffer(indices, size);
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Returns the number of indices in the index buffer.
     *
     * @return the number of indices in the index buffer
     */
    public abstract int getCount();

    /**
     * Binds the index buffer for rendering.
     */
    public abstract void bind();
}
