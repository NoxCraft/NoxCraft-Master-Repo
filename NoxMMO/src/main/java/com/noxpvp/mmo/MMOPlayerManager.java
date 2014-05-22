package com.noxpvp.mmo;

import java.util.HashMap;
import java.util.Map;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.BasePlayerManager;

public class MMOPlayerManager extends BasePlayerManager<MMOPlayer> {
	private static MMOPlayerManager instance;

	private MMOPlayerManager() {
		super(MMOPlayer.class);
	}

	public static MMOPlayerManager getInstance() {
		if (instance == null)
			instance = new MMOPlayerManager();

		return instance;
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
