package hnd.src.platform.opengl;

import hnd.src.renderer.IndexBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL45;

/**
 * This class represents an OpenGL index buffer.
 */
public class OpenGLIndexBuffer extends IndexBuffer {

    private final int count;
    private final int rendererID;

    /**
     * Constructs a new OpenGL index buffer with the given indices and count.
     *
     * @param indices The indices for the buffer.
     * @param count   The number of indices in the buffer.
     */
    public OpenGLIndexBuffer(int[] indices, int count) {
        this.count = count;
        rendererID = GL45.glCreateBuffers();
        // GL_ELEMENT_ARRAY_BUFFER is not valid without an actively bound VAO
        // Binding with GL_ARRAY_BUFFER allows the data to be loaded regardless of VAO state.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rendererID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
    }

    /**
     * Returns the number of indices in the buffer.
     *
     * @return The number of indices in the buffer.
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * Binds the buffer for rendering.
     */
    @Override
    public void bind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rendererID);
    }
}
