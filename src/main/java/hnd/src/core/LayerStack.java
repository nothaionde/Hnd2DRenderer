package hnd.src.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A stack of layers in a game engine.
 */
public class LayerStack {

    // The list of layers in the stack
    private List<Layer> layers = new ArrayList<>();

    /**
     * Get the list of layers in the stack.
     *
     * @return the list of layers in the stack
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * Add a layer to the top of the stack.
     *
     * @param layer the layer to add
     */
    public void pushLayer(Layer layer) {
        layers.add(layer);
    }
}
