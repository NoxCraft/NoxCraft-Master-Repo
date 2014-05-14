package com.noxpvp.core.data;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.projectiles.BlockProjectileSource;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.SafeLocation;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.gui.CoolDown;
import com.noxpvp.core.gui.CoreBar;
import com.noxpvp.core.gui.CoreBoard;
import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.internal.PermissionHandler;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.UUIDUtil;

public class NoxPlayer implements Persistant, NoxPlayerAdapter {
	
	private WeakHashMap<String, CoolDown> cd_cache;
	private List<CoolDown> cds;
	private PlayerManager manager;
	private String name;
	
	private final PermissionHandler permHandler;
	private ConfigurationNode persistant_data = null;
	
	/**
	 * Safely changes the persistant data object.
	 * <p>Cloning all data from original node.
	 * @param persistant_data to replace with.
	 */
	public void setPersistantData(ConfigurationNode persistant_data) {
		if (this.persistant_data != null)
		{
			Map<String, Object> values = this.persistant_data.getValues();
			for (Entry<String, Object> entry : values.entrySet())
				persistant_data.set(entry.getKey(), entry.getValue());
		}
		this.persistant_data = persistant_data;
	}

	private CoreBar coreBar;
	private CoreBoard coreBoard;
	private Reference<CoreBox> coreBox;
	
	private ConfigurationNode temp_data = new ConfigurationNode();
	
	private boolean isFirstLoad = true;

	public boolean isFirstLoad() {
		return isFirstLoad;
	}

	public void setFirstLoad(boolean isFirstLoad) {
		this.isFirstLoad = isFirstLoad;
	}

	private UUID uid;
	
	public NoxPlayer(NoxPlayer player)
	{
		permHandler = player.permHandler;
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();
		this.name = player.name;
		this.temp_data = player.temp_data;
		this.persistant_data = player.persistant_data;
		this.manager = player.manager;
		this.isFirstLoad = player.isFirstLoad;

		this.coreBoard = player.coreBoard != null? player.coreBoard : null;
		this.coreBar = player.coreBar != null? player.coreBar : null;
		this.coreBox = player.coreBox != null? player.coreBox : null;
		
		if (isFirstLoad)
			load();
	}
	
	public NoxPlayer(PlayerManager mn, String name) {
		Validate.notNull(mn);
		NoxCore core = mn.getPlugin();
		permHandler = core.getPermissionHandler();
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();
		manager = mn;
		this.name = name;
		
		getUID();
		
		if (getPlayer() != null){
			this.coreBar = new CoreBar(core, getPlayer());
			new CoreBoard(core, getPlayer());
		}
		setPersistantData(mn.getPlayerNode(this));
	}
	
	public void updatePersistantData() {
		manager.loadPlayer(this);
	}
	
	public NoxPlayer(PlayerManager mn, UUID uid) {
		Validate.notNull(mn);
		NoxCore core = mn.getPlugin();
		permHandler = core.getPermissionHandler();
		cds = new ArrayList<CoolDown>();
		cd_cache = new WeakHashMap<String, CoolDown>();
		manager = mn;
		this.name = null;
		this.uid = uid;
		
		if (getPlayer() != null){
			this.coreBar = new CoreBar(core, getPlayer());
			new CoreBoard(core, getPlayer());
		}
		this.persistant_data = mn.getPlayerNode(this);
	}
	
	public NoxPlayer(PlayerManager mn, String name, String uid) {
		this(mn, name);
		this.uid = UUID.fromString(uid);
	}
	
	private boolean isBadUID() {
		if (this.uid == null || this.uid == UUIDUtil.ZERO_UUID)
			return true;
		return false;
	}
	
	public UUID getUUID(boolean autoUpdate) {
		if (autoUpdate) {
			if (isBadUID())
				this.uid = UUIDUtil.getInstance().tryGetID(getName());
			
			if (isBadUID())
				this.uid = null;
			
			if (isBadUID() && isOnline())
				this.uid = getPlayer().getUniqueId();
		}
		return uid;
	}
	
	public UUID getUUID() {
		return getUUID(true);
	}
	
	public String getUID() {
		if (getUUID() != null)
			return getUUID().toString();
		return null;
	}
	
	public boolean hasFirstLoaded() {
		return isFirstLoad;
	}
	
	public CoreBoard getCoreBoard(){
		if (coreBoard == null)
			return (coreBoard = new CoreBoard(manager.getPlugin(), getPlayer()));
		
		return coreBoard;
	}
	
	public CoreBar getCoreBar(){
		if (coreBar == null)
			return (coreBar = new CoreBar(manager.getPlugin(), getPlayer()));
		
		return coreBar;
	}
	
	public boolean hasCoreBox(){
		return coreBox != null && coreBox.get() != null;
	}
	
	public boolean hasCoreBox(CoreBox box){
		return coreBox != null && coreBox.get() == box;
	}
	
	public void setCoreBox(CoreBox box){
		if (hasCoreBox())
			deleteCoreBox();
		
		coreBox = new WeakReference<CoreBox>(box);
	}

	public void deleteCoreBox(){
		if (hasCoreBox())
			getPlayer().closeInventory();
		
		coreBox = null;
	}
	
	
	/**
	 * Adds new cooldown to player.
	 * <br>
	 * This will use nano seconds if {@link NoxCore#isUsingNanoTime()} returns true. Else it will use millis.
	// * @param name of cooldown
	 * @param length of cooldown specified by nanos or millis depending is {@link NoxCore#isUsingNanoTime()}
//	 * @return
	 */
	public boolean addCoolDown(String name, long length, boolean coreBoardTimer)
	{
		NoxCore core = manager.getPlugin();
		boolean isNano = NoxCore.isUsingNanoTime();
		
		if (cd_cache.containsKey(name) && !cd_cache.get(name).expired())
			return false;
		
		CoolDown cd;
		
		long time = 0;
		if (isNano)
			time = System.nanoTime() + length;
		else
			time = length;
		
		cd = new CoolDown(name, time, isNano);
		
		cds.add(cd);
		cd_cache.put(cd.getName(), cd);
		
		if (coreBoardTimer) {
			ChatColor cdNameColor, cdCDColor;
			try {
				cdNameColor = ChatColor.valueOf(core.getCoreConfig().get(
						"gui.coreboard.cooldowns.name-color",
						String.class,
						"&e"));
				cdCDColor = ChatColor.valueOf(core.getCoreConfig().get(
						"gui.coreboard.cooldowns.time-color",
						String.class,
						"&a"));
				
			} catch (IllegalArgumentException e) {
				cdNameColor = ChatColor.YELLOW;
				cdCDColor = ChatColor.GREEN;
			}
			
			getCoreBoard().addTimer(
					name,
					name,
					(int) (isNano ? ((length / 1000) / 1000) : (length / 1000)),
					cdNameColor,
					cdCDColor);
		}
		
		return true;
	}
	
	public void decrementVote() {
		setVotes(getVotes() - 1);
	}
	
	public List<CoolDown> getCoolDowns() {
		return cds;
	}

	public long getFirstJoin() {
		return getFirstJoin(true);
	}
	
	public long getFirstJoin(boolean cached)
	{
		if (cached)
			return persistant_data.get("first.join", getOfflinePlayer().getFirstPlayed());
		else
			return getOfflinePlayer().getFirstPlayed();
	}
	
	public String getFullName() {
		StringBuilder text = new StringBuilder();
		text.append(VaultAdapter.chat.getGroupPrefix(getLastWorld(), getMainGroup()) + getPlayer().getName());
		
		String v = persistant_data.get("formatted-name", text.toString());
		
		if (!isOnline())
			return v;
		
		String v2 = VaultAdapter.GroupUtils.getFormatedPlayerName(getPlayer());
		if (!v2.equals(v))
			persistant_data.set("formatted-name", v2);

		return v2;
	}
	
	public Location getLastDeathLocation()
	{
		SafeLocation l = null;
		return ((l=this.persistant_data.get("last.death.location", SafeLocation.class))==null?null:l.toLocation()); //Nice handy work here!
	}
	
	public long getLastDeathTS()
	{
		return (this.persistant_data.get("last.death.timestamp", (long)0));
	}
	
	public long getLastJoin() {
		return getLastJoin(true);
	}
	
	public long getLastJoin(boolean cached)
	{
		if (cached)
			return persistant_data.get("last.join", long.class);
		else
			return getOfflinePlayer().getLastPlayed();
	}
	
	public SafeLocation getLastLocation() {
		if (getPlayer() != null)
			persistant_data.set("last.location", new SafeLocation(getPlayer().getLocation()));
		return persistant_data.get("last.location", SafeLocation.class);
	}
	
	public World getLastWorld() {
		String worldName = getLastWorldName();
		if (LogicUtil.nullOrEmpty(worldName) || worldName.equals("NONE"))
			return null;
		
		return Bukkit.getWorld(worldName);
	}
	
	public String getLastWorldName() {
		World w = null;
		if (getPlayer() != null)
		{
			w = getPlayer().getWorld();
			persistant_data.set("last.world", w.getName());
		}
		
		if (w != null)
			return w.getName();
		
		return persistant_data.get("last.world", String.class, "NONE");
	}
	
	private String getMainGroup() {
		
		String[] groups = VaultAdapter.permission.getPlayerGroups(getPlayer());
		LinkedList<String> groupList = new LinkedList<String>();//: put local group list here
		
		if (groups.length < 0) return null;
		
		int ind = 100;
		String finalGroup = null;
		
		for (String group : groups) {
				if (groupList.indexOf(group) < ind) {
						ind = groupList.indexOf(group);
						finalGroup = group;
				}
		}
		
		return finalGroup;
	}
	
	public Double getMoney() { return VaultAdapter.economy.getBalance(getPlayerName(), getLastWorldName()); }
	
	public Double getMoney(String worldName) { return VaultAdapter.economy.getBalance(getPlayerName(), worldName); }
	
	@Deprecated
	public String getName(){
		return getPlayerName();
	}
	
	public NoxPlayer getNoxPlayer() { return this; }
	
	public OfflinePlayer getOfflinePlayer() {
		if (getUUID() != null)
			return Bukkit.getOfflinePlayer(getUUID());
		else if (getPlayerName() != null)
			return Bukkit.getOfflinePlayer(getPlayerName());
		else
			return null;
	}
	
	public final ConfigurationNode getPersistantData() { return persistant_data;}
	
	public Player getPlayer() {
		if (getUUID(false) != null)
			return Bukkit.getPlayer(getUUID());
		else if (!LogicUtil.nullOrEmpty(getPlayerName()))
			return Bukkit.getPlayerExact(getPlayerName());
		else
			throw new IllegalStateException("Player Object is completely playerless! No name info nor UUID info present!");
	}
	
	public final String getPlayerName(){
		return name;
	}
	
	public final ConfigurationNode getTempData() { return temp_data; }
	
	public int getVotes()
	{
		return persistant_data.get("vote-count", (int)0);
	}
	
	public boolean hasPermission(String permNode)
	{
		if (getPlayer() != null)
			return permHandler.hasPermission(getPlayer(), permNode);
		else if (VaultAdapter.isPermissionsLoaded())
			return VaultAdapter.permission.has(getLastWorld(), getPlayerName(), permNode);
		else
			return false;
	}
	
	public boolean hasPermissions(String... permissions)
	{
		for (String perm : permissions)
			if (!hasPermission(perm))
				return false;
		return true;
	}
	
	public void incrementVote() {
		setVotes(getVotes() + 1);
	}
	
	public boolean isCooldownActive(String name)
	{
		if (cd_cache.containsKey(name))
			return cd_cache.get(name).expired();
		else
			return false;
	}
	
	public boolean isCooldownExpired(String name)
	{
		if (cd_cache.containsKey(name))
			return cd_cache.get(name).expired();
		else
			return true;
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	public synchronized void load() {
		load(true);
	}
	
	public synchronized void load(boolean overwrite) {
		if (overwrite)
			cds = persistant_data.getList("cooldowns", CoolDown.class);
		else
			cds.addAll(persistant_data.getList("cooldowns", CoolDown.class));
		
		if (getFirstJoin() == 0)
			setFirstJoin();
		
		setName(persistant_data.get("last.ign", String.class));
		
		rebuild_cache();
	}

	public final void setName(String name) {
		this.name = name;
	}

	private void rebuild_cache() {
		cd_cache.clear();
		for (CoolDown cd: cds)
			cd_cache.put(cd.getName(), cd);
	}
	
	public void removeCooldDown(String name)
	{
		if (cd_cache.containsKey(name))
		{
			cds.remove(cd_cache.get(name));
			cd_cache.remove(name);
		}
	}

	public void save() {
		getUUID();
		saveLastLocation();
		persistant_data.set("cooldowns", getCoolDowns());
		persistant_data.set("last.ign", getPlayerName());
	}
	
	public void saveLastLocation(){
		if (getPlayer() != null)
			persistant_data.set("last.location", new SafeLocation(getPlayer().getLocation()));
	}
	
	public void saveLastLocation(Player player){
		if (!player.getUniqueId().equals(getUUID()))
			throw new IllegalArgumentException("Must be the same player as object holder");
		
		persistant_data.set("last.location", new SafeLocation(player.getLocation()));
	}
	
	public void setFirstJoin() {
		setFirstJoin(getOfflinePlayer().getFirstPlayed());
	}
	
	public void setFirstJoin(long value)
	{
		this.persistant_data.set("first.join", value);
	}
	
	public void setLastDeath(PlayerDeathEvent event)
	{
		this.persistant_data.remove("last.death");
		setLastDeathTS();
		EntityDamageEvent ede = event.getEntity().getLastDamageCause();
		
		if (ede == null)
			return;
		
		this.persistant_data.set("last.death.cause.damage", ede.getDamage());
		this.persistant_data.set("last.death.cause.type", ede.getCause().name());
		
		if (ede instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbe = (EntityDamageByEntityEvent) ede;
			
			Entity damager = edbe.getDamager();
			Block bDamager = null;
			if (!(damager instanceof Projectile))
				this.persistant_data.set("last.death.cause.entity.type", damager.getType());
			else if (((Projectile)damager).getShooter() instanceof Entity)
				damager = (Entity) ((Projectile)damager).getShooter(); //TODO: add projectile info?
			else {
				bDamager = ((BlockProjectileSource)((Projectile)damager).getShooter()).getBlock();
				damager = null; //TODO: Hook into block logging and retrieve builder.
			}
			
			if (damager instanceof Player) {
				Player d = (Player)damager;
				this.persistant_data.set("last.death.cause.entity.name", d.getName());
				manager.getPlayer(d).setLastKill(getPlayer());
			} else if (damager instanceof LivingEntity)
				this.persistant_data.set("last.death.cause.entity.name", ((LivingEntity)damager).getCustomName());
			else if (bDamager != null) {
				this.persistant_data.set("last.death.cause.block.name", bDamager.getType().name());
			} else {
				this.persistant_data.set("last.death.cause.entity", "UNTRACKED");
			}
		} else if (ede instanceof EntityDamageByBlockEvent) {
			EntityDamageByBlockEvent edbb = (EntityDamageByBlockEvent) ede;
			
			try {
				this.persistant_data.set("last.death.cause.block.type", edbb.getDamager().getType().name());
				
			} catch (NullPointerException e) {}
		}
	}
	
	public void setLastDeathLocation(Location loc)
	{
		this.persistant_data.set("last.death.location", new SafeLocation(loc));
	}
	
	public void setLastDeathTS()
	{
		setLastDeathTS((NoxCore.isUsingNanoTime()?System.nanoTime(): System.currentTimeMillis()));
	}
		
	public void setLastDeathTS(long stamp)
	{
		this.persistant_data.set("last.death.timestamp", stamp);
	}

	public void setLastKill(Entity entity) {
		this.persistant_data.remove("last.kill");
		
		setLastKillTS();
		this.persistant_data.set("last.kill.entity.type", entity.getType());
		if (entity instanceof Player)
			this.persistant_data.set("last.kill.entity.name", ((Player)entity).getName());
		else if (entity instanceof LivingEntity)
			this.persistant_data.set("last.kill.entity.name", ((LivingEntity)entity).getCustomName());
		else
			this.persistant_data.set("last.kill.entity.name", "UNTRACKED");
		
	}
	
	public void setLastKillTS() { setLastKillTS((NoxCore.isUsingNanoTime()?System.nanoTime(): System.currentTimeMillis())); }
	
	public void setLastKillTS(long stamp) {
		this.persistant_data.set("last.kill.timestamp", stamp);
	}

	public void setVotes(int amount)
	{
		persistant_data.set("vote-count", amount);
	}

	public void saveToManager() {
		PlayerManager.getInstance().savePlayer(this);
	}
}
