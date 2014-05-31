/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo;

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
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.abilities.PlayerAbility;
import com.noxpvp.mmo.classes.ExperienceType;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {
	
	private static final String PRIMARY_CLASS_NODE = "current.class.primary";
	private static final String SECONDARY_CLASS_NODE = "current.class.secondary";
	private static final String TARGET_NODE = "current.target";
	private  PlayerClass primaryClass, secondaryClass;
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
	
	public PlayerClass getPrimaryClass() {
		return primaryClass;
	}
	
	public PlayerClass getSecondaryClass() {
		return secondaryClass;
	}
	
	public void setClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setClass(PlayerClass c) {
		if (c.isPrimaryClass())
			setPrimaryClass(c);
		else
			setSecondaryClass(c);
	}
	
	public void setPrimaryClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setPrimaryClass(PlayerClass c) {
		this.primaryClass = c;
	}
	
	public void setSecondaryClass(String c) {
		if (!PlayerClassUtil.hasClassId(c) && PlayerClassUtil.hasClassName(c))
			c = PlayerClassUtil.getIdByClassName(c);
		
		setClass(PlayerClassUtil.safeConstructClass(c, getPlayerName()));
	}
	
	public void setSecondaryClass(PlayerClass c) {
		this.secondaryClass = c;
	}
	
	public LivingEntity getTarget() {
		return target;
	}
	
	public void setTarget(LivingEntity entity) {
		this.target = entity;
	}
	
	public PlayerAbility[] getPlayerAbilities() {
		throw new NotImplementedException();
	}
	
	public PassiveAbility<?>[] getPassiveAbilities() {
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
		superLoad();
		
		ConfigurationNode node = getPersistantData();
		setPrimaryClass(node.get(PRIMARY_CLASS_NODE, ""));
		setSecondaryClass(node.get(SECONDARY_CLASS_NODE, ""));
		
		/*
		 * Seperate the scope Just in case we want to use the variables again.
		 */
		{
			String uid = node.get(TARGET_NODE + ".uuid", String.class);
			World world = Bukkit.getWorld(node.get(TARGET_NODE + ".world", String.class));
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
			node.set(TARGET_NODE + "uuid", getTarget().getUniqueId().toString());
		}
		
		superSave();
	}
}
