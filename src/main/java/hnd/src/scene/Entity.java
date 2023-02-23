package hnd.src.scene;

import hnd.src.core.Logger;
import hnd.src.core.UUID;
import hnd.src.scene.components.Component;
import hnd.src.scene.components.IDComponent;
import hnd.src.scene.components.TagComponent;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private final List<Component> componentList = new ArrayList<>();

    public <T extends Component> void addComponent(T component) {
        if (hasComponent(component.getClass())) {
            Logger.warn("Entity already has component!");
        }
        componentList.add(component);
    }

    public <T extends Component> boolean hasComponent(Class<T> component) {
        for (Component c : componentList) {
            if (component.isAssignableFrom(c.getClass())) {
                return true;
            }
        }
        return false;
    }

    public <T extends Component> T getComponent(Class<T> component) {
        for (Component c : componentList) {
            if (component.isAssignableFrom(c.getClass())) {
                return component.cast(c);
            }
        }
        return null;
    }

    public UUID getUUID() {
        return getComponent(IDComponent.class).id;
    }

    public String getName() {
        return getComponent(TagComponent.class).tag;
    }
}
