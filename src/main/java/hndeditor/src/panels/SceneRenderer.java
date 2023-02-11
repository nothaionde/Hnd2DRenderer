package hndeditor.src.panels;

import hnd.src.renderer.Renderer2D;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class SceneRenderer {

	private ImBoolean showPhysicsColliders = new ImBoolean(false);

	public SceneRenderer() {
	}

	public void onImGuiRender() {
		ImGui.begin("Renderer2D Stats:");
		ImGui.text("Renderer2D Stats:");
		ImGui.text("Draw Calls: " + Renderer2D.Statistics.drawCalls);
		ImGui.text("Quads: " + Renderer2D.Statistics.quadCount);
		ImGui.text("Vertices: " + Renderer2D.Statistics.getTotalVertexCount());
		ImGui.text("Indices: " + Renderer2D.Statistics.getTotalIndexCount());

		ImGui.end();

		ImGui.begin("Settings");
		ImGui.checkbox("Show physics colliders", showPhysicsColliders);
		ImGui.end();

	}
}
