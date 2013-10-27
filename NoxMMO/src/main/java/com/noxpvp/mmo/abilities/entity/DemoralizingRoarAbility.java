package com.noxpvp.mmo.abilities.entity;

import java.util.HashSet;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class DemoralizingRoarAbility extends BaseEntityAbility{
	
	private final static String ABILITY_NAME = "Demoralizing Roar";
	private int range;
	private HashSet<Creature> creatures = null;
	
	public int getRange() {return range;}
	public DemoralizingRoarAbility setRange(int range) {this.range = range; return this;}
	
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
	
	public boolean mayExecute() {
		return getEntity() != null;
	}
	
}
