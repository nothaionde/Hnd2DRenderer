package hnd.src.core;


import hnd.src.platform.GLFWWindow;

/**
 * Abstract class that contains main window creation and update logic.
 */
public abstract class Window {

	public static Window create(WindowProps windowProps) {
		return new GLFWWindow(windowProps);
	}

	public abstract void update();

	public abstract long getNativeWindow();

	public abstract int getWidth();

	public abstract int getHeight();


	public static class WindowProps {
		public String title;
		public int width = 1600;
		public int height = 900;

		public WindowProps(String title) {
			this.title = title;
		}
	}

}