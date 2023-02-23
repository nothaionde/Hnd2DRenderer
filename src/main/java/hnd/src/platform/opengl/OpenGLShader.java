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

public class OpenGLShader extends Shader {
    private String filepath;
    private HashMap<Integer, String> shaderSources;
    private int rendererID;

    public OpenGLShader(String filepath) {
        this.filepath = filepath;
        String source = readFile(filepath);
        shaderSources = preProcess(source);
        compileAndLinkShaders();
        logShaderInfo(filepath);
    }

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

        for (int i = 0; i < count[0]; i++)
        {
            GL20.glGetActiveUniform(rendererID, i, length, size, type, name);
            String nameFromByteBuffer = MemoryUtil.memUTF8(name);
            nameFromByteBuffer = nameFromByteBuffer.replace("\0", "");
            Logger.info("Uniform "+ i + " Type: " + type[0] +" Name: "+ nameFromByteBuffer);
        }
        name.clear();
    }

    private void compileAndLinkShaders() {
        // TODO add error checks for compiling and linking shaders
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
     * Searches the string for the first character that does not match any of the characters specified in its arguments.
     * The search only includes characters at or after position pos, ignoring any possible occurrences before that character.
     *
     * @param in    String to be used in the search.
     * @param notOf String to be used in the search.
     * @param from  Position of the first character in the string to be considered in the search.
     * @return The position of the first character that does not match. If no such characters are found, the function returns -1;
     */
    private int findFirstNotOf(String in, String notOf, int from) {
        for (int i = from; i < in.length(); i++) {
            if (notOf.indexOf(in.charAt(i)) == -1) {
                return i;
            }
        }
        return -1;
    }

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

    @Override
    public void bind() {
        GL20.glUseProgram(rendererID);
    }

    @Override
    public void setUniformMat4(String name, Matrix4f matrix) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(floatBuffer);
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
    }

    @Override
    public void uploadUniformIntArray(String name, int[] values) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        GL20.glUniform1iv(location, values);
    }

    @Override
    public void setFloat4(String name, Vector4f value) {
        uploadUniformFloat4(name, value);
    }

    private void uploadUniformFloat4(String name, Vector4f value) {
        int location = GL20.glGetUniformLocation(rendererID, name);
        GL20.glUniform4f(location, value.x, value.y, value.z, value.w);
    }
}
