package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.framebuffer.Framebuffer;
import hnd.src.renderer.framebuffer.FramebufferSpecification;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL45;

public class OpenGLFramebuffer extends Framebuffer {

	private int[] rendererID = {0};
	private int[] colorAttachments = {0};
	private int[] depthAttachments = {0};
	private FramebufferSpecification specification;

	public OpenGLFramebuffer(FramebufferSpecification specification) {
		this.specification = specification;
		invalidate();
	}

	private void dispose() {
		GL30.glDeleteFramebuffers(rendererID);
	}

	private void invalidate() {
		GL45.glCreateFramebuffers(rendererID);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, rendererID[0]);

		GL45.glCreateTextures(GL11.GL_TEXTURE_2D, colorAttachments);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorAttachments[0]);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8,
				specification.width, specification.height, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colorAttachments[0], 0);

		GL45.glCreateTextures(GL11.GL_TEXTURE_2D, depthAttachments);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthAttachments[0]);
		GL42.glTexStorage2D(GL11.GL_TEXTURE_2D, 1, GL30.GL_DEPTH24_STENCIL8, specification.width, specification.height);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthAttachments[0], 0);

		if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) == GL30.GL_FRAMEBUFFER_COMPLETE) {
			Logger.error("Framebuffer incomplete!");
		}

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	@Override
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, rendererID[0]);
	}

	@Override
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	@Override
	public int getSpecification() {
		return rendererID[0];
	}

	@Override
	public int getColorAttachmentRendererID() {
		return colorAttachments[0];
	}
}
