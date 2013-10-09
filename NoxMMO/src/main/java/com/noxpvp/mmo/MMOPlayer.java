package com.noxpvp.mmo;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.data.NoxPlayerAdaptor;

public class MMOPlayer implements NoxPlayerAdaptor, Persistant {
	private transient Reference<NoxPlayer> playerRef;
	private final String name;
	
	public MMOPlayer(OfflinePlayer player)
	{
		this(getNoxPlayer(player.getName()));
	}
	
	public MMOPlayer(String name)
	{
		this(getNoxPlayer(name));
	}

	public MMOPlayer(NoxPlayer player)
	{
		playerRef = new SoftReference<NoxPlayer>(player);
		this.name = player.getName();
	}
	
	public void updateReference(NoxPlayer player)
	{
		if (player == null) throw new IllegalArgumentException("Player object cannot be null!");
		if (!player.getName().equals(name)) throw new IllegalArgumentException("Player object does not match player name!");
		
		playerRef = new SoftReference<NoxPlayer>(player);
	}
	
	public void updateReference(){
		updateReference(getNoxPlayer(name));
	}
	
	public NoxPlayer getNoxPlayer() {
		if (playerRef.get() == null)
			updateReference();
		return playerRef.get();
	}
	
	public boolean isAlive() {
		return playerRef.get() != null;
	}
	
	private static NoxPlayer getNoxPlayer(String name) {
		return NoxCore.getInstance().getPlayerManager().getPlayer(name);
	}

	public void save() {
		if (getNoxPlayer() == null)
			return;
		
		//TODO: Implement Saving
	}

	public void load() {
		if (getNoxPlayer() == null)
			return;
		
		//TODO: Implement Loading.
	}

	public String getName() {
		if (getNoxPlayer() != null)
			return getNoxPlayer().getName();
		return this.name;
	}
}
