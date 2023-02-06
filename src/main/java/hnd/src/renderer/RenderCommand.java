package hnd.src.renderer;

import org.joml.Vector4f;

public class RenderCommand {

	private static final RendererAPI rendererAPI = RendererAPI.create();

	private RenderCommand() {

	}

	public static void init() {
		rendererAPI.init();
	}

	public static void setViewport(int x, int y, int width, int height) {
		rendererAPI.setViewport(x, y, width, height);
	}

	public static void setClearColor(Vector4f color) {
		rendererAPI.setClearColor(color);
	}

	public static void clear() {
		rendererAPI.clear();
	}
}
