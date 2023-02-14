package hnd.src.imgui;


import hnd.src.core.Application;
import hnd.src.core.Layer;
import hnd.src.events.Event;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

/**
 * Layer with ImGui initialization and configuration
 */
public class ImGuiLayer extends Layer {

	private final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
	private final ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

	/**
	 * Sets up color theme for ImGui
	 */
	public static void setColors(Theme theme) {
		switch (theme) {
			case DARK -> {
				ImGui.styleColorsDark();
				ImGuiStyle style = ImGui.getStyle();
				style.setColor(ImGuiCol.WindowBg, 0.1f, 0.105f, 0.11f, 1.0f);
				// Headers
				style.setColor(ImGuiCol.Header, 0.2f, 0.205f, 0.21f, 1.0f);
				style.setColor(ImGuiCol.HeaderHovered, 0.3f, 0.305f, 0.31f, 1.0f);
				style.setColor(ImGuiCol.HeaderActive, 0.15f, 0.1505f, 0.151f, 1.0f);
				// Buttons
				style.setColor(ImGuiCol.Button, 0.2f, 0.205f, 0.21f, 1.0f);
				style.setColor(ImGuiCol.ButtonHovered, 0.3f, 0.305f, 0.31f, 1.0f);
				style.setColor(ImGuiCol.ButtonActive, 0.15f, 0.1505f, 0.151f, 1.0f);
				// frame BG
				style.setColor(ImGuiCol.FrameBg, 0.2f, 0.205f, 0.21f, 1.0f);
				style.setColor(ImGuiCol.FrameBgHovered, 0.3f, 0.305f, 0.31f, 1.0f);
				style.setColor(ImGuiCol.FrameBgActive, 0.15f, 0.1505f, 0.151f, 1.0f);
				// Tabs
				style.setColor(ImGuiCol.Tab, 0.15f, 0.1505f, 0.151f, 1.0f);
				style.setColor(ImGuiCol.TabHovered, 0.38f, 0.3805f, 0.381f, 1.0f);
				style.setColor(ImGuiCol.TabActive, 0.28f, 0.2805f, 0.281f, 1.0f);
				style.setColor(ImGuiCol.TabUnfocused, 0.15f, 0.1505f, 0.151f, 1.0f);
				style.setColor(ImGuiCol.TabUnfocusedActive, 0.2f, 0.205f, 0.21f, 1.0f);
				// Title
				style.setColor(ImGuiCol.TitleBg, 0.15f, 0.1505f, 0.151f, 1.0f);
				style.setColor(ImGuiCol.TitleBgActive, 0.15f, 0.1505f, 0.151f, 1.0f);
				style.setColor(ImGuiCol.TitleBgCollapsed, 0.15f, 0.1505f, 0.151f, 1.0f);
			}
			case LIGHT -> {
				ImGui.styleColorsLight();
				ImGuiStyle style = ImGui.getStyle();
				style.setColor(ImGuiCol.WindowBg, 0.94f, 0.94f, 0.94f, 0.90f);
				// Headers
				style.setColor(ImGuiCol.Header, 0.26f, 0.59f, 0.98f, 1.00f);
				style.setColor(ImGuiCol.HeaderHovered, 0.26f, 0.59f, 0.98f, 0.60f);
				style.setColor(ImGuiCol.HeaderActive, 0.26f, 0.59f, 0.98f, 1.00f);
				// Buttons
				style.setColor(ImGuiCol.Button, 0.26f, 0.59f, 0.98f, 1.00f);
				style.setColor(ImGuiCol.ButtonHovered, 0.26f, 0.59f, 0.98f, 1.00f);
				style.setColor(ImGuiCol.ButtonActive, 0.06f, 0.53f, 0.98f, 1.00f);
				// frame BG
				style.setColor(ImGuiCol.FrameBg, 1.00f, 1.00f, 1.00f, 1.00f);
				style.setColor(ImGuiCol.FrameBgHovered, 0.26f, 0.59f, 0.98f, 1.00f);
				style.setColor(ImGuiCol.FrameBgActive, 0.26f, 0.59f, 0.98f, 1.00f);
				// Tabs
				style.setColor(ImGuiCol.Tab, 0.76f, 0.80f, 0.84f, 1.00f);
				style.setColor(ImGuiCol.TabHovered, 0.26f, 0.59f, 0.98f, 1.00f);
				style.setColor(ImGuiCol.TabActive, 0.60f, 0.73f, 0.88f, 1.00f);
				style.setColor(ImGuiCol.TabUnfocused, 0.92f, 0.93f, 0.94f, 1.00f);
				style.setColor(ImGuiCol.TabUnfocusedActive, 0.74f, 0.82f, 0.91f, 1.00f);
				// Title
				style.setColor(ImGuiCol.TitleBg, 0.96f, 0.96f, 0.96f, 1.00f);
				style.setColor(ImGuiCol.TitleBgActive, 0.82f, 0.82f, 0.82f, 1.00f);
				style.setColor(ImGuiCol.TitleBgCollapsed, 1.00f, 1.00f, 1.00f, 1.00f);
			}
		}
	}

	/**
	 * Updates ImGuiIO and org.lwjgl.glfw.GLFW state
	 * Start a new Dear ImGui frame, you can submit any command from this point until Render()/EndFrame().
	 * Starts the next frame for the Gizmo. Call this after you've called ImGui.beginFrame().
	 */
	public void begin() {
		imGuiImplGlfw.newFrame();
		ImGui.newFrame();
	}

	/**
	 * Ends the Dear ImGui frame, finalize the draw data.
	 */
	public void end() {
		ImGuiIO io = ImGui.getIO();
		Application application = Application.getInstance();
		io.setDisplaySize(application.getWindow().getWidth(), application.getWindow().getHeight());

		// Rendering
		ImGui.render();
		imGuiImplGl3.renderDrawData(ImGui.getDrawData());

		if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			long backupCurrentContext = GLFW.glfwGetCurrentContext();
			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();
			GLFW.glfwMakeContextCurrent(backupCurrentContext);
		}
	}

	@Override
	public void onAttach() {
		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);       // Enable Keyboard Controls
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);           // Enable Docking
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);         // Enable Multi-Viewport / Platform Windows

		if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			ImGuiStyle style = ImGui.getStyle();
			style.setWindowRounding(0.0f);
			float[][] colors = style.getColors();
			colors[ImGuiCol.WindowBg][3] = 1.0f;
			style.setColors(colors);
		}

		setColors(Theme.DARK);

		Application application = Application.getInstance();
		long windowPtr = application.getWindow().getNativeWindow();

		imGuiImplGlfw.init(windowPtr, true);
		imGuiImplGl3.init("#version 450");
	}

	@Override
	public void onDetach() {
		imGuiImplGl3.dispose();
		imGuiImplGlfw.dispose();
		ImGui.destroyContext();
	}

	@Override
	public void onUpdate(float ts) {
		// Do not need to update
	}

	@Override
	public void onImGuiRender() {
		// Do not need to update
	}

	@Override
	public void onEvent(Event e) {
		// Do not need to update
	}

	public enum Theme {
		DARK,
		LIGHT
	}
}