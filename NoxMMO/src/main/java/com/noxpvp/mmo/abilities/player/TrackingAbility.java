package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
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

public class TrackingAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Tracking";
	private boolean isInvis;
	private boolean hasSpeed;
	private Player it;
	private int radius;
	private int duration;
	private int effectFreq;
	private Effect EffectType;

	
	public boolean isInvis() {return isInvis;}
	public TrackingAbility setInvis(boolean isInvis) {this.isInvis = isInvis; return this;}
	
	public boolean isHasSpeed() {return hasSpeed;}
	public TrackingAbility setHasSpeed(boolean hasSpeed) {this.hasSpeed = hasSpeed; return this;}
	
	public int getRadius() {return radius;}
	public TrackingAbility setRadius(int radius) {this.radius = radius; return this;}

	public int getDuration() {return duration;}
	public TrackingAbility setDuration(int duration) {this.duration = duration; return this;}
	
	public int getEffectFreq() {return effectFreq;}
	public TrackingAbility setEffectFreq(int effectFreq) {this.effectFreq = effectFreq; return this;}

	public Effect getEffectType() {return EffectType;}
	public TrackingAbility setEffectType(Effect effectType) {EffectType = effectType; return this;}
	
	public TrackingAbility(Player player){
		super(ABILITY_NAME, player);
	}

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
		
		Runnable effect = new EffectRunnable(tracker, EffectType, (duration / effectFreq), 0);
		Bukkit.getScheduler().runTaskTimer(NoxMMO.getInstance(), effect, 0, effectFreq);
		
		Runnable despawn = new DespawnRunnable(tracker);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), despawn, (duration));
		
		return false;
	}

	public boolean mayExecute() {
		return getPlayer() != null;
	}
	

}