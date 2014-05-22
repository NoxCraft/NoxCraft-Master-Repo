package com.noxpvp.mmo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.EntityUtil;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.noxpvp.core.Persistant;
import com.noxpvp.core.annotation.Temporary;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.abilities.IPlayerAbility;
import com.noxpvp.mmo.abilities.targeted.TargetAbility;
import com.noxpvp.mmo.classes.internal.DummyClass;
import com.noxpvp.mmo.classes.internal.ExperienceType;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {
	
	private static final String PRIMARY_CLASS_NODE = "current.class.primary";
	private static final String SECONDARY_CLASS_NODE = "current.class.secondary";
	private static final String TARGET_NODE = "current.target";
	private IPlayerClass primaryClass = DummyClass.PRIMARY, secondaryClass = DummyClass.SECONDARY;
	private LivingEntity target;
	
	public MMOPlayer(OfflinePlayer player)
	{
		super(player);
	}
	
	public MMOPlayer(String name)
	{
		super(name);
	}

	public MMOPlayer(NoxPlayerAdapter player)
	{
		super(player);
	}
	
	@Temporary
	public List<Ability> getAllAbilities() {
		
		List<Ability> ret = new ArrayList<Ability>();
		if (getPrimaryClass() != null)
			ret.addAll(getPrimaryClass().getAbilities());
		if (getSecondaryClass() != null)
			ret.addAll(getSecondaryClass().getAbilities());
		
		return Collections.unmodifiableList(ret);
	}
	
	@Temporary
	public Map<String, Ability> getAllMappedAbilities() {
		Map<String, Ability> ret = new HashMap<String, Ability>();
		
		if (getPrimaryClass() != null)
			ret.putAll(getPrimaryClass().getAbilityMap());
		if (getSecondaryClass() != null)
			ret.putAll(getSecondaryClass().getAbilityMap());
		
		return Collections.unmodifiableMap(ret);
	}
	
	public IPlayerClass getPrimaryClass() {
		return primaryClass;
	}
	
	public IPlayerClass getSecondaryClass() {
		return secondaryClass;
	}
	
	public void setClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getName()));
	}
	
	public void setClass(IPlayerClass c) {
		if (c.isPrimaryClass())
			setPrimaryClass(c);
		else
			setSecondaryClass(c);
	}
	
	public void setPrimaryClass(String c) {
		if (LogicUtil.nullOrEmpty(c))
			setClass(DummyClass.PRIMARY);
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		PlayerClass clazz;
		if ((clazz = PlayerClassUtil.safeConstructClass(c, getName())) != null)
			setClass(clazz);
		
		return;
	}
	
	public void setPrimaryClass(IPlayerClass c) {
		this.primaryClass = c;
		getPlayer().setMaxHealth(primaryClass.getTier().getMaxHealth());
	}
	
	public void setSecondaryClass(String c) {
		if (LogicUtil.nullOrEmpty(c))
			setClass(DummyClass.SECONDARY);
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		PlayerClass clazz;
		if ((clazz = PlayerClassUtil.safeConstructClass(c, getName())) != null)
			setClass(clazz);
		
		return;
	}
	
	public void setSecondaryClass(IPlayerClass c) {
		this.secondaryClass = c;
	}
	
	public LivingEntity getTarget() {
		if (target == null)
			new TargetAbility(getPlayer()).execute();
		
		return target;
	}
	
	public void setTarget(LivingEntity entity) {
		this.target = entity;
	}
	
	public IPlayerAbility[] getPlayerAbilities() {
		throw new NotImplementedException();
	}
	
	public IPassiveAbility<?>[] getPassiveAbilities() {
		throw new NotImplementedException();
	}
	
	public void addExp(ExperienceType type, int amount) {
		throw new NotImplementedException();
		
		/*
		 * If successful, do this
		 */
//		Player p = getPlayer();
//		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, 0);
	}
	
	public void load() {
		ConfigurationNode node = getPersistantData();
		setPrimaryClass(node.get(PRIMARY_CLASS_NODE, ""));
		setSecondaryClass(node.get(SECONDARY_CLASS_NODE, ""));
		
		/*
		 * Seperate the scope Just in case we want to use the variables again.
		 */
		{
			String uid = node.get(TARGET_NODE + ".uuid", String.class);
			String worldName = node.get(TARGET_NODE + ".world", String.class);
			
			// check for possible null world name before getting world, as bukkit will freak out
			World world = worldName == null? null : Bukkit.getWorld(worldName);
			
			if (!LogicUtil.nullOrEmpty(uid) && world != null)
				setTarget(EntityUtil.getEntity(world, UUID.fromString(uid), LivingEntity.class));
		}
	}
	
	/**
	 * Warning this does not save to file. You must call saveToManager() method after using this method.
	 * DO NOT USE saveToManager() in this method. It will infinite loop. Or you can use saveToManager() without this... It will work just fine.
	 */
	public void save() {
		ConfigurationNode node = getPersistantData();
		if (getPrimaryClass() == null)
			node.remove(PRIMARY_CLASS_NODE);
		else
			node.set(PRIMARY_CLASS_NODE, getPrimaryClass().getUniqueID());
		
		if (getSecondaryClass() == null)
			node.remove(SECONDARY_CLASS_NODE);
		else
			node.set(SECONDARY_CLASS_NODE, getSecondaryClass().getUniqueID());
		
		if (getTarget() == null)
			node.remove(TARGET_NODE);
		else {
			node.set(TARGET_NODE + ".world", getTarget().getWorld().getName());
			node.set(TARGET_NODE + ".uuid", getTarget().getUniqueId().toString());
		}
	}
}
