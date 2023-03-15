package hnd.src.scene;

import hnd.src.core.Logger;
import hnd.src.core.UUID;
import hnd.src.scene.components.Component;
import hnd.src.scene.components.IDComponent;
import hnd.src.scene.components.TagComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an entity in a scene.
 */
public class Entity {

    private final List<Component> componentList = new ArrayList<>();

    /**
     * Adds a component to the entity.
     *
     * @param component the component to add to the entity
     * @param <T>       the type of the component
     */
    public <T extends Component> void addComponent(T component) {
        if (hasComponent(component.getClass())) {
            Logger.warn("Entity already has component!");
        }
        componentList.add(component);
    }

    /**
     * Checks if the entity has a component of a specific type.
     *
     * @param component the type of the component to check for
     * @param <T>       the type of the component
     * @return true if the entity has the component, false otherwise
     */
    public <T extends Component> boolean hasComponent(Class<T> component) {
        for (Component c : componentList) {
            if (component.isAssignableFrom(c.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the component of a specific type from the entity.
     *
     * @param component the type of the component to retrieve
     * @param <T>       the type of the component
     * @return the component of the specified type, or null if the entity doesn't have the component
     */
    public <T extends Component> T getComponent(Class<T> component) {
        for (Component c : componentList) {
            if (component.isAssignableFrom(c.getClass())) {
                return component.cast(c);
            }
        }
        return null;
    }

    /**
     * Retrieves the UUID of the entity.
     *
     * @return the UUID of the entity
     */
    public UUID getUUID() {
        return getComponent(IDComponent.class).id;
    }

    /**
     * Retrieves the name of the entity.
     *
     * @return the name of the entity
     */
    public String getName() {
        return getComponent(TagComponent.class).tag;
    }
}