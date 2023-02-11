package hndeditor.src;

import hnd.src.core.Application;
import hnd.src.core.Layer;
import hnd.src.events.Event;
import hnd.src.imgui.ImGuiLayer;
import hnd.src.platform.windows.WindowsPlatformUtils;
import hnd.src.renderer.RenderCommand;
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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public class EditorLayer extends Layer {
	int[] vertexArray = {0};
	int[] vertexBuffer = {0};
	int[] indexBuffer = {0};
	private Framebuffer framebuffer;
	private Scene activeScene;
	private Scene editorScene;
	private SceneHierarchyPanel sceneHierarchyPanel;
	private ContentBrowserPanel contentBrowserPanel;
	private SceneRenderer sceneRenderer;
	private Vector4f clearColor = new Vector4f(0.1f, 0.1f, 0.1f, 1);
	private ImVec2 viewportSize;
	private ImVec2[] viewportBounds;
	private String editorScenePath;

	@Override
	public void onAttach() {
		// Vertex Array
		GL30.glGenVertexArrays(vertexArray);
		GL30.glBindVertexArray(vertexArray[0]);
		// Vertex Buffer
		GL15.glGenBuffers(vertexBuffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer[0]);

		float[] vertices = {
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.0f, 0.5f, 0.0f,
		};
		// Upload data to GPU
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Float.BYTES * 3, 0);

		// Index Buffer
		GL15.glGenBuffers(indexBuffer);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer[0]);

		int[] indices = {
				0, 1, 2
		};
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

		FramebufferSpecification framebufferSpecification = new FramebufferSpecification();
		framebufferSpecification.width = 1280;
		framebufferSpecification.height = 720;
		framebuffer = Framebuffer.create(framebufferSpecification);


		editorScene = new Scene();
		activeScene = editorScene;

		sceneHierarchyPanel = new SceneHierarchyPanel();
		contentBrowserPanel = new ContentBrowserPanel();
		sceneRenderer = new SceneRenderer();
		viewportSize = new ImVec2();
		viewportBounds = new ImVec2[]{new ImVec2(), new ImVec2()};
	}

	@Override
	public void onDetach() {

	}

	@Override
	public void onUpdate(float ts) {
		framebuffer.bind();
		RenderCommand.setClearColor(clearColor);
		RenderCommand.clear();

		GL30.glBindVertexArray(vertexArray[0]);
		GL11.glDrawElements(GL11.GL_TRIANGLES, 3, GL11.GL_UNSIGNED_INT, 0);
		framebuffer.unbind();
	}

	@Override
	public void onImGuiRender() {
		// Note: Switch this to true to enable dockspace
		ImBoolean dockspaceOpen = new ImBoolean(true);
		boolean optFullscreen = true;
		int dockspaceFlags = ImGuiDockNodeFlags.None;
		// Using the ImGuiWindowFlags.NoDocking flag to make the parent window not dockable into
		int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
		if (optFullscreen) {
			ImGuiViewport viewport = ImGui.getMainViewport();
			ImGui.setNextWindowPos(viewport.getPos().x, viewport.getPos().y);
			ImGui.setNextWindowSize(viewport.getSize().x, viewport.getSize().y);
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
		style.setWindowMinSize(370.0f, 1);
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
				if (ImGui.menuItem("Scene", "Ctrl+N")) {
					newScene();
				}

				if (ImGui.menuItem("Scene", "Ctrl+S")) {
					saveScene();
				}

				if (ImGui.menuItem("Scene As...", "Ctrl+Shift+S")) {
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
		viewportSize = ImGui.getContentRegionAvail();
		int textureID = framebuffer.getTextureID();
		ImGui.image(textureID, viewportSize.x, viewportSize.y, 0, 1, 1, 0);
		ImGui.end();
		ImGui.end();
	}

	private void onOverlayRender() {

	}

	private void saveSceneAs() {
		// TODO: implement open project after serializing scene
	}

	private void saveScene() {
		// TODO: implement open project after serializing scene
	}

	private void newScene() {
		activeScene = new Scene();
		sceneHierarchyPanel.setContext(activeScene);

		editorScenePath = ""; // TODO set path after serializing
	}

	private void openProject() {
		String filepath = WindowsPlatformUtils.openFile(".hndproj");
		if (filepath == null)
			return;

		openProject(filepath);
	}

	private void openProject(String filepath) {
		// TODO: implement open project after serializing scene
	}

	@Override
	public void onEvent(Event event) {

	}
}
