package hnd.src.platform.opengl;

import hnd.src.renderer.Texture2D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL45;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

public class OpenGLTexture2D extends Texture2D {

    private final int[] rendererID = {0};
    private String path;
    private boolean isLoaded = false;
    private int width;
    private int height;
    private int internalFormat;
    private int dataFormat;

    public OpenGLTexture2D(String path) {
        this.path = path;
        int[] widthArray = {0};
        int[] heightArray = {0};
        int[] channelsArray = {0};
        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer data = STBImage.stbi_load(path, widthArray, heightArray, channelsArray, 0);
        if (data != null) {
            isLoaded = true;
            width = widthArray[0];
            height = widthArray[0];
            int innerInternalFormat = 0;
            int innerDataFormat = 0;
            if (channelsArray[0] == 4) {
                innerInternalFormat = GL11.GL_RGBA8;
                innerDataFormat = GL11.GL_RGBA;
            } else if (channelsArray[0] == 3) {
                innerInternalFormat = GL11.GL_RGB8;
                innerDataFormat = GL11.GL_RGB;
            }
            internalFormat = innerInternalFormat;
            dataFormat = innerDataFormat;
            GL45.glCreateTextures(GL11.GL_TEXTURE_2D, rendererID);
            GL45.glTextureStorage2D(rendererID[0], 1, internalFormat, width, height);
            GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL45.glTextureSubImage2D(rendererID[0], 0, 0, 0, width, height, dataFormat, GL11.GL_UNSIGNED_BYTE, data);
            STBImage.stbi_image_free(data);
        }
    }

    public OpenGLTexture2D(int width, int height) {
        this.width = width;
        this.height = height;

        internalFormat = GL11.GL_RGBA8;
        dataFormat = GL11.GL_RGBA;

        GL45.glCreateTextures(GL11.GL_TEXTURE_2D, rendererID);
        rendererID[0] = GL45.glCreateTextures(GL11.GL_TEXTURE_2D);
        GL45.glTextureStorage2D(rendererID[0], 1, internalFormat, width, height);

        GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL45.glTextureParameteri(rendererID[0], GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    @Override
    public void bind(int slot) {
        GL45.glBindTextureUnit(slot, rendererID[0]);
    }

    @Override
    public void setData(ByteBuffer data, int size) {
        GL45.glTextureSubImage2D(rendererID[0], 0, 0, 0, width, height, dataFormat, GL11.GL_UNSIGNED_BYTE, data.flip());
    }

    @Override
    public int getRendererID() {
        return rendererID[0];
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }
}
