package hnd.src.renderer;

import hnd.src.core.Logger;

public class BufferElement {
    public String name;
    public ShaderDataType type;
    public int size;
    public int offset;
    public boolean normalized;

    public BufferElement(ShaderDataType type, String name) {
        this.name = name;
        this.type = type;
        this.size = shaderDataTypeSize(type);
        offset = 0;
        normalized = false;
    }

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
                return 0;
            }
        }
    }

    public int getComponentCount() {
        switch (type) {
            case Float:
                return 1;
            case Float2:
                return 2;
            case Float3:
                return 3;
            case Float4:
                return 4;
            case Mat3:
                return 3; // 3* float3
            case Mat4:
                return 4; // 4* float4
            case Int:
                return 1;
            case Int2:
                return 2;
            case Int3:
                return 3;
            case Int4:
                return 4;
            case Boolean:
                return 1;
        }

        Logger.error("Unknown ShaderDataType!");
        return 0;
    }
}
