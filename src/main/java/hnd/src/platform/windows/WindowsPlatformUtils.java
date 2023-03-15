package hnd.src.platform.windows;


import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

/**
 * This class provides utility methods for interacting with the Windows platform.
 */
public class WindowsPlatformUtils {

    /**
     * Returns the time in seconds since the GLFW library was initialized.
     *
     * @return the time in seconds since the GLFW library was initialized
     */
    public static float getTime() {
        return (float) GLFW.glfwGetTime();
    }

    /**
     * Displays an "Open File" dialog and returns the path to the selected file.
     *
     * @param filter a string specifying the file filter to use (e.g. "*.txt")
     * @return the path to the selected file, or an empty string if no file was selected
     */
    public static String openFile(String filter) {
        String s = TinyFileDialogs.tinyfd_openFileDialog("Open File", null, null, filter, false);
        return s == null ? "" : s;
    }

    /**
     * Displays a "Save File" dialog and returns the path to the selected file.
     *
     * @param filter a string specifying the file filter to use (e.g. "*.txt")
     * @return the path to the selected file, or an empty string if no file was selected
     */
    public static String saveFile(String filter) {
        String s = TinyFileDialogs.tinyfd_saveFileDialog("Save File", null, null, filter);
        return s == null ? "" : s;
    }
}