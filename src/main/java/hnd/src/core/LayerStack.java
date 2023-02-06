package hnd.src.core;

import java.util.ArrayList;
import java.util.List;

public class LayerStack {

	private List<Layer> layers = new ArrayList<>();

	public List<Layer> getLayers() {
		return layers;
	}

	public void pushLayer(Layer layer) {
		layers.add(layer);
	}
}
