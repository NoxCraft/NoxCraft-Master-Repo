package com.noxpvp.core.data;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;

public abstract class BaseNoxPlayerAdapter implements NoxPlayerAdapter {
	private final String playerName;
	private Reference<NoxPlayer> playerRef;
	
	public BaseNoxPlayerAdapter(NoxPlayerAdapter player)
	{
		playerRef = new SoftReference<NoxPlayer>(player.getNoxPlayer());
		playerName = player.getNoxPlayer().getName();
	}
	
	public BaseNoxPlayerAdapter(OfflinePlayer player) {
		this(player.getName());
	}
	
	public BaseNoxPlayerAdapter(String name)
	{
		this.playerName = name;
		playerRef = new SoftReference<NoxPlayer>(getNoxPlayer(name));
	}

	public final NoxPlayer getNoxPlayer() {
		return getNoxPlayer(true);
	}
	
	public final NoxPlayer getNoxPlayer(boolean cache)
	{
		if (cache && !isAlive())
			updateReference();
		
		return playerRef.get();
	}
	
	public final String getPlayerName() { return playerName; }
	
	public final OfflinePlayer getOfflinePlayer() { return Bukkit.getOfflinePlayer(getPlayerName()); }
	
	public final boolean isOnline() { return getOfflinePlayer().isOnline(); }
	
	public final Player getPlayer() {
		if (isOnline())
			return (Player) getOfflinePlayer();
		else
			return null;
	}
	
	public final boolean isAlive() { return playerRef.get() != null; }
	
	public final void updateReference() {
		playerRef = new WeakReference<NoxPlayer>(getNoxPlayer(playerName));
	}
	
	private static NoxPlayer getNoxPlayer(String name) {
		return NoxCore.getInstance().getPlayerManager().getPlayer(name);
	}
}
