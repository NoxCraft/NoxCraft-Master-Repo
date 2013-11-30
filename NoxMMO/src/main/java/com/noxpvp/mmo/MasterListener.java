package com.noxpvp.mmo;

import java.util.Map;

import org.bukkit.event.Event;

import com.noxpvp.mmo.listeners.EventHandler;
import com.noxpvp.mmo.listeners.GenericNoxListener;

public class MasterListener {
	private Map<String, GenericNoxListener<?>> listeners;
	
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
	
	public <T extends Event> boolean unregisterHandler(EventHandler<T> handler)
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
	
	@SuppressWarnings("rawtypes")
	public <T extends Event> boolean registerHandler(EventHandler<T> handler)
	{
		String eventName = handler.getEventName();
		GenericNoxListener<T> listener;
		if (!listeners.containsKey(eventName))
			listeners.put(eventName, (listener = new GenericNoxListener(NoxMMO.getInstance(), handler.getEventType())));
		else
			listener = (GenericNoxListener<T>) listeners.get(eventName);
		
		try { 
			listener.registerHandler(handler);
			return true;
		} catch (IllegalStateException e) {
			return false;
		}
	}
}
