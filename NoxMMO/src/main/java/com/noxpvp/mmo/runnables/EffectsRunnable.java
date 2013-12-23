package com.noxpvp.mmo.runnables;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
	private String name;
	
	/**
	 * 
	 * @param name The effect name | List - https://gist.github.com/thinkofdeath/5110835
	 * @param loc The particle location
	 * @param locOffSet
	 * @param data
	 * @param amount The amount of particles
	 */
	public EffectsRunnable(String name, Location loc, Location locOffSet, float data, int amount){
		this.name = name;
		this.loc = loc;
		this.locOffSet = locOffSet;
		this.data = data;
		this.amount = amount;
		
	}
	
	/**
	 * 
	 * @param name The effect name | List - https://gist.github.com/thinkofdeath/5110835
	 * @param loc The particle location
	 * @param data
	 * @param amount The amount of particles
	 */
	public EffectsRunnable(String name, Location loc, float data, int amount){
		this.name = name;
		this.loc = loc;
		this.data = data;
		this.amount = amount;
		
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	public void run(){
		
		try {
			CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
			NMSPacketPlayOutWorldParticles effect = new NMSPacketPlayOutWorldParticles();
			
			commonEffect.write(effect.effectName, name);
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
			
			
				
			for (Player p : Bukkit.getOnlinePlayers()) {PacketUtil.sendPacket(p, commonEffect);}
		}
		catch (Exception e) {e.printStackTrace(); safeCancel(); return;}
	}

}
