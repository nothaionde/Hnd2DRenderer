package hnd.src.renderer;


import hnd.src.scene.components.SpriteRendererComponent;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
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
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Float, "a_TilingFactor"));
        quadVertexBufferElements.add(new BufferElement(ShaderDataType.Int, "a_EntityID"));
        data.quadVertexBuffer.setLayout(new BufferLayout(quadVertexBufferElements));

        data.quadVertexArray.addVertexBuffer(data.quadVertexBuffer);

        data.quadVertexPositions[0] = new Vector4f(-0.5f, -0.5f, 0.0f, 1.0f);
        data.quadVertexPositions[1] = new Vector4f(0.5f, -0.5f, 0.0f, 1.0f);
        data.quadVertexPositions[2] = new Vector4f(0.5f, 0.5f, 0.0f, 1.0f);
        data.quadVertexPositions[3] = new Vector4f(-0.5f, 0.5f, 0.0f, 1.0f);

        int[] quadIndices = new int[data.maxIndexCount];
        int offset = 0;
        for (int i = 0; i < quadIndices.length; i += 6) {
            quadIndices[i + 0] = offset + 0;
            quadIndices[i + 1] = offset + 1;
            quadIndices[i + 2] = offset + 2;

            quadIndices[i + 3] = offset + 0;
            quadIndices[i + 4] = offset + 2;
            quadIndices[i + 5] = offset + 3;
            offset += 4;
        }
        IndexBuffer quadIB = IndexBuffer.create(quadIndices, data.maxIndexCount);
        data.quadVertexArray.setIndexBuffer(quadIB);

        data.whiteTexture = Texture2D.create(1, 1);
        ByteBuffer whiteTextureData = BufferUtils.createByteBuffer(4 * Byte.BYTES);
        whiteTextureData.putInt(0xFFFF_FFFF);
        data.whiteTexture.setData(whiteTextureData, 4);

        data.textureSlots.add(0, data.whiteTexture);

        data.quadShader = Shader.create("assets/shaders/Renderer2D_Quad.glsl");
    }

    public static void beginScene(EditorCamera camera) {
        data.viewProjection = camera.getViewProjection();
        data.quadShader.setUniformMat4("u_ViewProjection", data.viewProjection);
        startBatch();
    }

    private static void startBatch() {
        data.quadIndexCount = 0;
        vertexOffset = 0;
        Arrays.fill(data.quadVertexBufferData, 0);
        data.textureSlotIndex = 1;
    }

    public static void endScene() {
        flush();
    }

    private static void flush() {
        if (data.quadIndexCount != 0) {
            data.quadVertexBuffer.setData(data.quadVertexBufferData);
            for (int i = 0; i < data.textureSlotIndex; i++) {
                data.textureSlots.get(i).bind(i);
            }
            data.quadShader.bind();
            RenderCommand.drawIndexed(data.quadVertexArray, data.quadIndexCount);
            Statistics.drawCalls++;
        }
    }

    private static void nextBatch() {
        flush();
        startBatch();
    }

    public static void drawSprite(Matrix4f transform, SpriteRendererComponent src) {
        if (src.texture != null) {
            drawQuad(transform, src.texture, src.tilingFactor, src.color);
        } else {
            drawQuad(transform, src.color);
        }

    }

    private static void drawQuad(Matrix4f transform, Texture2D texture, float tilingFactor, Vector4f color) {
        int quadVertexCount = 4;
        float textureIndex = 0.0f; // white texture
        final Vector2f[] textureCoordinates = new Vector2f[4];
        textureCoordinates[0] = new Vector2f(0.0f, 0.0f);
        textureCoordinates[1] = new Vector2f(1.0f, 0.0f);
        textureCoordinates[2] = new Vector2f(1.0f, 1.0f);
        textureCoordinates[3] = new Vector2f(0.0f, 1.0f);
        for (int i = 1; i < data.textureSlotIndex; i++) { // texture slot 0 is for white texture
            if (data.textureSlots.get(i) == texture) {
                textureIndex = (float) i;
                break;
            }
        }
        if (textureIndex == 0.0f) {
            if (data.textureSlotIndex >= data.maxTextureSlots) {
                nextBatch();
            }
            textureIndex = (float) data.textureSlotIndex;
            data.textureSlots.add(data.textureSlotIndex, texture);
            data.textureSlotIndex++;
        }
        Vector3f position = new Vector3f();
        for (int i = 0; i < quadVertexCount; i++) {
            transform.transformPosition(data.quadVertexPositions[i].x, data.quadVertexPositions[i].y, data.quadVertexPositions[i].z, position);
            // Position vec3
            data.quadVertexBufferData[0 + vertexOffset] = position.x;
            data.quadVertexBufferData[1 + vertexOffset] = position.y;
            data.quadVertexBufferData[2 + vertexOffset] = position.z;
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
            // Tiling factor float
            data.quadVertexBufferData[10 + vertexOffset] = tilingFactor;
            // Entity ID int
            data.quadVertexBufferData[11 + vertexOffset] = -1;

            vertexOffset += 12;
        }
        data.quadIndexCount += 6;
        Statistics.quadCount++;
    }


    public static void drawQuad(Vector2f position, Vector2f size, Vector4f color) {
        drawQuad(new Vector3f(position.x, position.y, 0.0f), size, color);
    }

    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        Matrix4f transform = new Matrix4f().translate(position)
                .scale(size.x, size.y, 1.0f, new Matrix4f());
        drawQuad(transform, color);
    }

    public static void drawQuad(Matrix4f transform, Vector4f color) {
        int quadVertexCount = 4;
        float textureIndex = 0.0f; // white texture
        final Vector2f[] textureCoordinates = new Vector2f[4];
        textureCoordinates[0] = new Vector2f(0.0f, 0.0f);
        textureCoordinates[1] = new Vector2f(1.0f, 0.0f);
        textureCoordinates[2] = new Vector2f(1.0f, 1.0f);
        textureCoordinates[3] = new Vector2f(0.0f, 0.1f);
        float tilingFactor = 1.0f;
        Vector3f position = new Vector3f();
        for (int i = 0; i < quadVertexCount; i++) {
            transform.transformPosition(data.quadVertexPositions[i].x, data.quadVertexPositions[i].y, data.quadVertexPositions[i].z, position);
            // Position vec3
            data.quadVertexBufferData[0 + vertexOffset] = position.x;
            data.quadVertexBufferData[1 + vertexOffset] = position.y;
            data.quadVertexBufferData[2 + vertexOffset] = position.z;
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
            // Tiling factor float
            data.quadVertexBufferData[10 + vertexOffset] = tilingFactor;
            // Entity ID int
            data.quadVertexBufferData[11 + vertexOffset] = -1;

            vertexOffset += 12;
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
