package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Player;

import com.noxpvp.core.NoxCore;
import com.noxpvp.core.data.NoxPlayer;

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
			return NoxCore.getInstance().getPlayerManager().getPlayer(getPlayer());
		return null;
	}
	
}
