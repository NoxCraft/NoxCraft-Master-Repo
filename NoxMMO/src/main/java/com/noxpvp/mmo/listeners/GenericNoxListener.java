package com.noxpvp.mmo.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.WeakHashMap;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.abilities.AbilityEventHandler;

public class GenericNoxListener<T extends Event> extends NoxListener {
	private Class<T> eventType;
	private WeakHashMap<String, AbilityEventHandler<T>> abe_name_cache;
	private Map<EventPriority, SortedSet<AbilityEventHandler<T>>> abilityHandlers;
	
	public GenericNoxListener(NoxPlugin plugin, Class<T> type) {
		super(plugin);
		eventType = type;
		
		abe_name_cache = new WeakHashMap<String, AbilityEventHandler<T>>();
		abilityHandlers = new HashMap<EventPriority, SortedSet<AbilityEventHandler<T>>>();
		for (EventPriority pri: EventPriority.values())
			abilityHandlers.put(pri, new TreeSet<AbilityEventHandler<T>>());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEventLowest(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.LOWEST))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEventLow(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.LOW))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEventNormal(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.NORMAL))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEventHigh(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGH))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEventHighest(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGHEST))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}	
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEventMonitor(T event)
	{
		for (AbilityEventHandler<T> handler : abilityHandlers.get(EventPriority.MONITOR))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	public Class<T> getEventType()
	{
		return eventType;
	}
	
	public void unregisterHandler(AbilityEventHandler<T> handler)
	{
		String name;
		if (!abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already not registered to this handler.");
		
		abilityHandlers.get(handler.getEventPriority()).remove(handler);
		abe_name_cache.remove(name);
	}
	
	public void registerHandler(AbilityEventHandler<T> handler)
	{
		String name;
		if (abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already registered!");
		
		abe_name_cache.put(name, handler);
		abilityHandlers.get(handler.getEventPriority()).add(handler);
	}
}
