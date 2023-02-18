package hnd.src.renderer;

import java.util.List;

public class BufferLayout {
    public List<BufferElement> elements;
    public int stride = 0;

    public BufferLayout(List<BufferElement> elements) {
        this.elements = elements;
        calculateOffsetsAndStride();
    }

    private void calculateOffsetsAndStride() {
        int offset = 0;
        stride = 0;
        for (BufferElement element : elements) {
            element.offset = offset;
            offset += element.size;
            stride += element.size;
        }
    }

    public int getStride() {
        return stride;
    }
}
