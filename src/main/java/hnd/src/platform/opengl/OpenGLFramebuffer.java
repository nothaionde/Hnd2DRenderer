package hnd.src.platform.opengl;

import hnd.src.renderer.framebuffer.Framebuffer;
import hnd.src.renderer.framebuffer.FramebufferSpecification;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class OpenGLFramebuffer extends Framebuffer {

	private static final int TOTAL_TEXTURES = 4;
	private int[] rendererID = {0};
	private int[] textureIDs;
	private FramebufferSpecification specification;

	public OpenGLFramebuffer(FramebufferSpecification specification) {
		this.specification = specification;
		invalidate();
	}

	private void dispose() {
		GL30.glDeleteFramebuffers(rendererID);
	}

	private void invalidate() {
		rendererID[0] = GL30.glGenFramebuffers();
		bind();
		textureIDs = new int[TOTAL_TEXTURES];
		GL11.glGenTextures(textureIDs);
		for (int i = 0; i < TOTAL_TEXTURES; i++) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIDs[i]);
			int attachmentType;
			if (i == TOTAL_TEXTURES - 1) {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_DEPTH_COMPONENT32F,
						specification.width, specification.height, 0, GL11.GL_DEPTH_COMPONENT,
						GL11.GL_FLOAT, (ByteBuffer) null);
				attachmentType = GL30.GL_DEPTH_ATTACHMENT;
			} else {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGBA32F, specification.width, specification.height,
						0, GL11.GL_RGBA, GL11.GL_FLOAT, (ByteBuffer) null);
				attachmentType = GL30.GL_COLOR_ATTACHMENT0 + i;
			}
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachmentType, GL11.GL_TEXTURE_2D, textureIDs[i], 0);
		}

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer intBuff = stack.mallocInt(TOTAL_TEXTURES);
			for (int i = 0; i < TOTAL_TEXTURES; i++) {
				intBuff.put(i, GL30.GL_COLOR_ATTACHMENT0 + i);
			}
			GL20.glDrawBuffers(intBuff);
		}
	}

	@Override
	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, rendererID[0]);
	}

	@Override
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
	}

	@Override
	public int getSpecification() {
		return rendererID[0];
	}

	@Override
	public int getTextureID() {
		return textureIDs[0];
	}
}
