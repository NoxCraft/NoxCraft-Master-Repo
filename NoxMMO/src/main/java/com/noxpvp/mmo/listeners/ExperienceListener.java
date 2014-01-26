package com.noxpvp.mmo.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import com.bergerkiller.bukkit.common.config.FileConfiguration;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.listeners.NoxListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.PlayerClass;

public class ExperienceListener extends NoxListener<NoxMMO> {
	
	public static enum ExperienceType {
		ENTITY_KILL,
		ORE_MINE,
		FURNACE_SMELT,
		EXCAVATION;
		
	}
	
	private final PlayerManager pm;
	
	private final FileConfiguration expConfig;
	private Map<String, Double> cachedMultipliers;
	
	private final int killExp;
//	private final int miningExp;
//	private final int excavationExp;
//	private final int smeltingExp;
	
	public ExperienceListener() {
		this(NoxMMO.getInstance());
	}
	
	public ExperienceListener(NoxMMO mmo) {
		super(mmo);
		
		this.pm = mmo.getPlayerManager();
		
		this.expConfig = mmo.getExperienceConfig();
		this.expConfig.load();
		
		this.killExp = expConfig.get("amounts.base.kill", 100);
//		this.miningExp = expConfig.get("amounts.base.mining", 100);
//		this.excavationExp = expConfig.get("amounts.base.excavation", 100);
//		this.smeltingExp = expConfig.get("amounts.base.smelting", 100);
		
		this.cachedMultipliers = this.initializePresets();
		
		this.expConfig.save();
	}
	
	private Map<String, Double> initializePresets() {
		Map<String, Double> cache = new HashMap<String, Double>();
		List<String> defs = new ArrayList<String>();
		
		Class<LivingEntity> living = LivingEntity.class;
		Class<Tameable> tamable = Tameable.class;
		
		/*
		 * Entity Killing / Taming
		 */
		for (EntityType type : EntityType.values()) {
			if (living.isAssignableFrom(type.getEntityClass()))
				defs.add("multipliers.kills." + type.name());
			else if (tamable.isAssignableFrom(type.getEntityClass()))
				defs.add("multipliers.taming." + type.name());
				
		}
		
		/*
		 * Mining
		 */
		defs.add("multipliers.mining." + Material.DIAMOND_ORE.name());
		defs.add("multipliers.mining." + Material.EMERALD_ORE.name());
		defs.add("multipliers.mining." + Material.GOLD_ORE.name());
		defs.add("multipliers.mining." + Material.QUARTZ_ORE.name());
		defs.add("multipliers.mining." + Material.IRON_ORE.name());
		defs.add("multipliers.mining." + Material.LAPIS_ORE.name());
		defs.add("multipliers.mining." + Material.REDSTONE_ORE.name());
		defs.add("multipliers.mining." + Material.COAL_ORE.name());
		
		/*
		 * Excavation 
		 */
		defs.add("multipliers.excavation." + Material.DIRT.name());
		defs.add("multipliers.excavation." + Material.GRASS.name());
		defs.add("multipliers.excavation." + Material.SAND.name());
		defs.add("multipliers.excavation." + Material.GRAVEL.name());
		defs.add("multipliers.excavation." + Material.CLAY.name());
		defs.add("multipliers.excavation." + Material.NETHERRACK.name());
		
		/*
		 * Smelting
		 */
		defs.add("multipliers.smelting." + Material.DIAMOND.name());
		defs.add("multipliers.smelting." + Material.EMERALD.name());
		defs.add("multipliers.smelting." + Material.GOLD_INGOT.name());
		defs.add("multipliers.smelting." + Material.IRON_INGOT.name());
		defs.add("multipliers.smelting." + Material.QUARTZ.name());
		defs.add("multipliers.smelting." + Material.INK_SACK.name());
		defs.add("multipliers.smelting." + Material.REDSTONE.name());
		defs.add("multipliers.smelting." + Material.HARD_CLAY.name());
		defs.add("multipliers.smelting." + Material.BRICK.name());
		defs.add("multipliers.smelting." + Material.GLASS.name());
		defs.add("multipliers.smelting." + Material.COAL.name());
		defs.add("multipliers.smelting." + Material.NETHER_BRICK.name());
		defs.add("multipliers.smelting." + Material.STONE.name());
		
		for (String value : defs)
			cache.put(value, expConfig.get("amounts." + value, 1.0));
		
		return cache;
	}
	
	public int getExpBoost(Player player) {
		if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.boost.5"))
			return 4;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.boost.4"))
			return 3;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.boost.3"))
			return 2;
		else if (VaultAdapter.permission.has(player, NoxMMO.PERM_NODE + ".exp.boost.2"))
			return 1;
		else return 0;
	}
	
	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onKill(EntityDeathEvent event) {
		LivingEntity killed = event.getEntity();
		
		EntityDamageByEntityEvent last = (EntityDamageByEntityEvent) ((killed.getLastDamageCause() instanceof EntityDamageByEntityEvent)? killed.getLastDamageCause() : null);
		if (last == null) return;
		
		Player attacker = (Player) (last.getDamager() instanceof Player? last.getDamager() : null);
		if (attacker == null) return;
		
		PlayerClass clazz = pm.getMMOPlayer(attacker).getMainPlayerClass();
		if (clazz == null || !clazz.canGainExp(experienceType.ENTITY_KILL)) return;
		
		double multiplier = cachedMultipliers.get(killed.getType().name());
		
		clazz.addExp((int) (killExp * multiplier));
		
	}

//	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
//	public void onBreak(BlockBreakEvent event) {
//		Player breaker = event.getPlayer();
//		
//		PlayerClass clazz = pm.getMMOPlayer(breaker).getMainPlayerClass();
//		if (clazz == null) return;
//		
//		double xpMulti = cachedMultipliers.get(event.getBlock().getType().name());
//		if (xpMulti != 0)
//			xpMulti =+ getExpBoost(breaker);
//		
//		clazz.addExp((int) (miningExp * xpMulti));
//		
//	}

//	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
//	public void onSmelting(FurnaceExtractEvent event) {
//		
//	}
	
//	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
//	public void onTame(EntityTameEvent event) {
//		
//	}
	
}
