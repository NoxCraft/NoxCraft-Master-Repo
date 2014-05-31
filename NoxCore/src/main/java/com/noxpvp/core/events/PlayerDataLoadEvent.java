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

package com.noxpvp.core.events;

import org.bukkit.event.HandlerList;

import com.noxpvp.core.data.NoxPlayerAdapter;

@Deprecated
public class PlayerDataLoadEvent extends NoxPlayerDataEvent {
	private boolean isOverwriting;

	@Deprecated
	public PlayerDataLoadEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		this(player, honorCore, true);
	}

	@Deprecated
	public PlayerDataLoadEvent(NoxPlayerAdapter player, boolean honorCore, boolean shouldOverwrite)
	{
		super(player, honorCore);
		this.isOverwriting = shouldOverwrite;
	}

	@Deprecated
	public boolean isOverwriting() {
		return isOverwriting;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	private static final HandlerList handlers = new HandlerList();
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
