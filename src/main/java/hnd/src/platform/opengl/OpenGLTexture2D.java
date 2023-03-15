package hnd.src.platform.opengl;

import hnd.src.renderer.Texture2D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL45;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

/**
 * This class implements a 2D texture using OpenGL. It extends the Texture2D abstract class and provides
 * implementation for the abstract methods. The texture can be loaded from a file or created with a specified
 * width and height. The texture can be bound to a texture unit and data can be set for the texture.
 */
public class OpenGLTexture2D extends Texture2D {
    /**
     * The OpenGL renderer ID for the texture.
     */
    private final int[] rendererID = {0};
    /**
     * The path to the file from which the texture is loaded.
     */
    private String path;

    /**
     * A flag indicating whether the texture has been loaded successfully.
     */
    private boolean isLoaded = false;

    /**
     * The width of the texture.
     */
    private int width;

    /**
     * The height of the texture.
     */
    private int height;

    /**
     * The internal format of the texture.
     */
    private int internalFormat;

    /**
     * The data format of the texture.
     */
    private int dataFormat;


    /**
     * Constructs a new OpenGLTexture2D object and loads the texture from the specified file.
     *
     * @param path the path to the file from which the texture is loaded
     */
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


    /**
     * Constructs a new OpenGLTexture2D object with the specified width and height.
     *
     * @param width  the width of the texture
     * @param height the height of the texture
     */
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


    /**
     * Binds the texture to the specified texture unit.
     *
     * @param slot the texture unit to which the texture is bound
     */
    @Override
    public void bind(int slot) {
        GL45.glBindTextureUnit(slot, rendererID[0]);
    }


    /**
     * Sets the data for the texture.
     *
     * @param data the data for the texture
     * @param size the size of the data
     */
    @Override
    public void setData(ByteBuffer data, int size) {
        GL45.glTextureSubImage2D(rendererID[0], 0, 0, 0, width, height, dataFormat, GL11.GL_UNSIGNED_BYTE, data.flip());
    }


    /**
     * Gets the OpenGL renderer ID for the texture.
     *
     * @return the renderer ID for the texture
     */
    @Override
    public int getRendererID() {
        return rendererID[0];
    }


    /**
     * Returns whether the texture has been loaded successfully.
     *
     * @return true if the texture has been loaded successfully; false otherwise
     */
    @Override
    public boolean isLoaded() {
        return isLoaded;
    }
}
