package com.noxpvp.mmo.abilities.player;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import com.noxpvp.mmo.runnables.EffectRunnable;

/**
 * @author NoxPVP
 *
 */
public class TrackingAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "track";
	private final static String ABILITY_NAME = "Tracking";
	private boolean isInvis;
	private boolean hasSpeed;
	private int speedAmp;
	private Player it;
	private int radius;
	private int duration;
	private int effectFreq;
	private Effect EffectType;

	/**
	 * 
	 * 
	 * @return Boolean - If tracker will be invisible
	 */
	public boolean isInvis() {return isInvis;}
	
	/**
	 * 
	 * 
	 * @param isInvis - Boolean if tracker should be invisible
	 * @return TrackingAbility - This instance used for chaining
	 */
	public TrackingAbility setInvis(boolean isInvis) {this.isInvis = isInvis; return this;}
	
	/**
	 * 
	 * 
	 * @return Boolean - If tracker will have speed effect applied
	 */
	public boolean isHasSpeed() {return hasSpeed;}
	
	/**
	 * 
	 * 
	 * @param hasSpeed - Boolean if tracker should have speed effect applied
	 * @return TrackingAbility - This instance used for chaining
	 */
	public TrackingAbility setHasSpeed(boolean hasSpeed) {this.hasSpeed = hasSpeed; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer - The amplifier set for speed effect used on tracker (Returns null if setSpeedAmp has not been used)
	 */
	public int getSpeedAmp() {return speedAmp;}
	
	/**
	 * 
	 * 
	 * @param speedAmp - Integer amplifier that will be used for trackers speed effect
	 */
	public void setSpeedAmp(int speedAmp) {this.speedAmp = speedAmp;}
	
	/**
	 * 
	 * 
	 * @return Integer - Radius used to search to tracking targets
	 */
	public int getRadius() {return radius;}
	
	
	/**
	 * 
	 * 
	 * @param radius - Set the radius used when searching for tracker targets
	 * @return TrackingAbility - This instance used for chaining
	 */
	public TrackingAbility setRadius(int radius) {this.radius = radius; return this;}

	/**
	 * 
	 * 
	 * @return Integer - The current duration set for tracker (Returns null is setDuration has not been used)
	 */
	public int getDuration() {return duration;}
	
	/**
	 * 
	 * 
	 * @param duration - Integer ticks that the tracker should last
	 * @return TrackingAbility - This instance, used for chaining
	 */
	public TrackingAbility setDuration(int duration) {this.duration = duration; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer - The rate at which the trackers effect will be applied
	 */
	public int getEffectFreq() {return effectFreq;}
	
	/**
	 * 
	 * 
	 * @param effectFreq - Integer ticks that the trackers effect should be applied
	 * @return TrackingAbility - This instance, used fo chaining
	 */
	public TrackingAbility setEffectFreq(int effectFreq) {this.effectFreq = effectFreq; return this;}

	/**
	 * 
	 * 
	 * @return Effect - The current set effect type set for tracker (Returns null if setEffectType() has not been used)
	 */
	public Effect getEffectType() {return EffectType;}
	
	/**
	 * 
	 * 
	 * @param effectType - The Effect Type that should be applied to the target
	 * @return TrackingAbility - This instance, used for chaining
	 */
	public TrackingAbility setEffectType(Effect effectType) {EffectType = effectType; return this;}
	
	/**
	 * 
	 * 
	 * @param player - The player that this tracker should be spawned from/ability user
	 */
	public TrackingAbility(Player player){
		super(ABILITY_NAME, player);
	}

	/**
	 * 
	 * 
	 * @return Boolean - If execution has ended successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
					
		Player p = getPlayer();
		
		for (Entity it : p.getNearbyEntities(radius, radius, radius)){
			if (!(it instanceof Player))
				continue;
			
			this.it = (Player) it;
		}
		
		if (it == null)
			return false;
		
		Monster tracker = (Monster) p.getWorld().spawnEntity(p.getLocation(), EntityType.ZOMBIE);

		tracker.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (duration * effectFreq), 15));
		
		if (hasSpeed)
			tracker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, (duration * effectFreq), 1));
		if (isInvis)
			tracker.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, (duration * effectFreq), 1));
		
		tracker.setTarget(it);
		
		NoxMMO instance = NoxMMO.getInstance();
		
		EffectRunnable effect = new EffectRunnable(tracker, EffectType, (duration / effectFreq), 0);
		effect.runTaskTimer(instance, 0, effectFreq);
		
		DespawnRunnable despawn = new DespawnRunnable(tracker);
		despawn.runTaskLater(instance, duration);
		
		return false;
	}

	/**
	 * 
	 * 
	 * @return If the execute() will normally be able to start
	 */
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	

}