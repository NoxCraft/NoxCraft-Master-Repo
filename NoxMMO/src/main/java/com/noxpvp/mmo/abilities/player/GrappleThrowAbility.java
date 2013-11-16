package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

public class GrappleThrowAbility extends BasePlayerAbility{
	
	public final static String ABILITY_NAME = "Grapple Throw";
	public final static String PERM_NODE = "grapplethrow";
	
	private int range;
	private int maxTargets;
	private int pushDelay = 16;
	
	/**
	 * 
	 * @return Integer The currently set range for this ability instance
	 */
	public int getRange() {return range;}

	/**
	 * 
	 * @param range Integer range to look for target in this ability instance
	 * @return GrappleThrowAbility This instance, used for chaining
	 */
	public GrappleThrowAbility setRange(int range) {this.range = range; return this;}

	/**
	 * 
	 * @return Integer The max amount of targets this ability can find
	 */
	public int getMaxTargets() {return maxTargets;}

	/**
	 * 
	 * @param maxTargets Integer amount of targets this ability is allowed to target at max
	 * @return GrappleThrowAbility This instance, used for chaining
	 */
	public GrappleThrowAbility setMaxTargets(int maxTargets) {this.maxTargets = maxTargets; return this;}

	/**
	 * 
	 * @return Integer The currently set tick delay before the targets a thrown/shock wave runs
	 */
	public int getPushDelay() {return pushDelay;}

	/**
	 * 
	 * @param pushDelay Integer amount of ticks to wait before throwing the target(s) and executing the shockwave
	 * @return GrappleThrowAbility This instance, used for chaining
	 */
	public GrappleThrowAbility setPushDelay(int pushDelay) {this.pushDelay = pushDelay; return this;}

	/**
	 * 
	 * @param player The Player type user object for this ability instance
	 */
	public GrappleThrowAbility(Player player){super(ABILITY_NAME, player);}
	
	/**
	 * 
	 * @return boolean If the ability was executed successfully
	 */
	public boolean execute(){
		if (!mayExecute())
			return false;

		Player p = getPlayer();
		final Location pLoc = p.getLocation();
		
		int i = 0;
		for (Entity it : p.getNearbyEntities(range, range, range)){
			if (i >= maxTargets) break;
			if (!(it instanceof Player)) continue;;
			if (!(p.hasLineOfSight(it))) continue;;
			
			i++;
			final Entity e = it;
			final Location itLoc = it.getLocation();
			
			Vector velo = (pLoc.toVector().subtract(itLoc.toVector()).multiply(0.4));
			e.setVelocity(velo);
			
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				public void run() {
					Location pLoc = getPlayer().getLocation();
					e.teleport(pLoc);
					e.setVelocity(pLoc.getDirection().setY(0.45).multiply(1.6));
			}}, pushDelay);
		}
		if (i > 0){
			new ExpandingDamageRunnable(p, 1, range).runTaskTimer(NoxMMO.getInstance(), pushDelay, 2);
			new ShockWaveAnimation(pLoc, 2, range, 0.30, true, pushDelay);
			return true;
		}
		else return false;
	}
	
	/**
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute(){
		return getPlayer() != null;
	}

}
