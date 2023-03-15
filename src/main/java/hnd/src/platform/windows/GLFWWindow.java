package hnd.src.platform.windows;

import hnd.src.core.Logger;
import hnd.src.core.Window;
import hnd.src.events.ApplicationEvent;
import hnd.src.events.KeyEvent;
import hnd.src.events.MouseEvent;
import hnd.src.renderer.GraphicsContext;
import hnd.src.renderer.Renderer;
import hnd.src.renderer.RendererAPI;
import hnd.src.utils.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.util.Objects;

/**
 * A window implementation using the GLFW library for the Windows platform.
 */
public class GLFWWindow extends Window {

    /**
     * The pointer to the GLFW window.
     */
    private static long windowPtr;

    /**
     * The graphics context associated with the GLFW window.
     */
    private static GraphicsContext context;

    /**
     * Creates a new GLFW window with the specified properties.
     *
     * @param windowProps The properties of the window.
     */
    public GLFWWindow(WindowProps windowProps) {
        init(windowProps);
    }

    /**
     * Handles GLFW error callbacks.
     *
     * @param error       The error code.
     * @param description The description of the error.
     */
    private static void glfwErrorCallback(int error, long description) {
        Logger.error("Error code: " + error + ", msg: " + MemoryUtil.memUTF8(description));
    }

    /**
     * Initializes the GLFW window with the specified properties.
     *
     * @param windowProps The properties of the window.
     */
    private static void init(WindowProps windowProps) {
        WindowData.title = windowProps.title;
        WindowData.width = windowProps.width;
        WindowData.height = windowProps.height;
        Logger.info("Creating window: " + windowProps.title + " (" + windowProps.width + ", " + windowProps.height + ")");

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            Logger.error("Couldn't initialize GLFW!");
        }

        // Set the GLFW error callback
        GLFW.glfwSetErrorCallback(GLFWWindow::glfwErrorCallback);

        // Set the OpenGL debug context hint if in debug mode
        if (Constants.DEBUG) {
            if (Renderer.getAPI() == RendererAPI.API.OPENGL) {
                GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
            }
        }

        // Create the GLFW window and graphics context
        windowPtr = GLFW.glfwCreateWindow(windowProps.width, windowProps.height, WindowData.title, 0L, 0L);
        context = GraphicsContext.create(windowPtr);
        assert context != null;
        context.init();

        // Enable VSync by default
        setVSync(true);

        // Set the GLFW event callbacks
        setGLFWCallbacks();
    }

    /**
     * Sets the GLFW event callbacks.
     */
    private static void setGLFWCallbacks() {
        GLFW.glfwSetWindowSizeCallback(windowPtr, ApplicationEvent::windowSizeCallback);
        GLFW.glfwSetWindowCloseCallback(windowPtr, ApplicationEvent::windowCloseEvent);
        GLFW.glfwSetKeyCallback(windowPtr, KeyEvent::keyCallbackEvent);
        GLFW.glfwSetMouseButtonCallback(windowPtr, MouseEvent::mouseButtonCallbackEvent);
        GLFW.glfwSetScrollCallback(windowPtr, MouseEvent::mouseScrollCallbackEvent);
        GLFW.glfwSetCursorPosCallback(windowPtr, MouseEvent::mousePosCallbackEvent);
    }

    /**
     * Sets whether VSync is enabled or disabled.
     *
     * @param enabled Whether VSync is enabled.
     */
    private static void setVSync(boolean enabled) {
        if (enabled) {
            GLFW.glfwSwapInterval(1);
        } else {
            GLFW.glfwSwapInterval(0);
        }
        WindowData.vSync = enabled;
    }

    /**
     * Updates the GLFW window and swaps the graphics buffers.
     */
    @Override
    public void update() {
        GLFW.glfwPollEvents();
        context.swapBuffers();
    }

    /**
     * Gets the native window pointer for the GLFW window.
     *
     * @return The native window pointer for the GLFW window.
     */
    @Override
    public long getNativeWindow() {
        return windowPtr;
    }

    /**
     * Gets the width of the GLFW window.
     *
     * @return The width of the GLFW window.
     */
    @Override
    public int getWidth() {
        return WindowData.width;
    }

    /**
     * Gets the height of the GLFW window.
     *
     * @return The height of the GLFW window.
     */
    @Override
    public int getHeight() {
        return WindowData.height;
    }

    /**
     * Disposes of the GLFW window and frees up any associated resources.
     */
    @Override
    public void dispose() {
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
        GLFW.glfwDestroyWindow(windowPtr);
    }

    /**
     * Represents the data for the GLFW window.
     */
    public static class WindowData {

        /**
         * The title of the GLFW window.
         */
        public static String title;

        /**
         * The width of the GLFW window.
         */
        public static int width;

        /**
         * The height of the GLFW window.
         */
        public static int height;

        /**
         * Whether or not V-Sync is enabled for the GLFW window.
         */
        public static boolean vSync;

        /**
         * Private constructor to prevent instantiation.
         */
        private WindowData() {
        }
    }

}