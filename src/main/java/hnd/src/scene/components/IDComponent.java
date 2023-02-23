package hnd.src.scene.components;

import hnd.src.core.UUID;

public class IDComponent extends Component {

    public UUID id;

    public IDComponent(UUID uuid) {
        this.id = uuid;
    }
}