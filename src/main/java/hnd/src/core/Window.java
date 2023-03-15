package hnd.src.core;


import hnd.src.platform.windows.GLFWWindow;

/**
 * An abstract class representing a window in the graphics engine.
 */
public abstract class Window {

    /**
     * Creates a new window with the given properties.
     *
     * @param windowProps The properties of the window to create.
     * @return A new instance of a Window.
     */
    public static Window create(WindowProps windowProps) {
        return new GLFWWindow(windowProps);
    }

    /**
     * Updates the window, for example by rendering a new frame.
     */
    public abstract void update();

    /**
     * Gets the native window handle of the window.
     *
     * @return The native window handle of the window.
     */
    public abstract long getNativeWindow();

    /**
     * Gets the width of the window.
     *
     * @return The width of the window in pixels.
     */
    public abstract int getWidth();

    /**
     * Gets the height of the window.
     *
     * @return The height of the window in pixels.
     */
    public abstract int getHeight();

    /**
     * Disposes of the window and any associated resources.
     */
    public abstract void dispose();


    /**
     * A class representing the properties of a window.
     */
    public static class WindowProps {
        /**
         * The title of the window.
         */
        public String title;

        /**
         * The default width of the window.
         */
        public int width = 1600;

        /**
         * The default height of the window.
         */
        public int height = 900;

        /**
         * Creates a new instance of WindowProps with the given title.
         *
         * @param title The title of the window.
         */
        public WindowProps(String title) {
            this.title = title;
        }
    }

}