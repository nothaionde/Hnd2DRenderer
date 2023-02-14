package hnd.src.events;

import hnd.src.core.Application;
import hnd.src.platform.windows.GLFWWindow;

/**
 * Application event handler
 */
public class ApplicationEvent extends Event {
	public static void windowSizeCallback(long windowPtr, int width, int height) {
		GLFWWindow.WindowData.width = width;
		GLFWWindow.WindowData.height = height;
		WindowResizeEvent event = new WindowResizeEvent(width, height);
		Application.getInstance().onEvent(event);
	}

	public static void windowCloseEvent(long windowPtr) {
		Application.getInstance().close();
	}

	public static class WindowResizeEvent extends Event {
		private int width;
		private int height;

		public WindowResizeEvent(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}
}
