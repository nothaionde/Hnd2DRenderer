package hndeditor.src.panels;

import hnd.src.scene.Scene;
import imgui.ImGui;

public class SceneHierarchyPanel {
	private Scene context;

	public SceneHierarchyPanel() {
	}

	public void setContext(Scene context) {
		this.context = context;
	}

	public void onImGuiRender() {
		ImGui.begin("Scene Hierarchy");

		ImGui.end();

		ImGui.begin("Properties");

		ImGui.end();
	}
}