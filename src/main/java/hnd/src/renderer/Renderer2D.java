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

/**
 * The Renderer2D class provides functionality for rendering 2D graphics.
 */
public class Renderer2D {
    /**
     * The Render2DData class encapsulates data that is used by Renderer2D.
     */
    private static final Render2DData data = new Render2DData();

    /**
     * The vertex offset.
     */
    private static int vertexOffset = 0;

    /**
     * Initializes the Renderer2D.
     */
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

    /**
     * Begins the 2D rendering scene.
     *
     * @param camera the EditorCamera to use for rendering
     */
    public static void beginScene(EditorCamera camera) {
        data.viewProjection = camera.getViewProjection();
        data.quadShader.setUniformMat4("u_ViewProjection", data.viewProjection);
        startBatch();
    }

    /**
     * Initializes the Renderer2D.
     */
    private static void startBatch() {
        data.quadIndexCount = 0;
        vertexOffset = 0;
        Arrays.fill(data.quadVertexBufferData, 0);
        data.textureSlotIndex = 1;
    }

    /**
     * Ends the current scene.
     */
    public static void endScene() {
        flush();
    }

    /**
     * This method flushes the renderer data and draws the quads.
     */
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

    /**
     * This method starts a new batch of quads.
     */
    private static void nextBatch() {
        flush();
        startBatch();
    }

    /**
     * This method draws a sprite with the given transform and sprite renderer component.
     *
     * @param transform the transform
     * @param src       the sprite renderer component
     */
    public static void drawSprite(Matrix4f transform, SpriteRendererComponent src) {
        if (src.texture != null) {
            drawQuad(transform, src.texture, src.tilingFactor, src.color);
        } else {
            drawQuad(transform, src.color);
        }

    }

    /**
     * This method draws a quad with the given transform, texture, tiling factor, and color.
     *
     * @param transform    the transform
     * @param texture      the texture
     * @param tilingFactor the tiling factor
     * @param color        the color
     */
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

    /**
     * Draws a quad with a position, size, and color.
     *
     * @param position the position of the quad
     * @param size     the size of the quad
     * @param color    the color of the quad
     */
    public static void drawQuad(Vector3f position, Vector2f size, Vector4f color) {
        Matrix4f transform = new Matrix4f().translate(position).scale(size.x, size.y, 1.0f, new Matrix4f());
        drawQuad(transform, color);
    }

    /**
     * Draws a quad with a transformation matrix and a color.
     *
     * @param transform the transformation matrix to apply to the quad
     * @param color     the color of the quad
     */
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


    /**
     * Resets the statistics for the number of draw calls and quad counts.
     */
    public static void resetStats() {
        Statistics.drawCalls = 0;
        Statistics.quadCount = 0;
    }

    /**
     * The Statistics class contains the statistics for the number of draw calls and quad counts.
     */
    public static class Statistics {

        public static int drawCalls = 0;
        public static int quadCount = 0;

        /**
         * Returns the total number of vertices.
         *
         * @return Total vertex count.
         */
        public static int getTotalVertexCount() {
            return quadCount * 4;
        }

        /**
         * Returns the total number of indices.
         *
         * @return Total index count.
         */
        public static int getTotalIndexCount() {
            return quadCount * 6;
        }
    }

}
