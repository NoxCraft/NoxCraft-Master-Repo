/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.events.internal.IPlayerAbilityEvent;

public abstract class PlayerAbilityEvent extends PlayerEvent implements IPlayerAbilityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected BasePlayerAbility ability;

	public PlayerAbilityEvent(Player who, BasePlayerAbility ability) {
		super(who);

		this.ability = ability;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BasePlayerAbility getAbility() {
		return ability;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
