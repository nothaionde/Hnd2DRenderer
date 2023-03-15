package hnd.src.renderer;

import java.util.List;

/**
 * Represents the layout of a vertex buffer, specifying the format and organization of the data in the buffer.
 */
public class BufferLayout {

    /**
     * The list of elements in the buffer layout.
     */
    public List<BufferElement> elements;

    /**
     * The stride (in bytes) of the buffer layout.
     */
    public int stride = 0;

    /**
     * Constructs a new BufferLayout object with the specified list of buffer elements.
     *
     * @param elements the list of buffer elements in the buffer layout
     */
    public BufferLayout(List<BufferElement> elements) {
        this.elements = elements;
        calculateOffsetsAndStride();
    }

    /**
     * Calculates the offsets and stride of the buffer layout based on its list of elements.
     */
    private void calculateOffsetsAndStride() {
        int offset = 0;
        stride = 0;
        for (BufferElement element : elements) {
            element.offset = offset;
            offset += element.size;
            stride += element.size;
        }
    }

    /**
     * Returns the stride (in bytes) of the buffer layout.
     *
     * @return the stride of the buffer layout
     */
    public int getStride() {
        return stride;
    }
}