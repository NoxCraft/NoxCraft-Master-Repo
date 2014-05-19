package com.noxpvp.core.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.player.PlayerInteractEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ChestBlockedEvent.
 */
public class ChestBlockedEvent extends Event implements Cancellable {
	
	/** The cancelled. */
	private boolean cancelled = false;
	
	/** The cause. */
	private final BlockCause cause;
	
	/** The entity. */
	private Entity entity = null;
	
	/** The event cause. */
	private final Event eventCause;
	
	/** The is player. */
	private boolean isPlayer = false;
	
	/**
	 * Instantiates a new chest blocked event.
	 *
	 * @param event the event
	 */
	public ChestBlockedEvent(BlockFormEvent event)
	{
		cause = BlockCause.BlockForm;
		eventCause = event;
	}
	
	/**
	 * Instantiates a new chest blocked event.
	 *
	 * @param event the event
	 */
	public ChestBlockedEvent(BlockPistonEvent event)
	{
		cause = BlockCause.Piston;
		eventCause = event;
	}
	
	/**
	 * Instantiates a new chest blocked event.
	 *
	 * @param event the event
	 */
	public ChestBlockedEvent(BlockPlaceEvent event)
	{
		isPlayer = true;
		entity = event.getPlayer();
		eventCause = event;
		cause = BlockCause.Player;
	}
	
	/**
	 * Instantiates a new chest blocked event.
	 *
	 * @param event the event
	 */
	public ChestBlockedEvent(EntityBlockFormEvent event)
	{
		entity = event.getEntity();
		cause = BlockCause.Entity;
		eventCause = event;
	}
	
	public ChestBlockedEvent(PlayerInteractEvent event)
	{
		entity = event.getPlayer();
		cause = BlockCause.Interact;
		eventCause = event;
	}
	
	/**
	 * Gets the cause.
	 *
	 * @return the cause
	 */
	public final BlockCause getCause() {
		return cause;
	}
	
//	public ChestBlockedEvent(EntityChangeBlockEvent event) //Not supported properly at current moment.
//	{
//		entity = event.getEntity();
//		cause = BlockCause.Entity;
//	}
	
	
	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public final Entity getEntity() {
		return entity;
	}

	/**
 * Gets the event cause.
 *
 * @return the eventCause
 */
	public final Event getEventCause() {
		return eventCause;
	}

	/**
	 * Gets the handlers.
	 *
	 * @return the handlers
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public final Player getPlayer() {
		if (isPlayer())
			return (Player) entity;
		else
			return null;
	}
	
	/**
	 * Checks if is cancelled.
	 *
	 * @return true, if is cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Checks if is entity.
	 *
	 * @return true, if is entity
	 */
	public boolean isEntity() {
		return (entity != null);
	}
	
	/**
	 * Checks if is player.
	 *
	 * @return true, if is player
	 */
	public boolean isPlayer() {
		return isPlayer;
	}

	/**
	 * Sets the cancelled.
	 *
	 * @param cancel the new cancelled
	 */
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	/**
	 * The Enum BlockCause.
	 */
	public static enum BlockCause {
		BlockForm, Entity, Interact, Piston, Player, Unknown
	}
	
	/** The Constant handlers. */
	private static final HandlerList handlers = new HandlerList();
	
	/**
	 * Gets the handler list.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
