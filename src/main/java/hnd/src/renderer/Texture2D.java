package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLTexture2D;

public abstract class Texture2D {
	public static Texture2D create(String path) {

		switch (Renderer.getAPI()) {
			case NONE: {
				Logger.error("RendererAPI.None is currently not supported!");
				return null;
			}
			case OPENGL:
				return new OpenGLTexture2D(path);
		}
		Logger.error("Unknown RendererAPI!");
		return null;
	}

	public abstract void bind(int slot);
}
