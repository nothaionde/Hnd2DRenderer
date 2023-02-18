package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLVertexArray;

public abstract class VertexArray {

    public static VertexArray create() {

        switch (Renderer.getAPI()) {
            case NONE: {
                Logger.error("RendererAPI.None is currently not supported!");
                return null;
            }
            case OPENGL:
                return new OpenGLVertexArray();
        }
        Logger.error("Unknown RendererAPI!");
        return null;
    }

    public abstract void bind();

    public abstract IndexBuffer getIndexBuffer();

    public abstract void setIndexBuffer(IndexBuffer indexBuffer);

    public abstract void addVertexBuffer(VertexBuffer vertexBuffer);
}
