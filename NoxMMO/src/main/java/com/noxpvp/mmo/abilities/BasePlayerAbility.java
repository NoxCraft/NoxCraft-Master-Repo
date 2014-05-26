package com.noxpvp.mmo.abilities;

import org.apache.commons.lang.IllegalClassException;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.internal.IHeated;
import com.noxpvp.core.manager.CorePlayerManager;
import com.noxpvp.core.utils.gui.MessageUtil;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.events.PlayerAbilityPreExecuteEvent;
import com.noxpvp.mmo.locale.MMOLocale;

public abstract class BasePlayerAbility extends BaseEntityAbility implements IPlayerAbility {
	private static MMOPlayerManager pm = MMOPlayerManager.getInstance();
	
	public BasePlayerAbility(final String name, Player player) {
		super(name, player);
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

	/**
	 * Returns is the player of this ability is null and has the permission, thus if the execute method will start
	 *
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Player player = getPlayer();
		if (player == null || !player.isOnline() || !hasPermission())
			return false;
		
		MMOPlayer p;
		if (this instanceof IHeated && (p = pm.getPlayer(player)).isCooldownActive(getName())) {
			if (!(this instanceof SilentAbility))
				MessageUtil.sendLocale(player, MMOLocale.ABIL_ON_COOLDOWN, getName(), p.getReadableRemainingCDTime(getName()));
			
			return false;
		}

		return super.mayExecute();
	}

	@Override
	public boolean isCancelled() {
		return CommonUtil.callEvent(new PlayerAbilityPreExecuteEvent(getPlayer(), this)).isCancelled();
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

		return VaultAdapter.PermUtils.hasPermission(p, (NoxMMO.PERM_NODE + ".ability." + getName().replaceAll(" ", "-").toLowerCase()));
	}

}
