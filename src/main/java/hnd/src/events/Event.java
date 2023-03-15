package hnd.src.events;

/**
 * The Event class is the base class for all types of events.
 */
public class Event {

    /**
     * Indicates whether or not the event has been handled.
     */
    public boolean handled = false;

    /**
     * The handle object associated with this event.
     */
    public Event handle;

    /**
     * Gets the EventType object associated with this event.
     *
     * @return The EventType object.
     */
    public EventType getEventType() {
        return getStaticType();
    }

    /**
     * Gets the EventType object associated with this event.
     *
     * @return The EventType object.
     */
    public EventType getStaticType() {
        return null;
    }
}