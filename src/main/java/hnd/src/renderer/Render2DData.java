package hnd.src.renderer;

import hnd.src.renderer.vertex.Vertex;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Render2DData {

    public final int maxQuadCount = 500;
    public final int maxVertexCount = maxQuadCount * 4;
    public final int maxIndexCount = maxQuadCount * 6;
    public int quadIndexCount = 0;
    public int textureIndexSlot = 1; // 0 is white texture
    public VertexBuffer quadVertexBuffer;
    public Vector3f[] quadVertexPositions = new Vector3f[4];
    public float[] quadVertexBufferData = new float[maxQuadCount];
    public Shader quadShader;
    public VertexArray quadVertexArray;
    public Matrix4f viewProjection;
}
