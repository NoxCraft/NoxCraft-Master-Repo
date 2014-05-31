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

package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.google.common.collect.MapMaker;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.manager.BasePlayerManager;

public class PlayerManager extends BasePlayerManager<MMOPlayer> {
	private static PlayerManager instance;
	
	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		
		return instance;
	}
	
	private PlayerManager()
	{
		super(MMOPlayer.class);
	}

	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

	public void loadPlayer(NoxPlayer player) {
		getPlayer(player).load();
	}

	@Override
	protected MMOPlayer craftNew(NoxPlayer noxPlayer) {
		return new MMOPlayer(noxPlayer);
	}

	@Override
	protected Map<String, MMOPlayer> craftNewStorage() {
		return new HashMap<String, MMOPlayer>();
	}
	
}
