package hndeditor.src;

import hnd.src.core.Layer;
import hnd.src.events.Event;
import hnd.src.platform.opengl.OpenGLFramebuffer;
import hnd.src.renderer.RenderCommand;
import hnd.src.renderer.framebuffer.Framebuffer;
import hnd.src.renderer.framebuffer.FramebufferSpecification;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class EditorLayer extends Layer {
	int[] vertexArray = {0};
	int[] vertexBuffer = {0};
	int[] indexBuffer = {0};
	private Framebuffer framebuffer;

	@Override
	public void onAttach() {
		// Vertex Array
		GL30.glGenVertexArrays(vertexArray);
		GL30.glBindVertexArray(vertexArray[0]);
		// Vertex Buffer
		GL15.glGenBuffers(vertexBuffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer[0]);

		float[] vertices = {
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 0.5f, 0.0f,
		};
		// Upload data to GPU
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 3, 0);

		// Index Buffer
		GL15.glGenBuffers(indexBuffer);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);

		int[] indices = {
				0, 1, 2
		};
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		FramebufferSpecification framebufferSpecification = new FramebufferSpecification();
		framebufferSpecification.width = 1280;
		framebufferSpecification.height = 720;
		framebuffer = Framebuffer.create(framebufferSpecification);
	}

	@Override
	public void onDetach() {

	}

	@Override
	public void onUpdate(float ts) {
		framebuffer.bind();
		RenderCommand.setClearColor(new Vector4f(0.5f, 0, 0.5f, 1));
		RenderCommand.clear();

		GL30.glBindVertexArray(vertexArray[0]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_INT, 0);
		framebuffer.unbind();
	}

	@Override
	public void onImGuiRender() {
		ImVec2 viewportPanelSize = ImGui.getContentRegionAvail();
		ImGui.begin("Viewport");
		ImGui.image(framebuffer.getColorAttachmentRendererID(), viewportPanelSize.x, viewportPanelSize.y, 0, 1, 1, 0);
		ImGui.end();
	}

	@Override
	public void onEvent(Event event) {

	}
}
