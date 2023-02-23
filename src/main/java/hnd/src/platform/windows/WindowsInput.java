package hnd.src.platform.windows;

import hnd.src.core.Application;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class WindowsInput {
    public static boolean isKeyPressed(int key) {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        return GLFW.glfwGetKey(windowPtr, key) == GLFW.GLFW_PRESS;
    }

    public static boolean isMouseButtonPressed(int button) {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        return GLFW.glfwGetMouseButton(windowPtr, button) == GLFW.GLFW_PRESS;
    }

    public static Vector2f getMousePosition() {
        long windowPtr = Application.getInstance().getWindow().getNativeWindow();
        double[] xPos = {0};
        double[] yPos = {0};
        GLFW.glfwGetCursorPos(windowPtr, xPos, yPos);
        return new Vector2f((float) xPos[0], (float) yPos[0]);
    }

    public static float getMouseX() {
        return getMousePosition().x;
    }


    public static float getMouseY() {
        return getMousePosition().y;
    }

}
