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
