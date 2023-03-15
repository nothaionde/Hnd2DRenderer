package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.Shader;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an OpenGL shader program.
 */
public class OpenGLShader extends Shader {
    private String filepath;
    private HashMap<Integer, String> shaderSources;
    private int rendererID;

    /**
     * Constructs a new OpenGLShader object from a file path.
     *
     * @param filepath the path to the shader source file.
     */
    public OpenGLShader(String filepath) {
        this.filepath = filepath;
        String source = readFile(filepath);
        shaderSources = preProcess(source);
        compileAndLinkShaders();
        logShaderInfo(filepath);
    }

    /**
     * Logs information about the shader to the console.
     *
     * @param filepath the path to the shader source file.
     */
    private void logShaderInfo(String filepath) {
        int[] count = {0};
        int[] size = {0};
        int[] type = {0};
        int bufSize = 20;
        ByteBuffer name = BufferUtils.createByteBuffer(bufSize);
        int[] length = {0};
        Logger.info("Shader path: " + filepath);

        // Attributes
        GL20.glGetProgramiv(rendererID, GL20.GL_ACTIVE_ATTRIBUTES, count);
        Logger.info("Active Attributes: " + count[0]);

        for (int i = 0; i < count[0]; i++) {
            GL20.glGetActiveAttrib(rendererID, i, length, size, type, name);
            String nameFromByteBuffer = MemoryUtil.memUTF8(name);
            nameFromByteBuffer = nameFromByteBuffer.replace("\0", "");
            Logger.info("Attribute " + i + " Type: " + type[0] + " Name: " + nameFromByteBuffer);
        }

        // Uniforms
        GL20.glGetProgramiv(rendererID, GL20.GL_ACTIVE_UNIFORMS, count);
        Logger.info("Active Uniforms: " + count[0]);

        for (int i = 0; i < count[0]; i++) {
            GL20.glGetActiveUniform(rendererID, i, length, size, type, name);
            String nameFromByteBuffer = MemoryUtil.memUTF8(name);
            nameFromByteBuffer = nameFromByteBuffer.replace("\0", "");
            Logger.info("Uniform " + i + " Type: " + type[0] + " Name: " + nameFromByteBuffer);
        }
        name.clear();
    }

    /**
     * Compiles and links the shaders to the shader program.
     */
    private void compileAndLinkShaders() {
        int program = GL20.glCreateProgram();
        List<Integer> shaderObjects = new ArrayList<>();
        shaderSources.forEach((type, source) -> {
            int shader = GL20.glCreateShader(type);
            shaderObjects.add(shader);
            GL20.glShaderSource(shader, source);
            GL20.glCompileShader(shader);
            int[] isCompiled = {0};
            GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, isCompiled);
            if (isCompiled[0] == GL11.GL_FALSE) {
                String info = GL20.glGetShaderInfoLog(shader);
                Logger.error("Shader compilation error: " + info);
            }
            GL20.glAttachShader(program, shader);
        });
        GL20.glLinkProgram(program);
        int[] isLinked = {0};
        GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, isLinked);
        if (isLinked[0] == GL11.GL_FALSE) {
            String info = GL20.glGetShaderInfoLog(program);
            Logger.error("Shader link error: " + info);
        }
        shaderObjects.forEach(type -> {
            GL20.glDetachShader(program, type);
            GL20.glDeleteShader(type);
        });
        rendererID = program;
    }

    /**
     * Preprocesses the shader source code and returns a map of shader types and their sources.
     *
     * @param source the source code to preprocess
     * @return a map of shader types and their sources
     */
    private HashMap<Integer, String> preProcess(String source) {
        HashMap<Integer, String> shaderSources = new HashMap<>();

        String typeToken = "#type";
        int typeTokenLength = typeToken.length();
        int pos = source.indexOf(typeToken, 0); //Start of shader type declaration line
        while (pos != -1) {
            int eol = source.indexOf("\r\n", pos); //End of shader type declaration line
            if (eol == -1) {
                Logger.error("OpenGLShader.java preProcess() syntax error!");
            }
            int begin = pos + typeTokenLength + 1; //Start of shader type name (after "#type " keyword)
            String type = source.substring(begin, eol);
            if (shaderTypeFromString(type) == 0) {
                Logger.error("Invalid shader type specified");
            }

            int nextLinePos = findFirstNotOf(source, "\r\n", eol); //Start of shader code after shader type declaration line
            if (nextLinePos == -1) {
                Logger.error("Syntax error");
            }
            pos = source.indexOf(typeToken, nextLinePos); //Start of next shader type declaration line
            shaderSources.put(shaderTypeFromString(type), (pos == -1) ? source.substring(nextLinePos) : source.substring(nextLinePos, pos));
        }
        return shaderSources;
    }

    /**
     * Finds the position of the first character in a string that is not in another string.
     *
     * @param in    the string to search
     * @param notOf the string to exclude from the search
     * @param from  the starting position of the search
     * @return the position of the first character that is not in the exclude string, or -1 if not found
     */
    private int findFirstNotOf(String in, String notOf, int from) {
        for (int i = from; i < in.length(); i++) {
            if (notOf.indexOf(in.charAt(i)) == -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determines the OpenGL shader type based on a string representation.
     *
     * @param type the string representation of the shader type
     * @return the OpenGL shader type
     */
    private int shaderTypeFromString(String type) {
        if (type.equals("vertex")) {
            return GL20.GL_VERTEX_SHADER;
        }
        if (type.equals("fragment") || type.equals("pixel")) {
            return GL20.GL_FRAGMENT_SHADER;
        }
        Logger.error("Unknown shader type!");
        return 0;
    }

    /**
     * Reads the contents of a file and returns them as a string.
     *
     * @param filepath the path to the file to read
     * @return the contents of the file as a string
     */
    private String readFile(String filepath) {
        String result = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result == null) {

            Logger.error("Could not read from file " + filepath);
        }
        return result;
    }

    /**
     * Binds this shader program for use.
     */
    @Override
    public void bind() {
        GL20.glUseProgram(rendererID);
    }

    /**
     * Sets the value of a matrix uniform in the shader.
     *
     * @param name   the name of the uniform
     * @param matrix the matrix to set the uniform to
     */
    @Override
    public void setUniformMat4(String name, Matrix4f matrix) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(floatBuffer);
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
    }

    /**
     * Uploads an integer array to a uniform in the shader.
     *
     * @param name   the name of the uniform
     * @param values the integer array to upload
     */
    @Override
    public void uploadUniformIntArray(String name, int[] values) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        GL20.glUniform1iv(location, values);
    }

    /**
     * Sets the value of a Vector4f uniform in the shader.
     *
     * @param name  the name of the uniform
     * @param value the value to set the uniform to
     */
    @Override
    public void setFloat4(String name, Vector4f value) {
        uploadUniformFloat4(name, value);
    }

    /**
     * Uploads a Vector4f to a uniform in the shader.
     *
     * @param name  the name of the uniform
     * @param value the Vector4f to upload
     */
    private void uploadUniformFloat4(String name, Vector4f value) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }
}
