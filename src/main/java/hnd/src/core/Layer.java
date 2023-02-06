package hnd.src.core;

import hnd.src.events.Event;

/**
 * Abstract class that contains methods that should be overridden.
 * These methods need to handle main logic for the application.
 */
public abstract class Layer {

	/**
	 * Pre init phase.
	 */
	public abstract void onAttach();

	/**
	 * On layer destroy methods. e.g. dispose functions for imgui.
	 */
	public abstract void onDetach();

	public abstract void onUpdate(float ts);

	public abstract void onImGuiRender();

	public abstract void onEvent(Event event);


}
