package com.noxpvp.core.data;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.NoxCore;

public abstract class BaseNoxPlayerAdapter implements NoxPlayerAdapter {
	private Reference<NoxPlayer> playerRef;
	private final String playerName;
	
	public BaseNoxPlayerAdapter(String name)
	{
		this.playerName = name;
		playerRef = new SoftReference<NoxPlayer>(getNoxPlayer(name));
	}
	
	public BaseNoxPlayerAdapter(NoxPlayerAdapter player)
	{
		playerRef = new SoftReference<NoxPlayer>(player.getNoxPlayer());
		playerName = playerRef.get().getName();
	}
	
	public BaseNoxPlayerAdapter(OfflinePlayer player) {
		this(player.getName());
	}

	public final String getPlayerName() { return playerName; }
	
	public final NoxPlayer getNoxPlayer() {
		return getNoxPlayer(true);
	}
	
	public final NoxPlayer getNoxPlayer(boolean cache)
	{
		if (cache && !isAlive())
			updateReference();
		
		return playerRef.get();
	}
	
	public final void updateReference() {
		playerRef = new SoftReference<NoxPlayer>(getNoxPlayer(playerName));
	}
	
	public final boolean isAlive() { return playerRef.get() != null; }
	
	private static NoxPlayer getNoxPlayer(String name) {
		return NoxCore.getInstance().getPlayerManager().getPlayer(name);
	}
}
