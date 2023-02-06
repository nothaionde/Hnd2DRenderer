package hnd.src.events;


import hnd.src.core.Application;
import org.lwjgl.glfw.GLFW;

/**
 * Keyboard input event handler
 */
public class KeyEvent extends Event {

	protected int keycode;

	public KeyEvent(int keycode) {
		this.keycode = keycode;
	}

	public static void keyCallbackEvent(long windowPtr, int key, int scancode, int action, int mods) {
		switch (action) {
			case GLFW.GLFW_PRESS -> {
				Application.getInstance().onEvent(new KeyPressedEvent(key, false));
			}
			case GLFW.GLFW_RELEASE -> {
				Application.getInstance().onEvent(new KeyReleasedEvent(key));
			}
		}
	}

	public int getKeycode() {
		return keycode;
	}

	public static class KeyPressedEvent extends KeyEvent {
		public boolean isRepeat = false;

		public KeyPressedEvent(int keycode, boolean isRepeat) {
			super(keycode);
			this.isRepeat = isRepeat;
		}
	}

	public static class KeyReleasedEvent extends KeyEvent {

		public KeyReleasedEvent(int keycode) {
			super(keycode);
		}
	}
}
