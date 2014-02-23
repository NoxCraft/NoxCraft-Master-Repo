package com.noxpvp.mmo.abilities.entity;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.runnables.EffectsRunnable;

/**
 * @author NoxPVP
 *
 */
public class DemoralizingRoarAbility extends BaseEntityAbility{
	
	public static final String ABILITY_NAME = "Demoralizing Roar";
	public static final String PERM_NODE = "demoralizing-roar";
	
	private HashSet<Creature> creatures = null;
	private int range;
	
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
			
			for (Entity itTwo : it.getNearbyEntities(range, range, range)){
				if (!(itTwo instanceof Creature || creatures.contains(it))) continue;
				
				((Creature) it).setTarget((Creature) itTwo);
				((Creature) itTwo).setTarget((Creature) it);
				
				
				new Location(itTwo.getWorld(),
						itTwo.getLocation().getX(),
						itTwo.getLocation().getY()+1.75,
						itTwo.getLocation().getZ());
				
				EffectsRunnable ef1 = new EffectsRunnable(Arrays.asList("angryVillager"), false, it.getLocation(), 0, 1, 1, null),
				ef2 = new EffectsRunnable(Arrays.asList("angryVillager"), false, itTwo.getLocation(), 0, 1, 1, null);
				
				ef1.runTask(NoxMMO.getInstance());
				ef2.runTask(NoxMMO.getInstance());

				
				creatures.add((Creature) it);
				creatures.add((Creature) itTwo);
				break;
			}
		}
		if (creatures.isEmpty())
			return false;
		
		return true;
	}
	
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
	
}
