package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.PVPAbility;

public class ForcePullAbility extends BaseEntityAbility implements PVPAbility {
	
	public static final String ABILITY_NAME = "Force Pull";
	public static final String PERM_NODE = "force-pull";
	
	private double range;
	private int maxTargets;
	
	public double getRange() { return range; }

	public ForcePullAbility setRange(double range) {this.range = range; return this; }

	public int getMaxTargets() { return maxTargets; }

	public ForcePullAbility setMaxTargets(int maxTargets) { this.maxTargets = maxTargets; return this; }

	public ForcePullAbility(Entity ent)
	{
		super(ABILITY_NAME, ent);
		
		this.range = 8;
		this.maxTargets = 4;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		
		Location eLoc = e.getLocation();
		int i = 0;
		for (Entity it : e.getNearbyEntities(range, range, range)) {
			if (!(it instanceof LivingEntity))
				continue;
			
			it.setVelocity(eLoc.subtract(it.getLocation()).toVector());
			
			if (++i >= maxTargets)
				break;
		}
		return true;
	}

}
