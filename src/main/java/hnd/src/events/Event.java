package hnd.src.events;

public class Event {
	public boolean handled = false;
	public Event handle;

	public EventType getEventType() {
		return getStaticType();
	}

	public EventType getStaticType() {
		return null;
	}

}
