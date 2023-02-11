package hnd.src.renderer.framebuffer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLFramebuffer;
import hnd.src.renderer.Renderer;

public abstract class Framebuffer {

	public static Framebuffer create(FramebufferSpecification specification) {
		switch (Renderer.getAPI()) {
			case NONE -> {
				Logger.error("RendererAPI.None is currently not supported!");
				return null;
			}
			case OPENGL -> {
				return new OpenGLFramebuffer(specification);
			}
		}
		Logger.error("Unknown RendererAPI!");
		return null;
	}

	public abstract void bind();

	public abstract void unbind();

	public abstract int getSpecification();

	public abstract int getTextureID();
}
