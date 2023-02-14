package test;

import hnd.src.core.Layer;
import hnd.src.events.Event;
import hnd.src.renderer.OrthographicCameraController;
import hnd.src.renderer.Shader;
import hnd.src.renderer.Texture2D;
import hnd.src.renderer.vertex.Vertex;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;

public class TestLayer extends Layer {
	Vector4f clearColor = new Vector4f(1.1f, 0.1f, 0.1f, 1);
	int quadVA;
	int quadVB;
	int[] indices;
	int quadIB;
	OrthographicCameraController cameraController;
	Shader flatColorShader;
	Matrix4f vp = new Matrix4f();
	Texture2D pixelBobTexture;
	int[] quadCount = {10, 10};
	int quadSize;


	int maxQuadCount = 1000;
	int maxVertexCount = maxQuadCount * 4;
	int maxIndexCount = maxQuadCount * 6;

	@Override
	public void onAttach() {
		cameraController = new OrthographicCameraController(1280.0f / 720.0f);

		flatColorShader = Shader.create("assets/shaders/flatColor.glsl");
		pixelBobTexture = Texture2D.create("assets/textures/PixelBob.png");
		flatColorShader.bind();
		int[] samplers = {0, 1};
		flatColorShader.uploadUniformIntArray("u_Textures", samplers);


		GL11.glClearColor(0, 0, 1, 1);

		Vertex v = new Vertex();

		quadVA = GL45.glCreateVertexArrays();
		GL30.glBindVertexArray(quadVA);

		quadVB = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVB);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, (long) v.size() * maxVertexCount, GL15.GL_DYNAMIC_DRAW);

		GL45.glEnableVertexArrayAttrib(quadVA, 0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, v.size(), v.getPositionOffset());

		GL45.glEnableVertexArrayAttrib(quadVA, 1);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, v.size(), v.getColorOffset());

		GL45.glEnableVertexArrayAttrib(quadVA, 2);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, v.size(), v.getTexCoordsOffset());

		GL45.glEnableVertexArrayAttrib(quadVA, 2);
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, v.size(), v.getTexIDOffset());

//		indices = new int[]{
//				0, 1, 2, 2, 3, 0,
//				4, 5, 6, 6, 7, 4
//		};
		indices = new int[maxIndexCount];
		int offset = 0;
		for (int i = 0; i < indices.length; i += 6) {
			indices[i + 0] = 0 + offset;
			indices[i + 1] = 1 + offset;
			indices[i + 2] = 2 + offset;

			indices[i + 3] = 2 + offset;
			indices[i + 4] = 3 + offset;
			indices[i + 5] = 0 + offset;
			offset += 4;
		}

		// Each quad is 4 vertices
		quadSize = v.size() * 4;

		quadIB = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIB);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void onDetach() {

	}

	@Override
	public void onUpdate(float ts) {
		cameraController.onUpdate(ts);

		// Set dynamic vertex buffer
//		// 3 floats position, 4 floats color, 2 floats texture coordinates, 1 float texture id
//		vertices = new float[]{
//				-1.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
//				-0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
//				-0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f,
//				-1.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
//
//				0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
//				1.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f,
//				1.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
//				0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
//		};

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(quadSize * maxQuadCount);
		int indexCount = 0;
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 20; x++) {
				FloatBuffer b1 = createQuad(x, y, 0);
				verticesBuffer.put(b1);
				indexCount += 6;
			}
		}
		verticesBuffer.flip();

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVB);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, verticesBuffer);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

		flatColorShader.bind();
		pixelBobTexture.bind(1);

		Matrix4f viewProjection = cameraController.getCamera().getViewProjectionMatrix();

		flatColorShader.setUniformMat4("u_ViewProj", viewProjection);
		flatColorShader.setUniformMat4("u_Transform", new Matrix4f().translate(new Vector3f()));

		GL30.glBindVertexArray(quadVA);
		GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
	}

	public FloatBuffer createQuad(float x, float y, float textureID) {

		float size = 1.0f;

		Vertex v0 = new Vertex();
		v0.position = new Vector3f(x, y, 0.0f);
		v0.color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		v0.texCoords = new Vector2f(0.0f, 0.0f);
		v0.texID = textureID;

		Vertex v1 = new Vertex();
		v1.position = new Vector3f(x + size, y, 0.0f);
		v1.color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		v1.texCoords = new Vector2f(1.0f, 0.0f);
		v1.texID = textureID;


		Vertex v2 = new Vertex();
		v2.position = new Vector3f(x + size, y + size, 0.0f);
		v2.color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		v2.texCoords = new Vector2f(1.0f, 1.0f);
		v2.texID = textureID;

		Vertex v3 = new Vertex();
		v3.position = new Vector3f(x, y + size, 0.0f);
		v3.color = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		v3.texCoords = new Vector2f(0.0f, 1.0f);
		v3.texID = textureID;

		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(v0.size() + v1.size() + v2.size() + v3.size());
		floatBuffer.put(v0.asFloatArray());
		floatBuffer.put(v1.asFloatArray());
		floatBuffer.put(v2.asFloatArray());
		floatBuffer.put(v3.asFloatArray());
		floatBuffer.flip();

		return floatBuffer;
	}

	@Override
	public void onImGuiRender() {
	}

	@Override
	public void onEvent(Event event) {

	}
}
