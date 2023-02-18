package hnd.src.renderer;


import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Renderer2D {

    private static final Render2DData data = new Render2DData();
    private static int vertexOffset = 0;

    public static void init() {
        data.quadVertexArray = VertexArray.create();

        data.quadVertexBuffer = VertexBuffer.create(data.maxVertexCount);
        List<BufferElement> quadVertexBufferElements = new ArrayList<>();
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Float3, "a_Position"));
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Float4, "a_Color"));
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Float2, "a_TexCoord"));
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Float, "a_TexIndex"));
        data.quadVertexBuffer.setLayout(new BufferLayout(quadVertexBufferElements));

        data.quadVertexArray.addVertexBuffer(data.quadVertexBuffer);

        data.quadVertexPositions[0] = new Vector3f(-0.5f, -0.5f, 0.0f);
        data.quadVertexPositions[1] = new Vector3f(0.5f, -0.5f, 0.0f);
        data.quadVertexPositions[2] = new Vector3f(0.5f, 0.5f, 0.0f);
        data.quadVertexPositions[3] = new Vector3f(-0.5f, 0.5f, 0.0f);

        int[] quadIndices = new int[data.maxIndexCount];
        int offset = 0;
        for (int i = 0; i < quadIndices.length; i += 6) {
            quadIndices[i + 0] = 0 + offset;
            quadIndices[i + 1] = 1 + offset;
            quadIndices[i + 2] = 2 + offset;

            quadIndices[i + 3] = 2 + offset;
            quadIndices[i + 4] = 3 + offset;
            quadIndices[i + 5] = 0 + offset;
            offset += 4;
        }
        IndexBuffer quadIB = IndexBuffer.create(quadIndices, data.maxIndexCount);
        data.quadVertexArray.setIndexBuffer(quadIB);

//        data.quadShader = Shader.create("assets/shaders/Renderer2D_Quad.glsl");
        data.quadShader = Shader.create("assets/shaders/flatColor.glsl");
    }

    public static void beginScene(EditorCamera camera) {
        data.viewProjection = camera.getViewProjection();
        data.quadShader.setUniformMat4("u_ViewProj", data.viewProjection);
        data.quadShader.setUniformMat4("u_Transform", new Matrix4f().translate(new Vector3f()));
        startBatch();
    }

    private static void startBatch() {
        data.quadIndexCount = 0;
        vertexOffset = 0;
        Arrays.fill(data.quadVertexBufferData, 0);
        data.textureIndexSlot = 1;
    }

    public static void endScene() {
        flush();
    }

    private static void flush() {
        if (data.quadIndexCount != 0) {
            data.quadVertexBuffer.setData(data.quadVertexBufferData);
            data.quadShader.bind();
            RenderCommand.drawIndexed(data.quadVertexArray, data.quadIndexCount);
            Statistics.drawCalls++;
        }
    }

    public static void drawQuad(Vector3f position, Vector4f color) {
        int quadVertexCount = 4;
        float textureIndex = 0.0f; // white texture
        final Vector2f[] textureCoordinates = new Vector2f[4];
        textureCoordinates[0] = new Vector2f(0.0f, 0.0f);
        textureCoordinates[1] = new Vector2f(1.0f, 0.0f);
        textureCoordinates[2] = new Vector2f(1.0f, 1.0f);
        textureCoordinates[3] = new Vector2f(0.0f, 0.1f);
        float tilingFactor = 1.0f;
        for (int i = 0; i < quadVertexCount; i++) {
            // Position vec3
            data.quadVertexBufferData[0 + vertexOffset] = position.x * data.quadVertexPositions[i].x;
            data.quadVertexBufferData[1 + vertexOffset] = position.y * data.quadVertexPositions[i].y;
            data.quadVertexBufferData[2 + vertexOffset] = position.z * data.quadVertexPositions[i].z;
            // Color vec4
            data.quadVertexBufferData[3 + vertexOffset] = color.x;
            data.quadVertexBufferData[4 + vertexOffset] = color.y;
            data.quadVertexBufferData[5 + vertexOffset] = color.z;
            data.quadVertexBufferData[6 + vertexOffset] = color.w;
            // Texture Coordinates vec2
            data.quadVertexBufferData[7 + vertexOffset] = textureCoordinates[i].x;
            data.quadVertexBufferData[8 + vertexOffset] = textureCoordinates[i].y;
            // Texture index float
            data.quadVertexBufferData[9 + vertexOffset] = textureIndex;
            vertexOffset += 10;
        }
        data.quadIndexCount += 6;
        Statistics.quadCount++;
    }

    public static void resetStats() {
        Statistics.drawCalls = 0;
        Statistics.quadCount = 0;
    }


    public static class Statistics {

        public static int drawCalls = 0;
        public static int quadCount = 0;

        public static int getTotalVertexCount() {
            return quadCount * 4;
        }

        public static int getTotalIndexCount() {
            return quadCount * 6;
        }
    }

}
