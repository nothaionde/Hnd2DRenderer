package hnd.src.renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates data necessary for rendering 2D quads.
 */
public class Render2DData {

    /**
     * The maximum number of quads that can be rendered.
     */
    public final int maxQuadCount = 500;

    /**
     * The maximum number of vertices that can be rendered.
     */
    public final int maxVertexCount = maxQuadCount * 4;

    /**
     * The maximum number of indices that can be rendered.
     */
    public final int maxIndexCount = maxQuadCount * 6;

    /**
     * The maximum number of texture slots that can be used.
     */
    public final int maxTextureSlots = 32;

    /**
     * The number of indices currently in the quad vertex buffer.
     */
    public int quadIndexCount = 0;

    /**
     * The index of the next available texture slot.
     */
    public int textureSlotIndex = 1; // 0 is white texture

    /**
     * The vertex buffer used to render quads.
     */
    public VertexBuffer quadVertexBuffer;

    /**
     * The positions of the vertices in a quad.
     */
    public Vector4f[] quadVertexPositions = new Vector4f[4];

    /**
     * The data buffer used to store quad vertex data.
     */
    public float[] quadVertexBufferData = new float[maxQuadCount];

    /**
     * The shader used to render quads.
     */
    public Shader quadShader;

    /**
     * The vertex array used to render quads.
     */
    public VertexArray quadVertexArray;

    /**
     * The view projection matrix used for rendering.
     */
    public Matrix4f viewProjection;

    /**
     * The white texture used as a fallback.
     */
    public Texture2D whiteTexture;

    /**
     * The list of textures currently bound.
     */
    public List<Texture2D> textureSlots = new ArrayList<>();
}
