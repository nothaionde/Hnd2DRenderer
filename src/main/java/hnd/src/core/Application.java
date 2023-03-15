package hnd.src.core;


import hnd.src.events.ApplicationEvent;
import hnd.src.events.Event;
import hnd.src.imgui.ImGuiLayer;
import hnd.src.renderer.Renderer;
import hndeditor.src.HndEditor;

/**
 * The base class for all Hnd applications. To create a custom application, you must extend this class.
 */
public abstract class Application {
    /**
     * The singleton instance of the application.
     */
    private static Application instance;
    /**
     * The window for the application.
     */
    private final Window window;
    /**
     * The application is running.
     */
    private boolean running = true;
    /**
     * The ImGuiLayer for the application.
     */
    private final ImGuiLayer imGuiLayer;
    /**
     * The LayerStack for the application.
     */
    private final LayerStack layerStack = new LayerStack();
    /**
     * The time of the last frame.
     */
    private float lastFrameTime = 0.0f;
    /**
     * The window is minimized.
     */
    private boolean minimized = false;

    /**
     * Creates a new instance of the Application.
     *
     * @param args The command line arguments passed to the application.
     */
    protected Application(String[] args) {
        if (instance == null) {
            instance = this;
        } else {
            Logger.error("Application already exists!");
        }
        window = Window.create(new Window.WindowProps("Hnd"));
        Renderer.init();

        imGuiLayer = new ImGuiLayer();
        pushLayer(imGuiLayer);
    }

    /**
     * Gets the singleton instance of the Application.
     *
     * @return The singleton instance of the Application.
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * Creates a new Application instance.
     *
     * @param args The command line arguments passed to the application.
     * @return A new Application instance.
     */
    public static Application createApplication(String[] args) {
        return HndEditor.createApplication(args);
    }

    /**
     * Gets the window for the application.
     *
     * @return The window for the application.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Pushes a new layer onto the LayerStack.
     *
     * @param layer The layer to push.
     */
    public void pushLayer(Layer layer) {
        layerStack.pushLayer(layer);
        layer.onAttach();
    }

    /**
     * Handles an event for the application.
     *
     * @param e The event to handle.
     */
    public void onEvent(Event e) {
        if (e instanceof ApplicationEvent.WindowResizeEvent windowResizeEvent) {
            onWindowResize(windowResizeEvent);
        }

        for (int i = layerStack.getLayers().size() - 1; i > 0; i--) {
            if (e.handled) {
                break;
            }
            layerStack.getLayers().get(i).onEvent(e);
        }
    }

    /**
     * Called when the window is resized.
     *
     * @param e The WindowResizeEvent containing the new width and height.
     * @return Always false.
     */
    protected boolean onWindowResize(ApplicationEvent.WindowResizeEvent e) {
        if (e.getWidth() == 0 || e.getHeight() == 0) {
            minimized = true;
            return false;
        }
        minimized = false;
        Renderer.onWindowResize(e.getWidth(), e.getHeight());
        return false;
    }

    /**
     * Runs the application.
     * <p>
     * This method runs a while loop while the application is running. During each loop, it calculates the time since
     * the last frame, updates each layer in the {@link #layerStack}, renders the ImGuiLayer, and updates the window.
     * If the window is minimized, it skips updating the layers and rendering the ImGuiLayer.
     */
    public void run() {
        float timestep;

        while (running) {
            float time = Timestep.getTime();
            timestep = time - lastFrameTime;
            lastFrameTime = time;

            if (!minimized) {
                for (Layer layer : layerStack.getLayers()) {
                    layer.onUpdate(timestep);
                }

                imGuiLayer.begin();
                for (Layer layer : layerStack.getLayers()) {
                    layer.onImGuiRender();
                }
                imGuiLayer.end();
            }
            window.update();
        }
    }

    /**
     * Closes the application.
     * <p>
     * This method sets the {@link #running} flag to false and disposes of the {@link #window}.
     */
    public void close() {
        running = false;
        window.dispose();
    }

    /**
     * Gets the ImGuiLayer for the application.
     *
     * @return The ImGuiLayer for the application.
     */
    public ImGuiLayer getImGuiLayer() {
        return imGuiLayer;
    }
}
