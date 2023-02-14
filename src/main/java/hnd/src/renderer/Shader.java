package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLShader;
import org.joml.Matrix4f;

public abstract class Shader {
	public static Shader create(String filepath) {

		switch (Renderer.getAPI()) {
			case NONE: {
				Logger.error("RendererAPI.None is currently not supported!");
				return null;
			}
			case OPENGL:
				return new OpenGLShader(filepath);
		}
		Logger.error("Unknown RendererAPI!");
		return null;
	}

	public abstract void bind();

	public abstract void setMat4(String name, Matrix4f value);

	public abstract void setUniformMat4(String name, Matrix4f matrix);

	public abstract void uploadUniformIntArray(String name, int[] values);
}
