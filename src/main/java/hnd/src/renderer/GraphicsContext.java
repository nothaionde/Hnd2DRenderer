package hnd.src.renderer;


import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLContext;

public abstract class GraphicsContext {
	public static GraphicsContext create(long windowPtr) {
		switch (Renderer.getAPI()) {
			case NONE:
				Logger.error("RendererAPI.NONE is currently not supported!");
				return null;
			case OPENGL:
				return new OpenGLContext(windowPtr);
		}
		Logger.error("Unknown RendererAPI!");
		return null;
	}

	public abstract void init();

	public abstract void swapBuffers();
}