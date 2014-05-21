package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.events.internal.IPlayerAbilityEvent;

public class PlayerAbilityEvent extends PlayerEvent implements IPlayerAbilityEvent {
	private final static HandlerList handlers = new HandlerList();
	protected BasePlayerAbility ability;

	public PlayerAbilityEvent(Player who, BasePlayerAbility ability) {
		super(who);
		
		this.ability = ability;
	}

	public BasePlayerAbility getAbility() {
		return ability;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
