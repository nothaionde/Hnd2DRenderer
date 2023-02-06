package hnd.src.renderer;


import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLContext;

public abstract class GraphicsContext {
	public static GraphicsContext create(long windowPtr) {
		switch (Renderer.getAPI()) {
			case NONE:
				Logger.error("RendererAPI.NONE is currently not supported!");
				System.exit(-1);
				break;
			case OPENGL:
				return new OpenGLContext(windowPtr);
		}
		Logger.error("Unknown RendererAPI!");
		System.exit(-1);
		// Unreachable return option!
		return null;
	}

	public abstract void init();

	public abstract void swapBuffers();
}