package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class CallOfTheWildAbility extends BasePlayerAbility{

	private static final String ABILITY_NAME = "Call Of the Wild";
	public static final String PERM_NODE = "wolf-call";
	
	private double range;
	private boolean cancelIfNearby;
	private EntityType spawnType;

	/**
	 * 
	 * @return double The currently set range to search for nearby entitys
	 */
	public double getRange() {return range;}

	/**
	 * 
	 * @param range the distance to search for nearby entitys
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildAbility setRange(double range) {this.range = range; return this;}

	/**
	 * 
	 * @return boolean If the ability is set to check for nearby entitys before executing
	 */
	public boolean isCancelIfNearby() {return cancelIfNearby;}

	/**
	 * 
	 * @param cancelIfNearby If the ability should check for nearby entitys before executing
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildAbility setCancelIfNearby(boolean cancelIfNearby) {this.cancelIfNearby = cancelIfNearby; return this;}

	/**
	 * 
	 * @return EntityType The type of entity set to spawn
	 */
	public EntityType getSpawnType() {return spawnType;}

	/**
	 * 
	 * @param spawnType The type of entity to spawn
	 * @return CallOfTheWildAbility This instance, used for chaining
	 */
	public CallOfTheWildAbility setSpawnType(EntityType spawnType) {this.spawnType = spawnType; return this;}

	public CallOfTheWildAbility(Player player) {
		super(ABILITY_NAME, player);
		this.cancelIfNearby = true;
		this.range = 50;
		this.spawnType = EntityType.WOLF;
	}

	/**
	 * 
	 * @return boolean If the ability has executed successfully
	 */
	public boolean execute() {
		if (!mayExecute()) return false;
		
		Player p = getPlayer();
		boolean canSpawn = true;
		
		if (cancelIfNearby){
			for (Entity it : p.getNearbyEntities(range, range, range)){
				if (!(it instanceof Wolf)) continue;
				
				canSpawn = false;
				break;
			}
		}
		if (!canSpawn) return false;
		
		Entity e = p.getWorld().spawnEntity(p.getLocation(), spawnType);
		
		if (e instanceof Tameable)
			((Tameable) e).setOwner(p);
		
		return true;
	}

	/**
	 * 
	 * @return boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
}
