package hnd.src.platform.opengl;

import hnd.src.renderer.RendererAPI;
import hnd.src.renderer.VertexArray;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL45;


public class OpenGLRendererAPI extends RendererAPI {
    @Override
    public void init() {
    }

    @Override
    public void setClearColor(Vector4f color) {
        GL11.glClearColor(color.x, color.y, color.z, color.w);
    }

    @Override
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    @Override
    public void setLineWidth(float width) {
        GL11.glLineWidth(width);
    }

    @Override
    public void drawIndexed(VertexArray vertexArray, int indexCount) {
        vertexArray.bind();
        int count = (indexCount != 0) ? indexCount : vertexArray.getIndexBuffer().getCount();
        GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, 0);
    }


}