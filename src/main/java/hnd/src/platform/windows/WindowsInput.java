package hnd.src.platform.windows;

import hnd.src.core.Application;
import org.lwjgl.glfw.GLFW;

public class WindowsInput {
	public static boolean isKeyPressed(int key) {
		long windowPtr = Application.getInstance().getWindow().getNativeWindow();
		return GLFW.glfwGetKey(windowPtr, key) == GLFW.GLFW_PRESS;
	}
}
