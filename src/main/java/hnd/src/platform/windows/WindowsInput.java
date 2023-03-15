package hnd.src.platform.windows;

import hnd.src.core.Application;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

/**
 * Provides input-related functionality specific to the Windows platform.
 */
public class WindowsInput {

    /**
     * Checks whether a keyboard key is currently pressed.
     *
     * @param key the key code to check
     * @return true if the key is currently pressed, false otherwise
     */
    public static boolean isKeyPressed(int key) {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        return GLFW.glfwGetKey(windowPtr, key) == GLFW.GLFW_PRESS;
    }

    /**
     * Checks whether a mouse button is currently pressed.
     *
     * @param button the mouse button code to check
     * @return true if the button is currently pressed, false otherwise
     */
    public static boolean isMouseButtonPressed(int button) {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        return GLFW.glfwGetMouseButton(windowPtr, button) == GLFW.GLFW_PRESS;
    }

    /**
     * Gets the current position of the mouse cursor.
     *
     * @return a Vector2f containing the x and y coordinates of the mouse cursor
     */
    public static Vector2f getMousePosition() {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        double[] xPos = {0};
        double[] yPos = {0};
        GLFW.glfwGetCursorPos(windowPtr, xPos, yPos);
        return new Vector2f((float) xPos[0], (float) yPos[0]);
    }

    /**
     * Gets the current x coordinate of the mouse cursor.
     *
     * @return the x coordinate of the mouse cursor
     */
    public static float getMouseX() {
        return getMousePosition().x;
    }

    /**
     * Gets the current y coordinate of the mouse cursor.
     *
     * @return the y coordinate of the mouse cursor
     */
    public static float getMouseY() {
        return getMousePosition().y;
    }

}
