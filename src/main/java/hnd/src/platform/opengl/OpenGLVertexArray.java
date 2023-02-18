package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;

import java.util.ArrayList;
import java.util.List;

public class OpenGLVertexArray extends VertexArray {

    private final int rendererID;
    private int vertexBufferIndex = 0;
    private IndexBuffer indexBuffer;
    private final List<VertexBuffer> vertexBuffers = new ArrayList<>();

    public OpenGLVertexArray() {
        rendererID = GL45.glCreateVertexArrays();
    }

    @Override
    public void bind() {
        GL30.glBindVertexArray(rendererID);
    }

    @Override
    public IndexBuffer getIndexBuffer() {
        return indexBuffer;
    }

    @Override
    public void setIndexBuffer(IndexBuffer indexBuffer) {
        GL30.glBindVertexArray(rendererID);
        indexBuffer.bind();
        this.indexBuffer = indexBuffer;
    }

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
                    GL45.glVertexAttribPointer(vertexBufferIndex,
                            element.getComponentCount(),
                            shaderDataTypeToOpenGLBaseType(element.type),
                            element.normalized,
                            layout.getStride(),
                            element.offset);
                    vertexBufferIndex++;
                }
                case Int, Int2, Int3, Int4, Boolean -> {
                    GL45.glEnableVertexAttribArray(vertexBufferIndex);
                    GL45.glVertexAttribIPointer(vertexBufferIndex,
                            element.getComponentCount(),
                            shaderDataTypeToOpenGLBaseType(element.type),
                            layout.getStride(),
                            element.offset);
                    vertexBufferIndex++;
                }
                case Mat3, Mat4 -> {
                    int count = element.getComponentCount();
                    for (int i = 0; i < count; i++) {
                        GL45.glEnableVertexAttribArray(vertexBufferIndex);
                        GL45.glVertexAttribPointer(vertexBufferIndex,
                                count,
                                shaderDataTypeToOpenGLBaseType(element.type),
                                element.normalized,
                                layout.getStride(),
                                element.offset + (long) Float.BYTES * count * i);
                        GL45.glVertexAttribDivisor(vertexBufferIndex, 1);
                        vertexBufferIndex++;
                    }
                }
                default -> Logger.error("Unknown ShaderDataType!");
            }
        }
        vertexBuffers.add(vertexBuffer);
    }

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
