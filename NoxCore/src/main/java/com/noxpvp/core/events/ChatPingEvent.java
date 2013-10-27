package com.noxpvp.core.events;
 
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class ChatPingEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	
	private final Player player;
 
	private boolean cancelled = false;
	
	public ChatPingEvent(Player player, boolean async)
	{
		super(async);
		this.player = player;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}