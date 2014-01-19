package com.noxpvp.core.events;
 
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
 
public class ChatPingEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	
	private final Player player;
 
	public ChatPingEvent(Player player, boolean async)
	{
		super(async);
		this.player = player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}