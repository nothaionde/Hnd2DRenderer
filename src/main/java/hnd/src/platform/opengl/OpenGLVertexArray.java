package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the abstract {@link VertexArray} class using OpenGL.
 */
public class OpenGLVertexArray extends VertexArray {

    /**
     * The OpenGL ID of the vertex array object.
     */
    private final int rendererID;

    /**
     * The index of the current vertex buffer.
     */
    private int vertexBufferIndex = 0;

    /**
     * The index buffer associated with this vertex array.
     */
    private IndexBuffer indexBuffer;

    /**
     * The list of vertex buffers associated with this vertex array.
     */
    private final List<VertexBuffer> vertexBuffers = new ArrayList<>();

    /**
     * Creates a new {@code OpenGLVertexArray} object.
     */
    public OpenGLVertexArray() {
        rendererID = GL45.glCreateVertexArrays();
    }

    /**
     * Binds this vertex array for rendering.
     */
    @Override
    public void bind() {
        GL30.glBindVertexArray(rendererID);
    }

    /**
     * Returns the index buffer associated with this vertex array.
     *
     * @return the index buffer associated with this vertex array
     */
    @Override
    public IndexBuffer getIndexBuffer() {
        return indexBuffer;
    }

    /**
     * Sets the index buffer associated with this vertex array.
     *
     * @param indexBuffer the new index buffer to set
     */
    @Override
    public void setIndexBuffer(IndexBuffer indexBuffer) {
        GL30.glBindVertexArray(rendererID);
        indexBuffer.bind();
        this.indexBuffer = indexBuffer;
    }

    /**
     * Adds a vertex buffer to this vertex array.
     *
     * @param vertexBuffer the vertex buffer to add
     */
    @Override
    public void addVertexBuffer(VertexBuffer vertexBuffer) {
        if (vertexBuffer.getLayout().elements.size() == 0) {
            Logger.error("Vertex buffer has no layout!");
        }
        GL45.glBindVertexArray(rendererID);
        vertexBuffer.bind();

        BufferLayout layout = vertexBuffer.getLayout();
        for (BufferElement element : layout.elements) {
            switch (element.type) {
                case Float, Float2, Float3, Float4 -> {
                    GL45.glEnableVertexAttribArray(vertexBufferIndex);
                    GL45.glVertexAttribPointer(vertexBufferIndex, element.getComponentCount(), shaderDataTypeToOpenGLBaseType(element.type), element.normalized, layout.getStride(), element.offset);
                    vertexBufferIndex++;
                }
                case Int, Int2, Int3, Int4, Boolean -> {
                    GL45.glEnableVertexAttribArray(vertexBufferIndex);
                    GL45.glVertexAttribIPointer(vertexBufferIndex, element.getComponentCount(), shaderDataTypeToOpenGLBaseType(element.type), layout.getStride(), element.offset);
                    vertexBufferIndex++;
                }
                case Mat3, Mat4 -> {
                    int count = element.getComponentCount();
                    for (int i = 0; i < count; i++) {
                        GL45.glEnableVertexAttribArray(vertexBufferIndex);
                        GL45.glVertexAttribPointer(vertexBufferIndex, count, shaderDataTypeToOpenGLBaseType(element.type), element.normalized, layout.getStride(), element.offset + (long) Float.BYTES * count * i);
                        GL45.glVertexAttribDivisor(vertexBufferIndex, 1);
                        vertexBufferIndex++;
                    }
                }
                default -> Logger.error("Unknown ShaderDataType!");
            }
        }
        vertexBuffers.add(vertexBuffer);
    }


    /**
     * Converts a {@link ShaderDataType} to its corresponding OpenGL base type.
     *
     * @param type the {@code ShaderDataType} to convert
     * @return the corresponding OpenGL base type
     */
    private int shaderDataTypeToOpenGLBaseType(ShaderDataType type) {
        switch (type) {
            case Float:
            case Float2:
            case Float3:
            case Float4:
            case Mat3:
            case Mat4:
                return GL11.GL_FLOAT;
            case Int:
            case Int2:
            case Int3:
            case Int4:
                return GL11.GL_INT;
            case Boolean:
                return GL20.GL_BOOL;
            default: {
                Logger.error("Unknown ShaderDataType!");
                return 0;
            }
        }
    }
}
