package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLVertexArray;

/**
 * The VertexArray class provides a way to create and manipulate vertex arrays in a graphics rendering system.
 * <p>
 * It is an abstract class that must be extended by a concrete implementation for a specific rendering API.
 */
public abstract class VertexArray {

    /**
     * Creates a new VertexArray object using the current graphics rendering API.
     *
     * @return The new VertexArray object.
     * @throws UnsupportedOperationException if the current rendering API is not supported.
     */
    public static VertexArray create() throws UnsupportedOperationException {

        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLVertexArray();
        }

        throw new UnsupportedOperationException("Unknown RendererAPI!");
    }

    /**
     * Binds this VertexArray object to the current graphics rendering context.
     */
    public abstract void bind();

    /**
     * Gets the index buffer associated with this VertexArray object.
     *
     * @return The index buffer.
     */
    public abstract IndexBuffer getIndexBuffer();

    /**
     * Sets the index buffer associated with this VertexArray object.
     *
     * @param indexBuffer The new index buffer.
     */
    public abstract void setIndexBuffer(IndexBuffer indexBuffer);

    /**
     * Adds a vertex buffer to this VertexArray object.
     *
     * @param vertexBuffer The vertex buffer to add.
     */
    public abstract void addVertexBuffer(VertexBuffer vertexBuffer);
}
