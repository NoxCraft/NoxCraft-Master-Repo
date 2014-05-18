package com.noxpvp.mmo.abilities.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;

public class MaliciousBiteAbility extends BaseEntityAbility implements PassiveAbility<EntityDamageByEntityEvent>,  PVPAbility{
	
	public static final String ABILITY_NAME = "Malicious bite";
	public static final String PERM_NODE = "malicious-bite";

	public MaliciousBiteAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return false;
		
		if (!(getEntity() instanceof Tameable)) return false;
		
		AnimalTamer a = ((Tameable) getEntity()).getOwner();
		
		if (a == null || !(a instanceof Player)) return false;
		
		Player o = (Player) a;
		
		IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(o).getPrimaryClass();
		
		return RandomUtils.nextFloat() < (pClass.getCurrentTierLevel() * pClass.getLevel()) / 1000;
	}
	
	public boolean execute() {
			return true;
	}

}
