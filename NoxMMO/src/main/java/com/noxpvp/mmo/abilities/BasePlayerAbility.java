package com.noxpvp.mmo.abilities;

import org.apache.commons.lang.IllegalClassException;
import org.bukkit.entity.Entity;
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
		if (!(getEntity() instanceof Player))
			throw new IllegalStateException("Internal Data was tampered with..", new IllegalClassException(Player.class, Entity.class));
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

		/*
		 * The below can not work. Must override.
		 */
		
		return player != null && hasPermission();
		
//		return player != null && VaultAdapter.PermUtils.hasPermission(p.getLastWorld(), player,);
	}
	
	/**
	 * Recommended to override if you want to add dynamic perm node support.
	 * 
	 * @return true if allowed or false if not OR if could not retrieve NoxPlayer object.
	 */
	public boolean hasPermission() {
		NoxPlayer p = getNoxPlayer();
		if (p == null)
			return false;
		
		return VaultAdapter.PermUtils.hasPermission(p,  (NoxMMO.PERM_NODE + ".ability." + getName().replaceAll(" ", "-").toLowerCase()));
	}
	
}
