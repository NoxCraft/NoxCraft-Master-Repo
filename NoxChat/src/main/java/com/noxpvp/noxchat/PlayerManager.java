package com.noxpvp.noxchat;

import java.util.HashMap;
import java.util.Map;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.manager.BasePlayerManager;

public class PlayerManager extends BasePlayerManager<ChatPlayer>{
	private static PlayerManager instance;
	
	public static PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		return instance;
	}
	
	private PlayerManager() {
		super(ChatPlayer.class);
	}
	

	public NoxPlugin getPlugin() {
		return NoxChat.getInstance();
	}

	@Override
	protected ChatPlayer craftNew(String name) {
		return new ChatPlayer(name);
	}

	@Override
	protected Map<String, ChatPlayer> craftNewStorage() {
		return new HashMap<String, ChatPlayer>();
	}

	@Override
	protected boolean preUnloadPlayer(String name) {
		return true;
	}
}
