package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLVertexBuffer;

/**
 * A abstract class representing a vertex buffer.
 */
public abstract class VertexBuffer {

    /**
     * Creates a new vertex buffer object of specified size.
     *
     * @param size the size of the vertex buffer.
     * @return a new vertex buffer object of specified size.
     */
    public static VertexBuffer create(int size) {

        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLVertexBuffer(size);
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    /**
     * Sets the data for the vertex buffer.
     *
     * @param data the data to be set in the vertex buffer.
     */
    public abstract void setData(float[] data);

    /**
     * Sets the buffer layout for the vertex buffer.
     *
     * @param layout the buffer layout to be set for the vertex buffer.
     */
    public abstract void setLayout(BufferLayout layout);

    /**
     * Gets the buffer layout of the vertex buffer.
     *
     * @return the buffer layout of the vertex buffer.
     */
    public abstract BufferLayout getLayout();

    /**
     * Binds the vertex buffer object.
     */
    public abstract void bind();
}