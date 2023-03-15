package hndeditor.src.panels;

import hnd.src.renderer.Texture2D;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A panel for browsing the content of a directory and displaying its contents as icons with names.
 */
public class ContentBrowserPanel {
    /**
     * The icon to display for directories.
     */
    private final Texture2D directoryIcon;
    /**
     * The icon to display for files.
     */
    private final Texture2D fileIcon;
    /**
     * The current directory being displayed.
     */
    private Path currentDirectory;
    /**
     * The path to the assets directory.
     */
    private final Path assetPath = Paths.get("assets");

    /**
     * The payload used for drag and drop functionality.
     */
    protected static final StringPayload payload = new StringPayload();

    /**
     * Constructs a new {@code ContentBrowserPanel}.
     */
    public ContentBrowserPanel() {
        currentDirectory = assetPath;
        directoryIcon = Texture2D.create("assets/logo/contentbrowser/DirectoryIcon.png");
        fileIcon = Texture2D.create("assets/logo/contentbrowser/FileIcon.png");
    }

    /**
     * Renders the panel's GUI using ImGui.
     */
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
            if (ImGui.beginDragDropSource()) {
                ImGui.setDragDropPayload("CONTENT_BROWSER_ITEM", payload);
                payload.file = file;
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

        ImGui.begin("Log");
        ImGui.end();
    }

    /**
     * The payload interface for drag and drop functionality.
     *
     * @param <T> The type of data being carried by the payload.
     */
    public interface Payload<T> {
        T getData();
    }

    /**
     * The payload used for carrying file data during drag and drop.
     */
    protected static class StringPayload implements Payload<File> {
        /**
         * The file being carried by the payload.
         */
        File file;

        @Override
        public File getData() {
            return file;
        }
    }
}
