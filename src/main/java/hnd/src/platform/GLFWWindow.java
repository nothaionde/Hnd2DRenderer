package hnd.src.platform;

import hnd.src.core.Logger;
import hnd.src.core.Window;
import hnd.src.events.ApplicationEvent;
import hnd.src.events.Event;
import hnd.src.events.KeyEvent;
import hnd.src.events.MouseEvent;
import hnd.src.renderer.GraphicsContext;
import hnd.src.renderer.Renderer;
import hnd.src.renderer.RendererAPI;
import hnd.src.utils.Constants;
import org.lwjgl.glfw.GLFW;

import java.util.Queue;


/**
 * Windows platform depended main window initialization and creating.
 */
public class GLFWWindow extends Window {

	public static int glfwWindowCount = 0;
	private WindowData data;
	private long windowPtr;
	private GraphicsContext context;

	public GLFWWindow(WindowProps windowProps) {
		init(windowProps);
	}

	/**
	 * GLFW error callback handler wrapper.
	 *
	 * @param error       the error code
	 * @param description a pointer to a UTF-8 encoded string describing the error.
	 */
	private static void glfwErrorCallback(int error, long description) {
		System.out.println(description);
	}

	/**
	 * Creates a window and its associated OpenGL context.
	 * How the window and its context should be created are specified with window hints
	 *
	 * @param windowProps {@link hnd.src.core.Window.WindowProps} window properties.
	 */
	private void init(WindowProps windowProps) {
		data = new WindowData();
		data.title = windowProps.title;
		data.width = windowProps.width;
		data.height = windowProps.height;
		Logger.info("Creating window: " + windowProps.title + " (" + windowProps.width + ", " + windowProps.height + ")");
		if (glfwWindowCount == 0) {
			if (!GLFW.glfwInit()) {
				Logger.error("Couldn't initialize GLFW!");
				System.exit(-1);
			}
			GLFW.glfwSetErrorCallback(GLFWWindow::glfwErrorCallback);
		}
		if (Constants.DEBUG) {
			if (Renderer.getAPI() == RendererAPI.API.OPENGL) {
				GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
			}
		}
		windowPtr = GLFW.glfwCreateWindow(windowProps.width, windowProps.height, data.title, 0L, 0L);
		glfwWindowCount++;
		context = GraphicsContext.create(windowPtr);
		context.init();
		setVSync(true);
		setGLFWCallbacks();
	}

	/**
	 * Sets the callbacks for the specified window.
	 */
	private void setGLFWCallbacks() {
		GLFW.glfwSetWindowSizeCallback(windowPtr, ApplicationEvent::windowSizeCallback);
		GLFW.glfwSetWindowCloseCallback(windowPtr, ApplicationEvent::windowCloseEvent);
		GLFW.glfwSetKeyCallback(windowPtr, KeyEvent::keyCallbackEvent);
		GLFW.glfwSetMouseButtonCallback(windowPtr, MouseEvent::mouseButtonCallbackEvent);
		GLFW.glfwSetScrollCallback(windowPtr, MouseEvent::mouseScrollCallbackEvent);
		GLFW.glfwSetCursorPosCallback(windowPtr, MouseEvent::mousePosCallbackEvent);
	}

	/**
	 * Sets vsync for the GLFW window.
	 * Better to keep VSync true to not overload system resources.
	 *
	 * @param enabled true to set VSync
	 */
	private void setVSync(boolean enabled) {
		if (enabled) {
			GLFW.glfwSwapInterval(1);
		} else {
			GLFW.glfwSwapInterval(0);
		}
		WindowData.vSync = enabled;
	}

	/**
	 * Swap buffers and update GLFW poll events
	 */
	@Override
	public void update() {
		GLFW.glfwPollEvents();
		context.swapBuffers();
	}

	/**
	 * Return GLFW window pointer
	 *
	 * @return GLFW window pointer
	 */
	@Override
	public long getNativeWindow() {
		return windowPtr;
	}

	/**
	 * Returns windows width in int
	 *
	 * @return window width in int
	 */
	@Override
	public int getWidth() {
		return WindowData.width;
	}

	/**
	 * Returns windows height in int
	 *
	 * @return window height in int
	 */
	@Override
	public int getHeight() {
		return WindowData.height;
	}

	/**
	 * Window properties data. Title, width, height, vsync
	 */
	public static class WindowData {
		public static String title;
		public static int width;
		public static int height;
		public static boolean vSync;
		private static Queue<Event> eventQueue;

		private WindowData() {
		}

		public static <T> void eventCallback(T event) {
			eventQueue.add((Event) event);
		}
	}

}