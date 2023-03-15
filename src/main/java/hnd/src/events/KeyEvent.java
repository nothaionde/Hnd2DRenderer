package hnd.src.events;


import hnd.src.core.Application;
import org.lwjgl.glfw.GLFW;

/**
 * The KeyEvent class is an abstract base class for all events related to a key being pressed or released.
 */
public abstract class KeyEvent extends Event {

    /**
     * The keycode for the key involved in the event.
     */
    protected int keycode;

    /**
     * Constructs a KeyEvent object with the given keycode.
     *
     * @param keycode the keycode for the key involved in the event
     */
    public KeyEvent(int keycode) {
        this.keycode = keycode;
    }

    /**
     * This static method is called by GLFW when a key event occurs. It creates a KeyEvent object and passes it to the
     * Application's onEvent() method.
     *
     * @param windowPtr the GLFW window pointer
     * @param key       the GLFW keycode
     * @param scancode  the GLFW scancode
     * @param action    the GLFW action (GLFW_PRESS, GLFW_RELEASE, or GLFW_REPEAT)
     * @param mods      the GLFW modifier bits
     */
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

    /**
     * Returns the keycode for the key involved in the event.
     *
     * @return the keycode for the key involved in the event
     */
    public int getKeycode() {
        return keycode;
    }

    /**
     * The KeyPressedEvent class represents a key pressed event.
     */
    public static class KeyPressedEvent extends KeyEvent {

        /**
         * Whether the key press is a repeat.
         */
        public boolean isRepeat = false;

        /**
         * Constructs a KeyPressedEvent object with the given keycode and repeat flag.
         *
         * @param keycode  the keycode for the key involved in the event
         * @param isRepeat true if the key press is a repeat; false otherwise
         */
        public KeyPressedEvent(int keycode, boolean isRepeat) {
            super(keycode);
            this.isRepeat = isRepeat;
        }
    }

    /**
     * The KeyReleasedEvent class represents a key released event.
     */
    public static class KeyReleasedEvent extends KeyEvent {

        /**
         * Constructs a KeyReleasedEvent object with the given keycode.
         *
         * @param keycode the keycode for the key involved in the event
         */
        public KeyReleasedEvent(int keycode) {
            super(keycode);
        }
    }
}