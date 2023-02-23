package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLShader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

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


	public abstract void setUniformMat4(String name, Matrix4f matrix);

	public abstract void uploadUniformIntArray(String name, int[] values);

	public abstract void setFloat4(String name, Vector4f value);
}
