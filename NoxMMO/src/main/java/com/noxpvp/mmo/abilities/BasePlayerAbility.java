package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Player;

import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.PlayerManager;

public abstract class BasePlayerAbility extends BaseEntityAbility implements PlayerAbility {
	
	public BasePlayerAbility(final String name, Player player)
	{
		super(name, player);
	}
	
	public Player getPlayer() {
		return (Player) getEntity();
	}

	public NoxPlayer getNoxPlayer() {
		if (isValid())
			return PlayerManager.getInstance().getPlayer(getPlayer());
		return null;
	}
	
	/**
	 * Returns is the player of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
}
