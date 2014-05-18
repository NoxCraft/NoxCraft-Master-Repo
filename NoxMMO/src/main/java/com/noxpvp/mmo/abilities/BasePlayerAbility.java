package com.noxpvp.mmo.abilities;

import org.apache.commons.lang.IllegalClassException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public abstract class BasePlayerAbility extends BaseEntityAbility implements PlayerAbility {
	
	private MasterListener masterListener;
	
	public BasePlayerAbility(final String name, Player player)
	{
		super(name, player);
		
		this.masterListener = NoxMMO.getInstance().getMasterListener();
	}
	
	public Player getPlayer() {
		if (!(getEntity() instanceof Player))
			throw new IllegalStateException("Internal Data was tampered with..", new IllegalClassException(Player.class, Entity.class));
		return (Player) getEntity();
	}

	public NoxPlayer getNoxPlayer() {
		if (isValid())
			return CorePlayerManager.getInstance().getPlayer(getPlayer());
		return null;
	}
	
	public MasterListener getMasterListener() {
		return masterListener;
	}
	
	public void registerHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.registerHandler(handler);
	}
	
	public void unregisterHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.unregisterHandler(handler);
		
	}
	
	/**
	 * Returns is the player of this ability is null and has the permission, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Player player = getPlayer();
		
		return player != null && player.isValid() && 
				player.isOnline() && hasPermission() &&
				(((this instanceof PVPAbility) && TownyUtil.isPVP(player)) || !(this instanceof PVPAbility));
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
