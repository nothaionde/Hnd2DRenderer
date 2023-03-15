package hnd.src.platform.opengl;

import hnd.src.core.Logger;
import hnd.src.renderer.GraphicsContext;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * An implementation of a {@link GraphicsContext} for OpenGL.
 */
public class OpenGLContext extends GraphicsContext {

    private long windowPtr;

    /**
     * Constructs a new OpenGL context with the given window pointer.
     *
     * @param windowPtr the pointer to the window to use for this context
     */
    public OpenGLContext(long windowPtr) {
        this.windowPtr = windowPtr;
        if (windowPtr == 0L) {
            Logger.warn("windowPtr in OpenGLContext constructor is 0L!");
        }
    }

    @Override
    public void init() {
        GLFW.glfwMakeContextCurrent(windowPtr);
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        Logger.info("OpenGL Info:");
        Logger.info("  Vendor: " + GL11.glGetString(GL11.GL_VENDOR));
        Logger.info("  Renderer: " + GL11.glGetString(GL11.GL_RENDERER));
        Logger.info("  Version: " + GL11.glGetString(GL11.GL_VERSION));
    }

    @Override
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(windowPtr);
    }
}
