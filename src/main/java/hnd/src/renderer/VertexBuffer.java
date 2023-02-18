package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLVertexBuffer;

import java.nio.FloatBuffer;
import java.util.List;

public abstract class VertexBuffer {
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

    public abstract void setData(float[] data);

    public abstract void setLayout(BufferLayout layout);

    public abstract BufferLayout getLayout();

    public abstract void bind();
}
