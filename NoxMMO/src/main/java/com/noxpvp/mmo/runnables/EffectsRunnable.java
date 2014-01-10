package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutWorldParticles;
import com.bergerkiller.bukkit.common.utils.PacketUtil;

/**
 * @author NoxPVP
 *
 */
public class EffectsRunnable extends BukkitRunnable{
	
	private Location loc;
	private Location locOffSet;
	private float data;
	private int amount;
	private int amt;
	private List<String> name = new ArrayList<>();
	
	private boolean runMulti = false;
	private boolean continuous = false;
	
	private boolean usingTracker = false;
	private Entity tracker;
	
	private int runs = 0;
	
	/**
	 * 
	 * @param List<String> The effect name(s) | List - https://gist.github.com/thinkofdeath/5110835
	 * @param loc The particle location
	 * @param data
	 * @param amount The amount of particles
	 */
	public EffectsRunnable(List<String> name, Location loc, float data, int amount, boolean runMultipleTimes, boolean continuous, Entity tracker) {
		this.name = name;
		this.loc = loc;
		this.data = data;
		this.amount = amount;
		this.amt = runMultipleTimes? 1 : amount;
		this.runs = 0;
		
		this.runMulti = runMultipleTimes;
		this.continuous = continuous;
		
		this.usingTracker = tracker != null;
		this.tracker = usingTracker? tracker : null;
		
	}
	
	/**
	 * 
	 * @param List<String> The effect name(s) | List - https://gist.github.com/thinkofdeath/5110835
	 * @param loc The particle location
	 * @param data
	 * @param amount The amount of particles
	 */
	public EffectsRunnable(String stringName, Location loc, float data, int amount, boolean runMultipleTimes, boolean continuous, Entity tracker) {
		this.name.add(stringName);
		this.loc = (loc != null)? loc : null;
		this.data = data;
		this.amount = amount;
		this.amt = runMultipleTimes? 1 : amount;
		this.runs = 0;
		
		this.runMulti = runMultipleTimes;
		this.continuous = continuous;
		
		this.usingTracker = tracker != null;
		this.tracker = usingTracker? tracker : null;
		
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	public void run(){
			if (runMulti) {
				if (continuous && runs >= name.size())
					runs = 0;
				else if (!continuous && (runs > amount || runs >= name.size())) {
					safeCancel();
					return;
				}
					
			} else if (runs != 0) {
				Bukkit.broadcastMessage("runs over limit, stopping");
				safeCancel();
				return;
			}
			
			if (usingTracker && (tracker.isDead() || tracker == null || !tracker.isValid())) {
				Bukkit.broadcastMessage("tracker in valid");
				safeCancel();
				return;
			} else if (usingTracker) {
				loc = tracker.getLocation();
			}
			
			try {
				CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
				NMSPacketPlayOutWorldParticles effect = new NMSPacketPlayOutWorldParticles();
				
				Bukkit.broadcastMessage("editing data");
				commonEffect.write(effect.effectName, name.get(runs++));
				commonEffect.write(effect.particleCount, amt);
				commonEffect.write(effect.speed, data);
				
				commonEffect.write(effect.x, (float) loc.getX());
				commonEffect.write(effect.y, (float) loc.getY());
				commonEffect.write(effect.z, (float) loc.getZ());
	
				if (locOffSet != null) {
					commonEffect.write(effect.randomX, (float) locOffSet.getX());
					commonEffect.write(effect.randomY, (float) locOffSet.getY());
					commonEffect.write(effect.randomZ, (float) locOffSet.getZ());
				}
				
				PacketUtil.broadcastPacketNearby(loc, 256, commonEffect);//max render distance
				Bukkit.broadcastMessage("effect sent");
				
			} catch (Exception e) {e.printStackTrace(); safeCancel(); return;}
	}

}
