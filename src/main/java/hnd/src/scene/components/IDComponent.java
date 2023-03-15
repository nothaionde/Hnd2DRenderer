package hnd.src.scene.components;

import hnd.src.core.UUID;

/**
 * The IDComponent class represents a unique identifier component that can be attached to an entity in a scene.
 */

public class IDComponent extends Component {
    /**
     * The unique identifier for this entity.
     */
    public UUID id;

    /**
     * Constructs a new IDComponent with the given UUID.
     *
     * @param uuid The UUID to set as the ID for this entity.
     */
    public IDComponent(UUID uuid) {
        this.id = uuid;
    }
}