package hnd.src.platform.opengl;

import hnd.src.renderer.BufferLayout;
import hnd.src.renderer.VertexBuffer;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL45;

import java.nio.FloatBuffer;

public class OpenGLVertexBuffer extends VertexBuffer {

    private final int rendererID;
    private BufferLayout layout;

    public OpenGLVertexBuffer(int size) {
        rendererID = GL45.glCreateBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, size, GL15.GL_DYNAMIC_DRAW);
    }

    @Override
    public void setData(float[] data) {
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
        GL45.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, data);
    }

    @Override
    public void setLayout(BufferLayout layout) {
        this.layout = layout;
    }

    @Override
    public BufferLayout getLayout() {
        return layout;
    }

    @Override
    public void bind() {
        GL20.glBindBuffer(GL15.GL_ARRAY_BUFFER, rendererID);
    }
}
