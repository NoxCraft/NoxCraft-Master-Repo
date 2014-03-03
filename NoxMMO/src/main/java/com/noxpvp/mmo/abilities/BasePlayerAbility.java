package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Player;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.mmo.NoxMMO;

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
	 * Returns is the player of this ability is null and has the permission, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Player player = getPlayer();
		NoxPlayer p = PlayerManager.getInstance().getPlayer(player);
		
		return player != null && VaultAdapter.PermUtils.hasPermission(p.getLastWorld(), player, (NoxMMO.PERM_NODE + ".ability." + the main perm node));
	}
	
}
