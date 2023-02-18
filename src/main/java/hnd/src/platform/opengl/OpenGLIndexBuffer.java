package hnd.src.platform.opengl;

import hnd.src.renderer.IndexBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL45;

public class OpenGLIndexBuffer extends IndexBuffer {
    private final int count;
    private final int rendererID;

    public OpenGLIndexBuffer(int[] indices, int count) {
        this.count = count;
        rendererID = GL45.glCreateBuffers();
        // GL_ELEMENT_ARRAY_BUFFER is not valid without an actively bound VAO
        // Binding with GL_ARRAY_BUFFER allows the data to be loaded regardless of VAO state.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rendererID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void bind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, rendererID);
    }
}
