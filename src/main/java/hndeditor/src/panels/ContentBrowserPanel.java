package hndeditor.src.panels;

import hnd.src.renderer.Texture2D;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ContentBrowserPanel {
    private final Texture2D directoryIcon;
    private final Texture2D fileIcon;
    private Path currentDirectory;
    private final Path assetPath = Paths.get("assets"); // "assets"
    private final StringPayload payload = new StringPayload();

    public ContentBrowserPanel() {
        currentDirectory = assetPath;
        directoryIcon = Texture2D.create("assets/logo/contentbrowser/DirectoryIcon.png");
        fileIcon = Texture2D.create("assets/logo/contentbrowser/FileIcon.png");
    }

    public void onImGuiRender() {
        ImGui.begin("Content Browser");
        if (currentDirectory != assetPath) {
            if (ImGui.button("<-")) {
                if (currentDirectory.getParent() != null) {
                    currentDirectory = currentDirectory.getParent();
                }
            }
        }
        float[] padding = {16.0f};
        float[] thumbnailSize = {128.0f};
        float cellSize = padding[0] + thumbnailSize[0];

        float panelWidth = ImGui.getContentRegionAvailX();
        int columnCount = (int) (panelWidth / cellSize);
        if (columnCount < 1) {
            columnCount = 1;
        }
        ImGui.columns(columnCount, "0", false);


        File folder = new File(String.valueOf(currentDirectory));
        File[] listOfFiles = folder.listFiles();
        for (File file : Objects.requireNonNull(listOfFiles)) {
            ImGui.pushID(file.getName());
            Texture2D icon = file.isDirectory() ? directoryIcon : fileIcon;
            ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 0.0f, 0.0f, 0.0f);
            ImGui.imageButton(icon.getRendererID(), thumbnailSize[0], thumbnailSize[0], 0.0f, 1.0f, 1.0f, 0.0f);
            payload.file = file;
            if (ImGui.beginDragDropSource()) {
                ImGui.setDragDropPayload("CONTENT_BROWSER_ITEM", payload);
                ImGui.text(payload.getData().toString());
                ImGui.endDragDropSource();
            }

            ImGui.popStyleColor();

            if (ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                if (file.isDirectory()) {
                    currentDirectory = currentDirectory.resolve(file.getName());
                }
            }

            ImGui.textWrapped(file.getName());
            ImGui.nextColumn();
            ImGui.popID();
        }


        ImGui.columns(1);
        ImGui.end();
    }

    public interface Payload<T> {
        T getData();
    }

    static final class StringPayload implements Payload<File> {
        File file;

        @Override
        public File getData() {
            return file;
        }
    }
}
