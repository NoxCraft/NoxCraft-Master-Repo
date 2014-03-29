package com.noxpvp.core.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.proxies.ProxyBase;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.proxies.WeakProxyBase;

public abstract class BaseNoxPlayerAdapter extends WeakProxyBase<NoxPlayer> implements NoxPlayerAdapter {
	private final String playerName;
	
	static {
		ProxyBase.validate(BaseNoxPlayerAdapter.class);
	}
	
	public BaseNoxPlayerAdapter(NoxPlayerAdapter player)
	{
		super(player.getNoxPlayer());
		this.playerName = player.getPlayerName();
	}
	
	public BaseNoxPlayerAdapter(OfflinePlayer player) {
		this(player.getName());
	}
	
	public BaseNoxPlayerAdapter(String name)
	{
		this(getNoxPlayer(name));
	}
	
	public final void setName(String name) {
		getProxyBase().setName(name);
	}
	
	public final void updateUID() {
		getProxyBase().updateUID();
	}
	
	public final NoxPlayer getNoxPlayer() {
		if (getProxyBase() != null)
			return getProxyBase();
		else
			return PlayerManager.getInstance().getPlayer(getPlayerName());
	}
	
	public boolean hasFirstLoaded() {
		return getProxyBase().hasFirstLoaded();
	}
	
	public final NoxPlayer getNoxPlayer(boolean forceUpdate)
	{
		if (forceUpdate)
			return PlayerManager.getInstance().getPlayer(getPlayerName());
		else
			return getProxyBase();
	}
	
	public final ConfigurationNode getPersistantData() {
		return getProxyBase().getPersistantData();
	}
	
	public final ConfigurationNode getTempData() {
		return getProxyBase().getTempData();
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
	
	public final String getFullName() {
		return getNoxPlayer().getFullName();
	}
	
	private static NoxPlayer getNoxPlayer(String name) {
		return PlayerManager.getInstance().getPlayer(name);
	}

	public void setProxyBase(NoxPlayer base) {
		super.setProxyBase(base);
	}

	public NoxPlayer getProxyBase() {
		if (super.getProxyBase() == null)
			setProxyBase(getNoxPlayer(true));
			
		return super.getProxyBase();
	}

	public String toString() {
		return getProxyBase().toString();
	}

	public int hashCode() {
		return getProxyBase().hashCode();
	}

	public CoreBoard getCoreBoard() {
		return getProxyBase().getCoreBoard();
	}

	public CoreBar getCoreBar() {
		return getProxyBase().getCoreBar();
	}

	public boolean hasCoreBox(CoreBox box) {
		return getProxyBase().hasCoreBox(box);
	}

	public void setCoreBox(CoreBox box) {
		getProxyBase().setCoreBox(box);
	}

	public boolean addCoolDown(String name, long length, boolean coreBoardTimer) {
		return getProxyBase().addCoolDown(name, length, coreBoardTimer);
	}

	public void decrementVote() {
		getProxyBase().decrementVote();
	}

	public List<CoolDown> getCoolDowns() {
		return getProxyBase().getCoolDowns();
	}

	public long getFirstJoin() {
		return getProxyBase().getFirstJoin();
	}

	public long getFirstJoin(boolean cached) {
		return getProxyBase().getFirstJoin(cached);
	}

	public Location getLastDeathLocation() {
		return getProxyBase().getLastDeathLocation();
	}

	public long getLastDeathTS() {
		return getProxyBase().getLastDeathTS();
	}

	public long getLastJoin() {
		return getProxyBase().getLastJoin();
	}

	public long getLastJoin(boolean cached) {
		return getProxyBase().getLastJoin(cached);
	}

	public SafeLocation getLastLocation() {
		return getProxyBase().getLastLocation();
	}

	public World getLastWorld() {
		return getProxyBase().getLastWorld();
	}

	public String getLastWorldName() {
		return getProxyBase().getLastWorldName();
	}

	public Double getMoney() {
		return getProxyBase().getMoney();
	}

	public Double getMoney(String worldName) {
		return getProxyBase().getMoney(worldName);
	}

	public String getName() {
		return getProxyBase().getName();
	}

	public int getVotes() {
		return getProxyBase().getVotes();
	}

	public boolean hasPermission(String permNode) {
		return getProxyBase().hasPermission(permNode);
	}

	public boolean hasPermissions(String... permissions) {
		return getProxyBase().hasPermissions(permissions);
	}

	public void incrementVote() {
		getProxyBase().incrementVote();
	}

	public boolean isCooldownActive(String name) {
		return getProxyBase().isCooldownActive(name);
	}

	public boolean isCooldownExpired(String name) {
		return getProxyBase().isCooldownExpired(name);
	}
	
	public void superLoad(boolean overwrite) {
		getProxyBase().load(overwrite);
	}

	public void removeCooldDown(String name) {
		getProxyBase().removeCooldDown(name);
	}
	
	public final void superSave() {
		getProxyBase().save();
	}
	
	public final void superLoad() {
		getProxyBase().load();
	}
	
	public final UUID getUUID() {
		return getProxyBase().getUUID();
	}
	
	public final String getUID() {
		return getProxyBase().getUID();
	}
	
	/**
	 * All data must be set in here. Do not save to file from here though.
	 */
	public abstract void save();
	
	public abstract void load();
	
	public void saveLastLocation() {
		getProxyBase().saveLastLocation();
	}

	public void saveLastLocation(Player player) {
		getProxyBase().saveLastLocation(player);
	}

	public void setFirstJoin() {
		getProxyBase().setFirstJoin();
	}

	public void setFirstJoin(long value) {
		getProxyBase().setFirstJoin(value);
	}

	public void setLastDeath(PlayerDeathEvent event) {
		getProxyBase().setLastDeath(event);
	}

	public void setLastDeathLocation(Location loc) {
		getProxyBase().setLastDeathLocation(loc);
	}

	public void setLastDeathTS() {
		getProxyBase().setLastDeathTS();
	}

	public void setLastDeathTS(long stamp) {
		getProxyBase().setLastDeathTS(stamp);
	}

	public void setLastKill(Entity entity) {
		getProxyBase().setLastKill(entity);
	}

	public void setLastKillTS() {
		getProxyBase().setLastKillTS();
	}

	public void setLastKillTS(long stamp) {
		getProxyBase().setLastKillTS(stamp);
	}

	public void setVotes(int amount) {
		getProxyBase().setVotes(amount);
	}

	public Location getBedSpawnLocation() {
		return getProxyBase().getBedSpawnLocation();
	}

	public long getFirstPlayed() {
		return getProxyBase().getFirstPlayed();
	}

	public long getLastPlayed() {
		return getProxyBase().getLastPlayed();
	}

	public boolean hasPlayedBefore() {
		return getProxyBase().hasPlayedBefore();
	}

	public boolean isBanned() {
		return getProxyBase().isBanned();
	}

	public boolean isOp() {
		return getProxyBase().isOp();
	}

	public boolean isWhitelisted() {
		return getProxyBase().isWhitelisted();
	}

	public void setBanned(boolean arg0) {
		getProxyBase().setBanned(arg0);
	}

	public void setOp(boolean arg0) {
		getProxyBase().setOp(arg0);
	}

	public void setWhitelisted(boolean arg0) {
		getProxyBase().setWhitelisted(arg0);
	}
	
	public void saveToManager() {
		PlayerManager.getInstance().savePlayer(getNoxPlayer());
	}
}
