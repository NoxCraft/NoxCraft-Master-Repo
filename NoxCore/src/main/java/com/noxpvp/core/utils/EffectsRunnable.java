package com.noxpvp.core.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutWorldParticles;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.utils.gui.MessageUtil;

/**
 * @author NoxPVP
 *
 */
public class EffectsRunnable extends BukkitRunnable{
	
	public enum EffectType{
		
		
	}
	private List<String> names = new ArrayList<String>();
	
	private Location loc;
	private boolean offSet;
	
	private float data;
	private int amount;
	
	private int runs;
	
	private Entity tracker;
	
	/**
	 * 
	 * @param List<String> The effect name(s) | List - https://gist.github.com/thinkofdeath/5110835
	 * @param offSet If each particle should have a random offset
	 * @param loc The particle location. Can not be null if tracker is null
	 * @param data
	 * @param amount The amount of particles
	 * @param runs The amount of time to run, EX: 5 amount and 3 runs would have the effect 3 times, each with 5 particles
	 * @param tracker Can not be null if loc is null
	 */
	public EffectsRunnable(List<String> name, boolean offSet, @Nullable Location loc, float data, int amount, int runs, @Nullable Entity tracker) {
		this.names = name;
		
		this.loc = loc;
		this.offSet = offSet;
		
		this.data = data;
		this.amount = amount;
		
		this.runs = runs;
		this.tracker = tracker;
		
	}

	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	int i = 0;
	public void run(){
		
		if (runs != 0 && i < runs){
			i++;
		} else if ((runs != 0 && i >= runs) || (runs == 0 && i > 0)) {
			safeCancel();
			return;
		}
		
		if (tracker != null && (tracker.isDead()  || !tracker.isValid())) {
			safeCancel();
			return;
		} else if (tracker != null){
			if (tracker instanceof LivingEntity){
				loc = ((LivingEntity) tracker).getEyeLocation();
			} else
				loc = tracker.getLocation();
		}
		
		for (String effectName : names){
			try {
				CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
				NMSPacketPlayOutWorldParticles effect = new NMSPacketPlayOutWorldParticles();
				
				commonEffect.write(effect.effectName, effectName);
				commonEffect.write(effect.particleCount, amount);
				commonEffect.write(effect.speed, data);
				
				commonEffect.write(effect.x, (float) loc.getX());
				commonEffect.write(effect.y, (float) loc.getY());
				commonEffect.write(effect.z, (float) loc.getZ());
	
				if (offSet) {
					commonEffect.write(effect.randomX, RandomUtils.nextFloat());
					commonEffect.write(effect.randomY, RandomUtils.nextFloat());
					commonEffect.write(effect.randomZ, RandomUtils.nextFloat());
				}
				
				PacketUtil.broadcastPacketNearby(loc, 125, commonEffect);
			
			} catch (Exception e) {
				e.printStackTrace();
				safeCancel();
				return;
			}
		}
	}

}
