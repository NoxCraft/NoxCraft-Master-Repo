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

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

import com.noxpvp.mmo.abilities.BaseAbility;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.events.internal.IAbilityEvent;

public abstract class EntityAbilityEvent extends EntityEvent implements IAbilityEvent {
	private static final HandlerList handlers = new HandlerList();
	protected BaseEntityAbility ability;

	public EntityAbilityEvent(Entity what, BaseEntityAbility ability) {
		super(what);

		this.ability = ability;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public BaseAbility getAbility() {
		return ability;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

}
