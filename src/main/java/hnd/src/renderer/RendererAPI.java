package hnd.src.renderer;


import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLRendererAPI;
import org.joml.Vector4f;

public abstract class RendererAPI {

	private static final API api = API.OPENGL;

	public static API getAPI() {
		return api;
	}

	public static RendererAPI create() {
		switch (api) {
			case NONE: {
				Logger.error("RendererAPI.None is currently not supported!");
				return null;
			}
			case OPENGL:
				return new OpenGLRendererAPI();
		}
		Logger.error("Unknown RendererAPI!");
		return null;
	}

	public abstract void init();

	public abstract void setClearColor(Vector4f color);

	public abstract void clear();


	public abstract void setViewport(int x, int y, int width, int height);


	public abstract void setLineWidth(float width);

	public enum API {
		NONE,
		OPENGL,
	}

}
