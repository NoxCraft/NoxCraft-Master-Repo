package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.Event;

import com.noxpvp.mmo.listeners.MMOEventHandler;
import com.noxpvp.mmo.listeners.GenericNoxListener;

@SuppressWarnings("unchecked")
public class MasterListener {
	private Map<String, GenericNoxListener<?>> listeners = new HashMap<String, GenericNoxListener<?>>();
	
	public <T extends Event> GenericNoxListener<T> getNoxListener(Class<T> eventClass, String eventName)
	{
		GenericNoxListener<T> listener = (GenericNoxListener<T>)listeners.get(eventName);
		if (listener == null)
			listeners.put(eventName, (listener = new GenericNoxListener<T>(NoxMMO.getInstance(), eventClass)));
		
		return listener;
	}
	
	public <T extends Event> GenericNoxListener<T> getNoxListener(T event)
	{
		return getNoxListener((Class<T>)event.getClass(), event.getEventName());
	}
	
	public <T extends Event> boolean unregisterHandler(MMOEventHandler<T> handler)
	{
		String eventName = handler.getEventName();
		if (!listeners.containsKey(eventName))
			return false;
		try {
			((GenericNoxListener<T>)listeners.get(eventName)).unregisterHandler(handler);
			return true;
		} catch (IllegalStateException e) {
			return false;
		}
	}
	
	public <T extends Event> boolean registerHandler(MMOEventHandler<T> handler)
	{
		String eventName = handler.getEventName();
		GenericNoxListener<T> listener;
		if (!listeners.containsKey(eventName))
			listeners.put(eventName, new GenericNoxListener<T>(NoxMMO.getInstance(), handler.getEventType()));

		listener = (GenericNoxListener<T>) listeners.get(eventName);
		
		try { 
			listener.registerHandler(handler);
			return true;
		} catch (IllegalStateException e) {
			return false;
		}
	}

	public void unregisterAll() {
		for (GenericNoxListener<?> listener : listeners.values())
			listener.unregister();
	}
}
