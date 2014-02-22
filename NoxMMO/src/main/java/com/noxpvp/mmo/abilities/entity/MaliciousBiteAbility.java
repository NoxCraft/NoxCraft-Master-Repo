package com.noxpvp.mmo.abilities.entity;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class MaliciousBiteAbility extends BaseEntityAbility implements PassiveAbility<EntityDamageByEntityEvent> {
	
	public static final String ABILITY_NAME = "Malicious bite";
	public static final String PERM_NODE = "malicious-bite";
	
//	private static final float damageMultiplier = 2.0f; //What is this?

	public MaliciousBiteAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		// TODO Auto-generated method stub - BBC FINISH THIS
		if (!mayExecute())
			return false;
		
		if (!(getEntity() instanceof Tameable)) return false;
		
		AnimalTamer a = ((Tameable) getEntity()).getOwner();
		
		if (a == null || !(a instanceof Player)) return false;
		
		Player o = (Player) a;
		
		PlayerClass pClass = NoxMMO.getInstance().getPlayerManager().getPlayer(o).getPrimaryClass();
		
		return RandomUtils.nextFloat() < (pClass.getCurrentTierLevel() * pClass.getLevel()) / 1000;
	}
	
	public boolean execute() {
//		if (!mayExecute())
			return false;
//		
//		if (!(getEntity() instanceof Tameable)) return false;
//		
//		AnimalTamer a = ((Tameable) getEntity()).getOwner();
//		
//		if (a == null || !(a instanceof Player)) return false;
//		
//		Player o = (Player) a;
//		
//		PlayerClass pClass = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(o).getMainPlayerClass();
//		
//		return RandomUtils.nextFloat() < (pClass.getTierLevel() * pClass.getLevel()) / 1000;
	}

}
