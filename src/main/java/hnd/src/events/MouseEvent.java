package hnd.src.events;

import org.lwjgl.glfw.GLFW;

/**
 * Mouse input event handler
 */
public class MouseEvent extends Event {

	private static final MouseEvent instance = new MouseEvent();
	private boolean[] mouseButtonPressed = new boolean[10];
	private double xOffset;
	private double yOffset;
	private double mouseX;
	private double mouseY;

	public static void mouseButtonCallbackEvent(long windowPtr, int button, int action, int mods) {
		switch (action) {
			case GLFW.GLFW_PRESS -> getInstance().mouseButtonPressed[button] = false;
			case GLFW.GLFW_RELEASE -> getInstance().mouseButtonPressed[button] = true;
		}
	}


	private static MouseEvent getInstance() {
		return instance;
	}

	public static void mouseScrollCallbackEvent(long windowPtr, double xOffset, double yOffset) {
		getInstance().xOffset = xOffset;
		getInstance().yOffset = yOffset;
	}

	public static void mousePosCallbackEvent(long windowPtr, double xPos, double yPos) {
		getInstance().mouseX = xPos;
		getInstance().mouseY = yPos;
	}
}
