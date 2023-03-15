package hndeditor.src.panels;

import hnd.src.core.UUID;
import hnd.src.renderer.Texture2D;
import hnd.src.scene.Entity;
import hnd.src.scene.Scene;
import hnd.src.scene.components.Component;
import hnd.src.scene.components.SpriteRendererComponent;
import hnd.src.scene.components.TagComponent;
import hnd.src.scene.components.TransformComponent;
import hnd.src.utils.Maths;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImString;
import org.joml.Vector3f;

/**
 * This class represents the scene hierarchy panel in the HND Editor application.
 * It is responsible for rendering and managing the scene hierarchy view,
 * as well as allowing the user to select and edit entities and their components.
 */
public class SceneHierarchyPanel {
    private Scene context;
    private Entity selectionContext;
    private Entity entityToRemove = null;
    private boolean removeEntity;
    private boolean removeComponent;
    private float[] colorComponent = {1, 1, 1, 1};
    private float[] tilingFactor = {1};
    private Object payload = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");

    /**
     * Creates a new SceneHierarchyPanel instance.
     */
    public SceneHierarchyPanel() {
    }

    /**
     * Sets the scene that this SceneHierarchyPanel instance should be associated with.
     *
     * @param context the scene to associate this panel with
     */
    public void setContext(Scene context) {
        this.context = context;
        selectionContext = null;
    }

    /**
     * Renders the scene hierarchy panel and its associated properties panel using ImGui.
     */
    public void onImGuiRender() {
        ImGui.begin("Scene Hierarchy");
        if (context != null) {
            context.entityMap.forEach(this::drawEntityNode);
            if (removeEntity) {
                context.destroyEntity(entityToRemove);
                if (selectionContext == entityToRemove) {
                    selectionContext = null;
                }
            }
            if (ImGui.isMouseDown(0) && ImGui.isWindowHovered()) {
                selectionContext = null;
            }
            if (ImGui.beginPopupContextWindow(ImGuiPopupFlags.NoOpenOverItems | ImGuiPopupFlags.MouseButtonRight)) {
                if (ImGui.menuItem("Create Empty Entity")) {
                    context.createEntity("Empty Entity");
                }
                ImGui.endPopup();
            }
        }
        ImGui.end();
        ImGui.begin("Properties");
        if (selectionContext != null) {
            drawComponents(selectionContext);
        }
        ImGui.end();
    }

    /**
     * Draws an entity node in the scene hierarchy panel.
     *
     * @param uuid   the UUID of the entity to draw
     * @param entity the entity to draw
     */
    private void drawEntityNode(UUID uuid, Entity entity) {
        String tag = entity.getComponent(TagComponent.class).tag;

        int flags = ((selectionContext == entity) ? ImGuiTreeNodeFlags.Selected : 0) | ImGuiTreeNodeFlags.OpenOnArrow;
        flags |= ImGuiTreeNodeFlags.SpanAvailWidth;
        boolean opened = ImGui.treeNodeEx(entity.hashCode(), flags, tag);
        if (ImGui.isItemClicked()) {
            selectionContext = entity;
        }
        removeEntity = false;
        if (ImGui.beginPopupContextItem()) {
            if (ImGui.menuItem("Delete Entity")) {
                removeEntity = true;
                selectionContext = entity;
            }
            ImGui.endPopup();
        }
        if (opened) {
            int flagsTwo = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.SpanAvailWidth;
			/*TODO cast something meaningful instead of 9817239. This should be a pointer for entity class.
		 	https://github.com/ocornut/imgui/blob/master/docs/FAQ.md */
            boolean openedTwo = ImGui.treeNodeEx(9817239, flagsTwo, tag);
            if (openedTwo) {
                ImGui.treePop();
            }
            ImGui.treePop();
        }
        if (removeEntity) {
            entityToRemove = selectionContext;
        }
    }

    /**
     * Draws the components of an entity in the properties panel.
     *
     * @param entity the entity whose components to draw
     */
    private void drawComponents(Entity entity) {
        if (entity.hasComponent(TagComponent.class)) {
            String tag = entity.getComponent(TagComponent.class).tag;
            ImString buffer = new ImString();
            buffer.set(tag);
            if (ImGui.inputText("##Tag", buffer)) {
                entity.getComponent(TagComponent.class).tag = buffer.toString();
            }
        }

        ImGui.sameLine();
        ImGui.pushItemWidth(-1);

        if (ImGui.button("Add component")) {
            ImGui.openPopup("AddComponent");
        }

        if (ImGui.beginPopup("AddComponent")) {
            displayAddComponentEntry(new SpriteRendererComponent(), "Sprite Renderer");

            ImGui.endPopup();
        }

        ImGui.popItemWidth();

        if (drawComponent("Transform", entity, entity.getComponent(TransformComponent.class))) {
            drawVec3Control("Translation", entity.getComponent(TransformComponent.class).translation, 0.0f);
            Vector3f rotation = Maths.radiansToDegrees(entity.getComponent(TransformComponent.class).rotation);
            drawVec3Control("Rotation", rotation, 0.0f);
            entity.getComponent(TransformComponent.class).rotation = Maths.degreesToRadians(rotation);
            drawVec3Control("Scale", entity.getComponent(TransformComponent.class).scale, 1.0f);
            ImGui.treePop();
        }
        if (drawComponent("Sprite Renderer", entity, entity.getComponent(SpriteRendererComponent.class))) {
            float[] color = {entity.getComponent(SpriteRendererComponent.class).color.x, entity.getComponent(SpriteRendererComponent.class).color.y, entity.getComponent(SpriteRendererComponent.class).color.z, entity.getComponent(SpriteRendererComponent.class).color.w};
            colorComponent = color;
            ImGui.colorEdit4("Color", colorComponent);
            entity.getComponent(SpriteRendererComponent.class).color.set(colorComponent);
            ImGui.button("Texture", 100.0f, 0.0f);
            if (ImGui.beginDragDropTarget()) {
                payload = ImGui.acceptDragDropPayload("CONTENT_BROWSER_ITEM");
                if (payload != null) {
                    ContentBrowserPanel.StringPayload path = (ContentBrowserPanel.StringPayload) payload;
                    Texture2D texture = Texture2D.create(path.getData().toString());
                    if (texture.isLoaded()) {
                        entity.getComponent(SpriteRendererComponent.class).texture = texture;
                    }
                }
                ImGui.endDragDropTarget();
            }
            ImGui.sameLine();
            if (ImGui.button("Delete texture", 100.0f, 0.0f)) {
                entity.getComponent(SpriteRendererComponent.class).texture = null;
            }
            ImGui.dragFloat("Tiling factor", tilingFactor, 0.1f, 0.0f, 100.0f);
            entity.getComponent(SpriteRendererComponent.class).tilingFactor = tilingFactor[0];
            ImGui.treePop();
        }
    }

    /**
     * Draws a vector3f control in the ImGui window.
     *
     * @param label      the label to display next to the control
     * @param values     the values of the vector3f to edit
     * @param resetValue the value to reset the vector3f to when the user double-clicks on it
     * @return true if the vector3f control was edited, false otherwise
     */
    private void drawVec3Control(String label, Vector3f values, float resetValue) {
        float columnWidth = 100.0f;

        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float calcItemWidth = ImGui.calcItemWidth() / 3;
        ImGui.pushItemWidth(calcItemWidth);
        ImGui.pushItemWidth(calcItemWidth);
        ImGui.pushItemWidth(calcItemWidth);

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0.0f, 0.0f);

        ImGuiStyle style = new ImGuiStyle();
        float lineHeight = ImGui.getFont().getFontSize() + style.getFramePaddingY() * 2.0f;

        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("X", lineHeight + 3.0f, lineHeight)) {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] valuesx = {values.x};
        ImGui.dragFloat("##X", valuesx, 0.1f, 0.0f, 0.0f, "%.2f");
        values.x = valuesx[0];
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("Y", lineHeight + 3.0f, lineHeight)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] valuesy = {values.y};
        ImGui.dragFloat("##Y", valuesy, 0.1f, 0.0f, 0.0f, "%.2f");
        values.y = valuesy[0];
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.25f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.35f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.25f, 0.8f, 1.0f);
        if (ImGui.button("Z", lineHeight + 3.0f, lineHeight)) {
            values.z = resetValue;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] valuesz = {values.z};
        ImGui.dragFloat("##Z", valuesz, 0.1f, 0.0f, 0.0f, "%.2f");
        values.z = valuesz[0];
        ImGui.popItemWidth();

        ImGui.popStyleVar();

        ImGui.columns(1);

        ImGui.popID();
    }

    /**
     * Draws a component in the properties panel and allows the user to edit its properties.
     *
     * @param name      the name of the component to draw
     * @param entity    the entity containing the component to draw
     * @param component the component to draw
     * @param <T>       the type of the component to draw
     * @return true if the component was edited, false otherwise
     */
    private <T extends Component> boolean drawComponent(String name, Entity entity, T component) {
        int treeNodeFlags = ImGuiTreeNodeFlags.DefaultOpen | ImGuiTreeNodeFlags.Framed | ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.AllowItemOverlap | ImGuiTreeNodeFlags.FramePadding;
        if (component != null) {
            if (entity.hasComponent(component.getClass())) {
                ImVec2 contentRegionAvailable = ImGui.getContentRegionAvail();

                ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 4.0f, 4.0f);
                ImGuiStyle style = new ImGuiStyle();
                float lineHeight = ImGui.getFont().getFontSize() + style.getFramePaddingY() * 2.0f;
                ImGui.separator();
                boolean open = ImGui.treeNodeEx(component.hashCode(), treeNodeFlags, name);
                ImGui.popStyleVar();
                ImGui.sameLine(contentRegionAvailable.x - lineHeight * 0.5f);
                if (ImGui.button("+", lineHeight, lineHeight)) {
                    ImGui.openPopup("ComponentSettings");
                }
                removeComponent = false;
                if (ImGui.beginPopup("ComponentSettings")) {
                    if (ImGui.menuItem("Remove component")) {
                        removeComponent = true;
                    }
                    ImGui.endPopup();
                }
                return open;
            }
        }
        return false;
    }

    /**
     * Displays an entry in the "AddComponent" popup menu for the given component type.
     *
     * @param component the component to display an entry for
     * @param entryName the label to display for the entry
     * @param <T>       the type of the component to display an entry for
     */
    private <T extends Component> void displayAddComponentEntry(T component, String entryName) {
        if (!selectionContext.hasComponent(component.getClass())) {
            if (ImGui.menuItem(entryName)) {
                selectionContext.addComponent(component);
                ImGui.closeCurrentPopup();
            }
        }
    }

    public Entity getSelectedEntity() {
        return selectionContext;
    }
}