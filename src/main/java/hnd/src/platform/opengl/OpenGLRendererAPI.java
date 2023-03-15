package hnd.src.platform.opengl;

import hnd.src.renderer.RendererAPI;
import hnd.src.renderer.VertexArray;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

/**
 * This class represents an OpenGL renderer API.
 */
public class OpenGLRendererAPI extends RendererAPI {

    /**
     * Initializes the renderer API.
     */
    @Override
    public void init() {
    }

    /**
     * Sets the clear color for the renderer.
     *
     * @param color The color to set as the clear color.
     */
    @Override
    public void setClearColor(Vector4f color) {
        GL11.glClearColor(color.x, color.y, color.z, color.w);
    }

    /**
     * Clears the screen.
     */
    @Override
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Sets the viewport.
     *
     * @param x      The x position of the viewport.
     * @param y      The y position of the viewport.
     * @param width  The width of the viewport.
     * @param height The height of the viewport.
     */
    @Override
    public void setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
    }

    /**
     * Sets the line width.
     *
     * @param width The width of the lines to draw.
     */
    @Override
    public void setLineWidth(float width) {
        GL11.glLineWidth(width);
    }

    /**
     * Draws the vertex array using indices.
     *
     * @param vertexArray The vertex array to draw.
     * @param indexCount  The number of indices to use for drawing.
     */
    @Override
    public void drawIndexed(VertexArray vertexArray, int indexCount) {
        vertexArray.bind();
        int count = (indexCount != 0) ? indexCount : vertexArray.getIndexBuffer().getCount();
        GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, 0);
    }

}
