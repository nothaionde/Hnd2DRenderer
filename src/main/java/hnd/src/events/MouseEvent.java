package hnd.src.events;

import org.lwjgl.glfw.GLFW;

/**
 * Represents a mouse event, which is a type of {@link Event}.
 */
public class MouseEvent extends Event {
    /**
     * The singleton instance of the MouseEvent class.
     */
    private static final MouseEvent instance = new MouseEvent();

    /**
     * An array that keeps track of whether a mouse button is currently pressed or not.
     */
    private final boolean[] mouseButtonPressed = new boolean[10];

    /**
     * The x-offset of the mouse scroll.
     */
    private double xOffset;

    /**
     * The y-offset of the mouse scroll.
     */
    private double yOffset;

    /**
     * The x-coordinate of the mouse position.
     */
    private double mouseX;

    /**
     * The y-coordinate of the mouse position.
     */
    private double mouseY;

    /**
     * This method is called when a mouse button is pressed or released.
     *
     * @param windowPtr The pointer to the window where the event occurred.
     * @param button    The button that was pressed or released.
     * @param action    The action that was performed (GLFW_PRESS or GLFW_RELEASE).
     * @param mods      The modifier keys that were held down.
     */
    public static void mouseButtonCallbackEvent(long windowPtr, int button, int action, int mods) {
        switch (action) {
            case GLFW.GLFW_PRESS -> getInstance().mouseButtonPressed[button] = false;
            case GLFW.GLFW_RELEASE -> getInstance().mouseButtonPressed[button] = true;
        }
    }

    /**
     * Returns the singleton instance of the MouseEvent class.
     *
     * @return The MouseEvent instance.
     */
    private static MouseEvent getInstance() {
        return instance;
    }

    /**
     * This method is called when the mouse wheel is scrolled.
     *
     * @param windowPtr The pointer to the window where the event occurred.
     * @param xOffset   The x-offset of the mouse scroll.
     * @param yOffset   The y-offset of the mouse scroll.
     */
    public static void mouseScrollCallbackEvent(long windowPtr, double xOffset, double yOffset) {
        getInstance().xOffset = xOffset;
        getInstance().yOffset = yOffset;
    }

    /**
     * This method is called when the mouse is moved.
     *
     * @param windowPtr The pointer to the window where the event occurred.
     * @param xPos      The x-coordinate of the mouse position.
     * @param yPos      The y-coordinate of the mouse position.
     */
    public static void mousePosCallbackEvent(long windowPtr, double xPos, double yPos) {
        getInstance().mouseX = xPos;
        getInstance().mouseY = yPos;
    }
}