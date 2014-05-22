package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.core.utils.PlayerUtils.LineOfSightUtil;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.runnables.ExpandingDamageRunnable;
import com.noxpvp.mmo.runnables.ShockWaveAnimation;

public class ThrowAbility extends BasePlayerAbility implements IPVPAbility {
	
	public static final String ABILITY_NAME = "Grapple Throw";
	public static final String PERM_NODE = "grapple-throw";
	
	@Override
	public String getDescription() {
		return "You grab all nearby enemys within " + String.format("%.2f", getRange()) + " blocks and throw them high into the air";
	}
	
	private int range;
	private int maxTargets;
	private int pushDelay;
	
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
	public ThrowAbility setRange(int range) {this.range = range; return this;}

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
	public ThrowAbility setMaxTargets(int maxTargets) {this.maxTargets = maxTargets; return this;}

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
	public ThrowAbility setPushDelay(int pushDelay) {this.pushDelay = pushDelay; return this;}

	/**
	 * 
	 * @param player The Player type user object for this ability instance
	 */
	public ThrowAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.maxTargets = 1;
		this.pushDelay = 20;
		this.range = 8;
	}
	
	public boolean execute(){
		if (!mayExecute())
			return false;

		Player p = getPlayer();
		final Location pLoc = p.getLocation();
		
		int i = 0;
		for (Entity it : p.getNearbyEntities(range, range, range)){
			if (i >= maxTargets) break;
			
			if (!(it instanceof LivingEntity) || it == p)
				continue;
			
			if (!LineOfSightUtil.hasLineOfSight(p, it.getLocation(), Material.AIR))
				continue;
			
			i++;
			final Damageable e = (Damageable) it;
			final Location itLoc = it.getLocation();
			
			e.setVelocity( (pLoc.toVector().subtract(itLoc.toVector()).multiply(0.4)) );
			
			Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {
				public void run() {
					Location pLoc = getPlayer().getLocation();
					e.teleport(pLoc);
					e.setVelocity(pLoc.getDirection().multiply(3).setY(1));
			}}, pushDelay);
		}
		
		if (i > 0){
			new ExpandingDamageRunnable(p, pLoc, 4, range, 2).start(pushDelay);
			new ShockWaveAnimation(pLoc, 2, range, true).start(pushDelay);
			return true;
		} else return false;
	}

}
