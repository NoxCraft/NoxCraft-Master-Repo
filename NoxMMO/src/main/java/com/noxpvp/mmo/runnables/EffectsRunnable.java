package com.noxpvp.mmo.runnables;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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
	
	public enum EffectType{
		
		
	}
	private List<String> names = new ArrayList<String>();
	
	private Location loc;
	private Location locOffSet;
	
	private float data;
	private int amount;
	
	private int runs;
	
	private Entity tracker;
	
	/**
	 * 
	 * @param List<String> The effect name(s) | List - https://gist.github.com/thinkofdeath/5110835
	 * @param offSet If each particle should have a random offset
	 * @param loc The particle location
	 * @param data
	 * @param amount The amount of particles
	 * @param runs The amount of time to run, EX: 5 amount and 3 runs would have the effect 3 times, each with 5 particles
	 * @param tracker 
	 */
	public EffectsRunnable(List<String> name, boolean offSet, @Nullable Location loc, float data, int amount, int runs, @Nullable Entity tracker) {
		this.names = name;
		
		this.loc = loc;
		
		if (offSet){
			boolean upOrDown = (float) Math.random() > .5? true : false;
			float random = (float) Math.random();
			
			double x = upOrDown? loc.getX() + random : loc.getBlockX() - random;
			double y = upOrDown? loc.getY() + random : loc.getBlockY() - random;
			double z = upOrDown? loc.getZ() + random : loc.getBlockZ() - random;
			
			this.locOffSet = new Location(loc.getWorld(), x, y, z);
		}
		
		this.data = data;
		this.amount = amount;
		
		this.runs = runs;
		this.tracker = tracker != null? tracker : null;
		
	}

	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	int i = 0;
	public void run(){
		if (runs != 0 && i <= runs){
			i++;
		} else if (runs != 0 && i > runs) {
			safeCancel();
			return;
		} else if (runs == 0) {
			if (tracker != null && (tracker.isDead()  || !tracker.isValid())) {
				safeCancel();
				return;
			} else {
				this.loc = tracker.getLocation();
			}
		}
		
		for (String effectName : names){
			try {
				CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
				NMSPacketPlayOutWorldParticles effect = new NMSPacketPlayOutWorldParticles();
				
				Bukkit.broadcastMessage("editing data");
				commonEffect.write(effect.effectName, effectName);
				commonEffect.write(effect.particleCount, amount);
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

}
