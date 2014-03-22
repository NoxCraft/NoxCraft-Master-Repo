package com.noxpvp.core.data;

import java.util.List;

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
		this(player.getPlayerName());
	}
	
	public BaseNoxPlayerAdapter(OfflinePlayer player) {
		this(player.getName());
	}
	
	public BaseNoxPlayerAdapter(String name)
	{
		super(getNoxPlayer(name));
		this.playerName = name;
		
	}
	
	public final NoxPlayer getNoxPlayer() {
		return getProxyBase();
	}
	
	public boolean hasFirstLoaded() {
		return getProxyBase().hasFirstLoaded();
	}
	
	public final NoxPlayer getNoxPlayer(boolean cache)
	{
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

	public void setProxyBase(OfflinePlayer base) {
		getProxyBase().setProxyBase(base);
	}

	public NoxPlayer getProxyBase() {
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

	public void load() {
		getProxyBase().load();
	}

	public void load(boolean overwrite) {
		getProxyBase().load(overwrite);
	}

	public void rebuild_cache() {
		getProxyBase().rebuild_cache();
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
	
	public void save() {
		superSave();
	}

	@Deprecated
	public void save(boolean throwEvent) {
		getProxyBase().save(throwEvent);
	}

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
}
