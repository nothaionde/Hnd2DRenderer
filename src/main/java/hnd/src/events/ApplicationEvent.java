package hnd.src.events;

import hnd.src.core.Application;
import hnd.src.platform.windows.GLFWWindow;

/**
 * This class represents events related to the application, such as window resizing and closing.
 */
public class ApplicationEvent extends Event {

    /**
     * This method is called when the window is resized.
     *
     * @param windowPtr the pointer to the GLFW window
     * @param width     the new width of the window
     * @param height    the new height of the window
     */
    public static void windowSizeCallback(long windowPtr, int width, int height) {
        GLFWWindow.WindowData.width = width;
        GLFWWindow.WindowData.height = height;
        WindowResizeEvent event = new WindowResizeEvent(width, height);
        Application.getInstance().onEvent(event);
    }

    /**
     * This method is called when the window is closed.
     * It will close the application.
     *
     * @param windowPtr the pointer to the GLFW window
     */
    public static void windowCloseEvent(long windowPtr) {
        Application.getInstance().close();
    }

    /**
     * This class represents a window resize event.
     */
    public static class WindowResizeEvent extends Event {
        private int width;
        private int height;

        /**
         * Creates a new window resize event with the specified width and height.
         *
         * @param width  the new width of the window
         * @param height the new height of the window
         */
        public WindowResizeEvent(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Gets the new width of the window.
         *
         * @return the new width of the window
         */
        public int getWidth() {
            return width;
        }

        /**
         * Gets the new height of the window.
         *
         * @return the new height of the window
         */
        public int getHeight() {
            return height;
        }
    }
}