package hnd.src.platform.windows;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/**
 * Windows platform related functions
 */
public class WindowsPlatformUtils {

	/**
	 * Returns the value of the GLFW timer. The timer measures time elapsed since GLFW was initialized.
	 *
	 * @return the current value, in seconds, or zero if an error occurred
	 */

	public static float getTime() {
		return (float) GLFW.glfwGetTime();
	}

	public static String openFile(String filter) {
		String s = TinyFileDialogs.tinyfd_openFileDialog("Open File", null, null, filter, false);
		return s == null ? "" : s;
	}

	public static String saveFile(String filter) {
		String s = TinyFileDialogs.tinyfd_saveFileDialog("Save File", null, null, null);
		return s == null ? "" : s;
	}
}
