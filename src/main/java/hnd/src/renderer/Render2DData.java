package hnd.src.renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class Render2DData {
    public final int maxQuadCount = 500;
    public final int maxVertexCount = maxQuadCount * 4;
    public final int maxIndexCount = maxQuadCount * 6;
    public final int maxTextureSlots = 32;
    public int quadIndexCount = 0;
    public int textureSlotIndex = 1; // 0 is white texture
    public VertexBuffer quadVertexBuffer;
    public Vector4f[] quadVertexPositions = new Vector4f[4];
    public float[] quadVertexBufferData = new float[maxQuadCount];
    public Shader quadShader;
    public VertexArray quadVertexArray;
    public Matrix4f viewProjection;
    public Texture2D whiteTexture;
    public List<Texture2D> textureSlots = new ArrayList<>();
}
