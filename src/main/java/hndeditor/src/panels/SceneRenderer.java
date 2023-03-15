package hndeditor.src.panels;

import hnd.src.renderer.Renderer2D;
import imgui.ImGui;
import imgui.type.ImBoolean;

/**
 * The SceneRenderer class provides a way to render 2D scenes and display various statistics and settings using ImGui.
 */
public class SceneRenderer {
    /**
     * A boolean value to determine whether or not to show the demo windows in the ImGui interface.
     */
    private final ImBoolean showDemoWindows = new ImBoolean(false);

    /**
     * Constructs a new instance of the SceneRenderer class.
     */
    public SceneRenderer() {
    }

    /**
     * Renders the ImGui interface and displays various statistics and settings.
     */
    public void onImGuiRender() {
        ImGui.begin("Renderer2D Stats:");
        ImGui.text("Renderer2D Stats:");
        ImGui.text("Draw Calls: " + Renderer2D.Statistics.drawCalls);
        ImGui.text("Quads: " + Renderer2D.Statistics.quadCount);
        ImGui.text("Vertices: " + Renderer2D.Statistics.getTotalVertexCount());
        ImGui.text("Indices: " + Renderer2D.Statistics.getTotalIndexCount());

        ImGui.end();

        ImGui.begin("Settings");
        ImGui.checkbox("Show demo window", showDemoWindows);
        ImGui.end();
    }
    /**
     * Returns the boolean value for whether or not to show the demo windows.
     *
     * @return The boolean value for whether or not to show the demo windows.
     */
    public ImBoolean getShowDemoWindow() {
        return showDemoWindows;
    }
}
