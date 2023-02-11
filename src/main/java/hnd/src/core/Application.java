package hnd.src.core;


import hnd.src.events.ApplicationEvent;
import hnd.src.events.Event;
import hnd.src.imgui.ImGuiLayer;
import hnd.src.renderer.Renderer;
import hnd.src.utils.Constants;
import hndeditor.src.HndEditor;
import hndrelease.src.HndRelease;

/**
 * Main application singleton.
 * Every entry point should extend this class.
 * After initialization need to push new layer.
 * Handles main run, update and window rendering loops.
 */
public abstract class Application {

	/**
	 * Singleton holder
	 */
	private static Application instance;
	private Window window;
	private boolean running = true;
	private ImGuiLayer imGuiLayer;
	private LayerStack layerStack = new LayerStack();
	private float lastFrameTime = 0.0f;
	private boolean minimized = false;

	/**
	 * Creates application instance, window and pushes ImGui layer to initialize starting configuration
	 *
	 * @param args command-line arguments
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
	 * @return application instance
	 */
	public static Application getInstance() {
		return instance;
	}

	public static Application createApplication(String[] args) {
		if (Constants.RELEASE) {
			return HndRelease.createApplication(args);
		} else {
			return HndEditor.createApplication(args);
		}
	}

	public Window getWindow() {
		return window;
	}

	public void pushLayer(Layer layer) {
		layerStack.pushLayer(layer);
		layer.onAttach();
	}

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

	protected boolean onWindowResize(ApplicationEvent.WindowResizeEvent e) {
		if (e.getWidth() == 0 || e.getHeight() == 0) {
			minimized = true;
			return false;
		}
		minimized = false;
		Renderer.onWindowResize(e.getWidth(), e.getHeight());
		return false;
	}

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

	public void close() {
		running = false;
	}

	public ImGuiLayer getImGuiLayer() {
		return imGuiLayer;
	}


}
