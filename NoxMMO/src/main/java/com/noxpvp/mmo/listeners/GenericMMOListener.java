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
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;

import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;

public class GenericMMOListener<T extends Event> extends NoxListener<NoxMMO> {
	
	private Class<T> eventType;
	private WeakHashMap<String, MMOEventHandler<T>> abe_name_cache;
	private Map<EventPriority, SortedSet<MMOEventHandler<T>>> abilityHandlers;
	
	public static <T extends Event> GenericMMOListener newListener(NoxMMO plugin, Class<T> type) {
		if (type.isAssignableFrom(PlayerInteractEvent.class))
			return new PlayerInteractHandler(plugin);
		return null;
	}
	
	protected GenericMMOListener(NoxMMO plugin, Class<T> type) {
		super(plugin);
		eventType = type;
		
		abe_name_cache = new WeakHashMap<String, MMOEventHandler<T>>();
		abilityHandlers = new HashMap<EventPriority, SortedSet<MMOEventHandler<T>>>();
		for (EventPriority pri: EventPriority.values())
			abilityHandlers.put(pri, new TreeSet<MMOEventHandler<T>>());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEventLowest(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.LOWEST))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEventLow(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.LOW))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEventNormal(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.NORMAL))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEventHigh(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGH))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEventHighest(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.HIGHEST))
		{
			if (event instanceof Cancellable && ((Cancellable) event).isCancelled() && handler.ignoreCancelled())
				continue;
			handler.execute(event);
		}	
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEventMonitor(T event)
	{
		for (MMOEventHandler<T> handler : abilityHandlers.get(EventPriority.MONITOR))
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
	
	public void unregisterHandler(MMOEventHandler<T> handler)
	{
		String name;
		if (!abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already not registered to this handler.");
		
		abilityHandlers.get(handler.getEventPriority()).remove(handler);
		abe_name_cache.remove(name);
		
		boolean empty = true;
		for (EventPriority priority : abilityHandlers.keySet()) {
			if (!abilityHandlers.get(priority).isEmpty()) {
				empty = false;
			}
		}
		
		if (empty)
			unregister();
	}
	
	public void registerHandler(MMOEventHandler<T> handler)
	{
		String name;
		if (abe_name_cache.containsKey((name = handler.getID())))
			throw new IllegalStateException(name + " is already registered!");
		
		abe_name_cache.put(name, handler);
		abilityHandlers.get(handler.getEventPriority()).add(handler);
		
		for (EventPriority priority : abilityHandlers.keySet()) {
			if (!abilityHandlers.get(priority).isEmpty()) {
				if (isRegistered())
					return;
				else
					register();
			}
		}
	}
	
	public static class EntityDamageHandlers { //Simply used for sorting...
		public static class EntityDamageByEntityHandler extends GenericMMOListener<EntityDamageByEntityEvent> {
			public EntityDamageByEntityHandler(NoxMMO plugin) {
				super(plugin, EntityDamageByEntityEvent.class);
			}
		}
		
		public static class EntityDamageByBlockHandler extends GenericMMOListener<EntityDamageByBlockEvent> {
			public EntityDamageByBlockHandler(NoxMMO plugin) {
				super(plugin, EntityDamageByBlockEvent.class);
			}
		}
		
		public static class EntityDamageHandler extends GenericMMOListener<EntityDamageEvent> {
			public EntityDamageHandler(NoxMMO plugin) {
				super(plugin, EntityDamageEvent.class);
			}
		}
	}
	
	public static class PlayerInteractHandler extends GenericMMOListener<PlayerInteractEvent> {
		public PlayerInteractHandler(NoxMMO plugin) {
			super(plugin, PlayerInteractEvent.class);
		}
	}
	
	public static class EntityInteractHandler extends GenericMMOListener<EntityInteractEvent> {
		public EntityInteractHandler(NoxMMO plugin) {
			super(plugin, EntityInteractEvent.class);
		}
	}
}
