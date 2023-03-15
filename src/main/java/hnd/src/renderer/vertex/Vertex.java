package hnd.src.renderer.vertex;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Represents a vertex with position, color, texture coordinates, and a texture ID.
 */
public class Vertex {
    /**
     * The position of the vertex.
     */
    public Vector3f position = new Vector3f();

    /**
     * The color of the vertex.
     */
    public Vector4f color = new Vector4f();

    /**
     * The texture coordinates of the vertex.
     */
    public Vector2f texCoords = new Vector2f();

    /**
     * The ID of the texture for the vertex.
     */
    public float texID;

    /**
     * The size of the vertex in bytes.
     * Size of Vertex buffer: position 3 + color 4 + texCoords 2 + texID 1.
     */
    private final int size = 10 * Float.BYTES;

    /**
     * Returns the size of the vertex in bytes.
     *
     * @return the size of the vertex in bytes
     */
    public int size() {
        return size;
    }

    /**
     * Returns the offset of the position in the vertex buffer.
     *
     * @return the offset of the position in the vertex buffer
     */
    public int getPositionOffset() {
        return 0;
    }

    /**
     * Returns the offset of the color in the vertex buffer.
     *
     * @return the offset of the color in the vertex buffer
     */
    public int getColorOffset() {
        return 12;
    }

    /**
     * Returns the offset of the texture coordinates in the vertex buffer.
     *
     * @return the offset of the texture coordinates in the vertex buffer
     */
    public int getTexCoordsOffset() {
        return 28;
    }

    /**
     * Returns the offset of the texture ID in the vertex buffer.
     *
     * @return the offset of the texture ID in the vertex buffer
     */
    public int getTexIDOffset() {
        return 36;
    }

    /**
     * Returns the vertex as an array of floats.
     *
     * @return the vertex as an array of floats
     */
    public float[] asFloatArray() {
        return new float[]{position.x, position.y, position.z, color.x, color.y, color.z, color.w, texCoords.x, texCoords.y, texID};
    }
}
