package com.noxpvp.mmo.abilities.entity;

import java.util.HashSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 *
 */
public class DemoralizingRoarAbility extends BaseEntityAbility{
	
	private static final String ABILITY_NAME = "Demoralizing Roar";
	public static final String PERM_NODE = "demoralizing-roar";
	
	private int range;
	private HashSet<Creature> creatures = null;
	
	/**
	 * 
	 * 
	 * @return Integer Currently set range to look for targets
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range Integer range to look that this ability should look for targets
	 * @return DemoralizingRoarAbility This instance, used for chaining
	 */
	public DemoralizingRoarAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @param entity Entity type user of this ability (Usually a wolf)
	 */
	public DemoralizingRoarAbility(Entity entity){
		super(ABILITY_NAME, entity);
		creatures = new HashSet<Creature>();
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Entity e = getEntity();
		
		for (Entity it : e.getNearbyEntities(range, range, range)){
			if (!(it instanceof Creature || creatures.contains(it))) continue;
			
			for (Entity itIt : it.getNearbyEntities(range, range, range)){
				if (!(itIt instanceof Creature || creatures.contains(it))) continue;
				
				((Creature) it).setTarget((Creature) itIt);
				((Creature) itIt).setTarget((Creature) it);
				
				creatures.add((Creature) it);
				creatures.add((Creature) itIt);
				break;
			}
		}
		if (creatures.isEmpty())
			return false;
		
		return true;
	}
	
}
