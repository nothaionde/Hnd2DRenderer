package hndeditor.src;

import hnd.src.core.Application;
import hnd.src.core.Layer;
import hnd.src.events.Event;
import hnd.src.imgui.ImGuiLayer;
import hnd.src.platform.windows.WindowsPlatformUtils;
import hnd.src.renderer.EditorCamera;
import hnd.src.renderer.OrthographicCameraController;
import hnd.src.renderer.RenderCommand;
import hnd.src.renderer.Renderer2D;
import hnd.src.renderer.framebuffer.Framebuffer;
import hnd.src.renderer.framebuffer.FramebufferSpecification;
import hnd.src.scene.Scene;
import hndeditor.src.panels.ContentBrowserPanel;
import hndeditor.src.panels.SceneHierarchyPanel;
import hndeditor.src.panels.SceneRenderer;
import imgui.*;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.joml.Vector4f;

/**
 * The EditorLayer class is responsible for handling the main editor functionality.
 * It inherits from the Layer class and contains fields for the editor's framebuffer,
 * camera controller, active scene, editor scene, scene hierarchy panel, content browser panel,
 * scene renderer, viewport size, viewport bounds, editor scene path, and viewport focus.
 */
public class EditorLayer extends Layer {
	/**
	 * The color used to clear the editor's framebuffer.
	 */
	private final Vector4f clearColor = new Vector4f(0.1f, 0.1f, 0.1f, 1);
	/**
	 * The camera controller used by the editor.
	 */
	private final OrthographicCameraController cameraController;
	/**
	 * The framebuffer used by the editor.
	 */
	private Framebuffer framebuffer;
	/**
	 * The camera used by the editor.
	 */
	private EditorCamera editorCamera;
	/**
	 * The currently active scene.
	 */
	private Scene activeScene;

	/**
	 * The scene used by the editor.
	 */
	private Scene editorScene;

	/**
	 * The panel used to display the scene hierarchy.
	 */
	private SceneHierarchyPanel sceneHierarchyPanel;

	/**
	 * The panel used to browse content.
	 */
	private ContentBrowserPanel contentBrowserPanel;

	/**
	 * The renderer used to display the scene.
	 */
	private SceneRenderer sceneRenderer;

	/**
	 * The size of the editor's viewport.
	 */
	private ImVec2 viewportSize;

	/**
	 * The bounds of the editor's viewport.
	 */
	private ImVec2[] viewportBounds;

	/**
	 * The path of the editor's scene.
	 */
	private String editorScenePath;

	/**
	 * A boolean indicating whether the editor's viewport is focused.
	 */
	private boolean viewportFocused;

	/**
	 * Creates a new instance of the EditorLayer class and initializes the camera controller.
	 */
	public EditorLayer() {
		cameraController = new OrthographicCameraController(1280.0f / 720.0f);
	}

	/**
	 * Initializes the editor's framebuffer, scene, camera, and panels.
	 */
	@Override
	public void onAttach() {
		FramebufferSpecification framebufferSpecification = new FramebufferSpecification();
		framebufferSpecification.width = 1280;
		framebufferSpecification.height = 720;
		framebuffer = Framebuffer.create(framebufferSpecification);

		editorScene = new Scene();
		activeScene = editorScene;

		editorCamera = new EditorCamera(30.0f, 1.778f, 0.1f, 1000.0f);

		sceneHierarchyPanel = new SceneHierarchyPanel();
		contentBrowserPanel = new ContentBrowserPanel();
		sceneRenderer = new SceneRenderer();
		viewportSize = new ImVec2();
		viewportBounds = new ImVec2[]{new ImVec2(), new ImVec2()};
	}

	/**
	 * Detaches the editor layer.
	 */
	@Override
	public void onDetach() {

	}

	/**
	 * Updates the editor layer on every frame.
	 * It resizes the framebuffer if necessary, clears the screen and renders the active scene.
	 * It also updates the camera and scene on every frame.
	 *
	 * @param ts the time since the last frame
	 */
	@Override
	public void onUpdate(float ts) {
		// Resize
		if (viewportSize.x > 0.0f && viewportSize.y > 0.0f && // zero framebuffer is invalid
				(framebuffer.getSpecification().width != viewportSize.x || framebuffer.getSpecification().height != viewportSize.y)) {
			framebuffer.resize((int) viewportSize.x, (int) viewportSize.y);
			cameraController.onResize(viewportSize.x, viewportSize.y);
			editorCamera.setViewportSize(viewportSize.x, viewportSize.y);
		}
		Renderer2D.resetStats();
		framebuffer.bind();
		RenderCommand.setClearColor(clearColor);
		RenderCommand.clear();
		if (viewportFocused) {
			cameraController.onUpdate(ts);
		}
		editorCamera.onUpdate(ts);
		activeScene.onUpdateEditor(editorCamera);

		framebuffer.unbind();
	}

	/**
	 * Renders the ImGui user interface for the editor.
	 */
	@Override
	public void onImGuiRender() {
		if (sceneRenderer.getShowDemoWindow().get()) {
			ImGui.showDemoWindow();
		}
		// Note: Switch this to true to enable dockspace
		ImBoolean dockspaceOpen = new ImBoolean(true);
		boolean optFullscreen = true;
		int dockspaceFlags = ImGuiDockNodeFlags.None;
		// Using the ImGuiWindowFlags.NoDocking flag to make the parent window not dockable into
		int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
		if (optFullscreen) {
			ImGuiViewport viewport = ImGui.getMainViewport();
			ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
			ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
			ImGui.setNextWindowViewport(viewport.getID());
			ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
			ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
			windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize
					| ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
		}
		// When using ImGuiDockNodeFlags.PassthruCentralNode, DockSpace() will render our background and handle the pass-thru hole, so we ask Begin() to not render a background.
		if ((dockspaceFlags & ImGuiDockNodeFlags.PassthruCentralNode) != 0) {
			windowFlags |= ImGuiWindowFlags.NoBackground;
		}
		// Important: note that we proceed even if Begin() returns false (aka window is collapsed).
		// This is because we want to keep our DockSpace() active. If a DockSpace() is inactive,
		// all active windows docked into it will lose their parent and become undocked.
		// We cannot preserve the docking relationship between an active window and an inactive docking, otherwise
		// any change of dockspace/settings would lead to windows being stuck in limbo and never being visible.
		ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);
		ImGui.begin("Dockspace", dockspaceOpen, windowFlags);
		ImGui.popStyleVar();
		if (optFullscreen) {
			ImGui.popStyleVar(2);
		}
		// Dockspace
		ImGuiIO io = ImGui.getIO();
		ImGuiStyle style = ImGui.getStyle();
		float minWinSizeX = style.getWindowMinSizeX();
		style.setWindowMinSize(100, 100);
		if (io.hasConfigFlags(ImGuiConfigFlags.DockingEnable)) {
			long dockspaceID = ImGui.getID("MyDockspace");
			ImGui.dockSpace((int) dockspaceID, 0.0f, 0.0f, dockspaceFlags);
		}
		style.setWindowMinSize(minWinSizeX, 1);
		if (ImGui.beginMenuBar()) {
			if (ImGui.beginMenu("File")) {
				if (ImGui.menuItem("Open Project...", "Ctrl+O")) {
					openProject();
				}
				ImGui.separator();
				if (ImGui.menuItem("New Scene", "Ctrl+N")) {
					newScene();
				}

				if (ImGui.menuItem("Save Scene", "Ctrl+S")) {
					saveScene();
				}

				if (ImGui.menuItem("Save Scene As...", "Ctrl+Shift+S")) {
					saveSceneAs();
				}
				ImGui.separator();
				if (ImGui.menuItem("Exit")) {
					Application.getInstance().close();
				}
				ImGui.endMenu();
			}
			if (ImGui.beginMenu("Options")) {
				if (ImGui.menuItem("Dark theme")) {
					ImGuiLayer.setColors(ImGuiLayer.Theme.DARK);
				}
				if (ImGui.menuItem("Light theme")) {
					ImGuiLayer.setColors(ImGuiLayer.Theme.LIGHT);
				}

				ImGui.endMenu();
			}
			ImGui.endMenuBar();
		}

		sceneHierarchyPanel.onImGuiRender();
		contentBrowserPanel.onImGuiRender();
		sceneRenderer.onImGuiRender();

		ImGui.begin("Viewport");
		viewportFocused = ImGui.isWindowFocused();
		viewportSize = ImGui.getContentRegionAvail();
		int textureID = framebuffer.getColorAttachmentRendererID();
		ImGui.image(textureID, viewportSize.x, viewportSize.y, 0, 1, 1, 0);
		ImGui.end();
		ImGui.end();
	}

	/**
	 * This method is called when the overlay needs to be rendered.
	 */
	private void onOverlayRender() {

	}

	/**
	 * Saves the scene as a new file.
	 */
	private void saveSceneAs() {
		// TODO: implement open project after serializing scene
	}

	/**
	 * Saves the scene to the current file.
	 */
	private void saveScene() {
		// TODO: implement open project after serializing scene
	}

	/**
	 * Creates a new scene.
	 */
	private void newScene() {
		activeScene = new Scene();
		sceneHierarchyPanel.setContext(activeScene);

		editorScenePath = ""; // TODO set path after serializing
	}

	/**
	 * Opens a project from a file.
	 */
	private void openProject() {
		String filepath = WindowsPlatformUtils.openFile(".hndproj");
		if (filepath.equals("")) {
			return;
		}

		openProject(filepath);
	}

	/**
	 * Opens a project from a file.
	 *
	 * @param filepath The path of the file to open.
	 */
	private void openProject(String filepath) {
		// TODO: implement open project after serializing scene
	}

	/**
	 * Handles events received by the layer.
	 *
	 * @param event The event received.
	 */
	@Override
	public void onEvent(Event event) {

	}
}
