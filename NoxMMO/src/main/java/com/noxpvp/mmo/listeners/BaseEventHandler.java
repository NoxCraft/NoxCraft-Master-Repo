package com.noxpvp.mmo.listeners;

import java.util.Comparator;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public abstract class BaseEventHandler<T extends Event> implements EventHandler<T>, Comparator<EventHandler<T>> {
	private final String id;
	private final EventPriority eventPriority;
	private int priority;
	
	public BaseEventHandler(String id, EventPriority eventPriority, int priority)
	{
		this.id = id;
		this.eventPriority = eventPriority;
		this.priority = priority;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof EventHandler))
			return false;
		
		return ((EventHandler<?>)obj).getID().equals(this.getID());
	}
	
	public final String getID() {
		return id;
	}
	
	public int compare(EventHandler<?> o1, EventHandler<?> o2) {
		EventPriority ep1 = o1.getEventPriority(), ep2 = o2.getEventPriority();
		int d1 = ep1.getSlot(), d2 = ep2.getSlot();
		
		if (d1 > d2)
			return 1;
		else if (d1 < d2)
			return -1;
		
		int p1 = o1.getPriority(), p2 = o2.getPriority();
		if (p1 > p2)
			return 1;
		else if (p1 < p2)
			return -1;
		else
			return 0;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int value) {
		priority = value;
	}
	
	public EventPriority getEventPriority() {
		return eventPriority;
	}
}
