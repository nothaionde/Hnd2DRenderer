package hnd.src.renderer;

import hnd.src.core.Logger;
import hnd.src.platform.opengl.OpenGLShader;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * This abstract class represents a shader used for rendering.
 * It provides methods for creating, binding and setting uniform values.
 */
public abstract class Shader {
    /**
     * Creates a new shader based on the API used by the Renderer.
     *
     * @param filepath the path to the shader file
     * @return a new Shader object
     */
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

    /**
     * Binds the shader for use in rendering.
     */
    public abstract void bind();

    /**
     * Sets the value of a matrix uniform.
     *
     * @param name   the name of the uniform variable
     * @param matrix the matrix to be uploaded
     */
    public abstract void setUniformMat4(String name, Matrix4f matrix);

    /**
     * Uploads an integer array to a uniform variable.
     *
     * @param name   the name of the uniform variable
     * @param values the integer array to be uploaded
     */
    public abstract void uploadUniformIntArray(String name, int[] values);

    /**
     * Sets the value of a 4-component float uniform.
     *
     * @param name  the name of the uniform variable
     * @param value the vector containing the 4-component float values
     */
    public abstract void setFloat4(String name, Vector4f value);
}
