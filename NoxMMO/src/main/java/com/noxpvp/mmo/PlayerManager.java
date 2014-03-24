package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.manager.BasePlayerManager;

public class PlayerManager extends BasePlayerManager<MMOPlayer> {
	private WeakHashMap<String, MMOPlayer> players;
	
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
	
	public boolean isPlayerInMemory(String name) {
		if (players.containsKey(name))
			return true;
		else if (com.noxpvp.core.manager.PlayerManager.getInstance().isLoaded(name)) {
			getPlayer(name);
			return true;
		} else
			return false;
	}

	public MMOPlayer getPlayer(NoxPlayerAdapter player) {
		return getPlayer(player.getPlayerName());
	}
	
	public void save() {
		for (MMOPlayer player : players.values())
			player.save();
	}

	public void load() {
		// TODO Auto-generated method stub
	}

	public NoxMMO getPlugin() {
		return NoxMMO.getInstance();
	}

	@Override
	protected MMOPlayer craftNew(String name) {
		return new MMOPlayer(name);
	}

	@Override
	protected Map<String, MMOPlayer> craftNewStorage() {
		return new HashMap<String, MMOPlayer>();
	}

	@Override
	protected boolean preUnloadPlayer(String name) {
		return true;
	}
}
