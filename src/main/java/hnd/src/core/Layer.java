package hnd.src.core;

import hnd.src.events.Event;

/**
 * The base class for all layers in the engine.
 */
public abstract class Layer {

    /**
     * Called when the layer is attached to the application.
     */
    public abstract void onAttach();

    /**
     * Called when the layer is detached from the application.
     */
    public abstract void onDetach();

    /**
     * Called every frame to update the layer.
     *
     * @param ts The elapsed time since the last frame, in seconds.
     */
    public abstract void onUpdate(float ts);

    /**
     * Called every frame to render the ImGui user interface for the layer.
     */
    public abstract void onImGuiRender();

    /**
     * Called when an event is received by the layer.
     *
     * @param event The event that was received.
     */
    public abstract void onEvent(Event event);
}