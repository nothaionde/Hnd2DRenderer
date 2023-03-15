package hnd.src.renderer;

import hnd.src.core.Logger;

/**
 * Represents an element in a buffer layout.
 */
public class BufferElement {
    /**
     * The name of the buffer element.
     */
    public String name;

    /**
     * The data type of the buffer element.
     */
    public ShaderDataType type;

    /**
     * The size, in bytes, of the buffer element.
     */
    public int size;

    /**
     * The offset, in bytes, of the buffer element.
     */
    public int offset;

    /**
     * Indicates whether the buffer element is normalized.
     */
    public boolean normalized;

    /**
     * Constructs a new buffer element with the specified data type and name.
     *
     * @param type the data type of the buffer element
     * @param name the name of the buffer element
     */
    public BufferElement(ShaderDataType type, String name) {
        this.name = name;
        this.type = type;
        this.size = shaderDataTypeSize(type);
        offset = 0;
        normalized = false;
    }

    /**
     * Returns the size, in bytes, of the specified data type.
     *
     * @param type the data type
     * @return the size, in bytes, of the data type
     * @throws IllegalArgumentException if the data type is unknown
     */
    private int shaderDataTypeSize(ShaderDataType type) {
        switch (type) {
            case Float:
            case Int:
                return 4;
            case Float2:
            case Int2:
                return 4 * 2;
            case Float3:
            case Int3:
                return 4 * 3;
            case Float4:
            case Int4:
                return 4 * 4;
            case Mat3:
                return 4 * 3 * 3;
            case Mat4:
                return 4 * 4 * 4;
            case Boolean:
                return 1;
            default: {
                Logger.error("Unknown ShaderDataType!");
                throw new IllegalArgumentException("Unknown ShaderDataType!");
            }
        }
    }

    /**
     * Returns the number of components in the buffer element.
     *
     * @return the number of components
     * @throws IllegalArgumentException if the data type is unknown
     */
    public int getComponentCount() {
        switch (type) {
            case Float:
            case Int:
            case Boolean:
                return 1;
            case Float2:
            case Int2:
                return 2;
            case Float3:
            case Int3:
                return 3;
            case Float4:
            case Int4:
                return 4;
            case Mat3:
                return 3; // 3 * float3
            case Mat4:
                return 4; // 4 * float4
            default: {
                Logger.error("Unknown ShaderDataType!");
                throw new IllegalArgumentException("Unknown ShaderDataType!");
            }
        }
    }
}
