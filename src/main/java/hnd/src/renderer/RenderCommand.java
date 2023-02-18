package hnd.src.renderer;

import org.joml.Vector4f;

public class RenderCommand {

	private static final RendererAPI rendererAPI = RendererAPI.create();

	private RenderCommand() {

	}

	public static void init() {
		assert rendererAPI != null;
		rendererAPI.init();
	}

	public static void setViewport(int x, int y, int width, int height) {
		assert rendererAPI != null;
		rendererAPI.setViewport(x, y, width, height);
	}

	public static void setClearColor(Vector4f color) {
		assert rendererAPI != null;
		rendererAPI.setClearColor(color);
	}

	public static void clear() {
		assert rendererAPI != null;
		rendererAPI.clear();
	}

    public static void drawIndexed(VertexArray vertexArray, int indexCount) {
		assert rendererAPI != null;
		rendererAPI.drawIndexed(vertexArray, indexCount);
    }
}
