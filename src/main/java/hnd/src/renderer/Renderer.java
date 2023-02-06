package hnd.src.renderer;

public class Renderer {
	public static RendererAPI.API getAPI() {
		return RendererAPI.getAPI();
	}

	public static void init() {
		RenderCommand.init();
		Renderer2D.init();
	}

	public static void onWindowResize(int width, int height) {
		RenderCommand.setViewport(0, 0, width, height);
	}
}
