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
public class PlayerDataSaveEvent extends NoxPlayerDataEvent{
	@Deprecated
	public PlayerDataSaveEvent(NoxPlayerAdapter player, boolean honorCore)
	{
		super(player, honorCore);
	}
	
	@Deprecated
	public HandlerList getHandlers() {
		return handlers;
	}

	@Deprecated
	private static final HandlerList handlers = new HandlerList();

	@Deprecated
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
