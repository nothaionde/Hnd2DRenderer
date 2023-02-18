package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLIndexBuffer;
import hnd.src.platform.opengl.OpenGLVertexBuffer;

public abstract class IndexBuffer {
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

    public abstract int getCount();

    public abstract void bind();
}
