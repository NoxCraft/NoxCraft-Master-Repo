/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core.packet;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.PacketUtil;
import com.noxpvp.core.NoxCore;

/**
 * @author NoxPVP
 *
 */
public class ParticleRunner extends BukkitRunnable{
	private String name;
	
	private Object loc;
	private boolean offSet;
	
	private float data;
	private int amount;
	private int runs;
	
	int i;
	
	public ParticleRunner(ParticleType type, Entity tracker, boolean offSet, float data, int amount, int runs) {
		this(type.name(), tracker.getLocation(), offSet, data, amount, runs);
	}
	
	public ParticleRunner(ParticleType type, LivingEntity tracker, boolean offSet, float data, int amount, int runs) {
		this(type.name(), tracker.getEyeLocation(), offSet, data, amount, runs);
	}
	
	public ParticleRunner(ParticleType type, Location loc, boolean offSet, float data, int amount, int runs) {
		this(type.name(), loc, offSet, data, amount, runs);
	}
	
	public ParticleRunner(String name, Object loc, boolean offSet, float data, int amount, int runs) {
		this.name = name;
		
		this.loc = loc;
		this.offSet = offSet;
		
		this.data = data;
		this.amount = amount;
		this.runs = runs;
		
		this.i = 0;
	}

	public void start(int delay) {
		start(delay, 5);
	}
	
	public void start(int delay, int period) {
		runTaskTimer(NoxCore.getInstance(), delay, period);
	}
	
	public void safeCancel() {
		try { cancel(); } catch (IllegalStateException e) {}
	}
	
	public void run(){
		
		if (loc instanceof LivingEntity)
			loc = ((LivingEntity) loc).getEyeLocation().add(0, 1, 0);
		else if (loc instanceof Entity)
			loc = ((Entity) loc).getLocation();
		else if (!(loc instanceof Location))
			throw new IllegalArgumentException("Location must a type of location or entity");
		
		if ((runs != 0 && i >= runs) || (runs == 0 && i > 0)) {
			safeCancel();
			return;
		}
		i++;
		
		try {
			CommonPacket commonEffect = new CommonPacket(PacketType.OUT_WORLD_PARTICLES);
			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.effectName, name);
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.speed, data);			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.particleCount, amount);
			
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.x, (float) ((Location) loc).getX());
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.y, (float) ((Location) loc).getY());
			commonEffect.write(PacketType.OUT_WORLD_PARTICLES.z, (float) ((Location) loc).getZ());
			
			if (offSet) {
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomX, RandomUtils.nextFloat());
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomY, RandomUtils.nextFloat());
				commonEffect.write(PacketType.OUT_WORLD_PARTICLES.randomZ, RandomUtils.nextFloat());
			}
			
			PacketUtil.broadcastPacketNearby((Location) loc, 125, commonEffect);
			
		} catch (Exception e) {
			e.printStackTrace();
			safeCancel();
			return;
		}
	}

}
