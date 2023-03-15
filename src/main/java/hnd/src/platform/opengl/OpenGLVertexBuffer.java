package hnd.src.platform.opengl;

import hnd.src.renderer.BufferLayout;
import hnd.src.renderer.VertexBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL45;

/**
 * Represents a vertex buffer in OpenGL.
 */
public class OpenGLVertexBuffer extends VertexBuffer {

    /**
     * The ID of the OpenGL buffer object.
     */
    private final int rendererID;

    /**
     * The layout of the vertex buffer.
     */
    private BufferLayout layout;

    /**
     * Constructs a new OpenGL vertex buffer with the specified size.
     *
     * @param size the size of the vertex buffer
     */
    public OpenGLVertexBuffer(int size) {
        rendererID = GL45.glCreateBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, GL15.GL_DYNAMIC_DRAW);
    }

    /**
     * Sets the data of the vertex buffer.
     *
     * @param data the data to set
     */
    @Override
    public void setData(float[] data) {
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
        GL45.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
    }

    /**
     * Sets the layout of the vertex buffer.
     *
     * @param layout the layout to set
     */
    @Override
    public void setLayout(BufferLayout layout) {
        this.layout = layout;
    }

    /**
     * Returns the layout of the vertex buffer.
     *
     * @return the layout of the vertex buffer
     */
    @Override
    public BufferLayout getLayout() {
        return layout;
    }

    /**
     * Binds the vertex buffer.
     */
    @Override
    public void bind() {
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
    }
}