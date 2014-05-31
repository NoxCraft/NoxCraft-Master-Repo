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

package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketTypeClasses.NMSPacketPlayOutNamedEntitySpawn;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class BandageAbility extends BaseEntityAbility{
	
	public final static String ABILITY_NAME = "Bandage";
	public final static String PERM_NODE = "bandage";
	private int delay;

	public int getDelay() {return delay;}

	public BandageAbility setDelay(int delay) {this.delay = delay; return this;}

	public BandageAbility(Entity entity) {
		super(ABILITY_NAME, entity);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;		
		
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		LivingEntity e = (LivingEntity) getEntity();
		
		e.setHealth(e.getMaxHealth());
		
		DamageRunnable wereOff = new DamageRunnable(e, e, (e.getMaxHealth() / 10), 10);
		wereOff.runTaskTimer(NoxMMO.getInstance(), delay, 15);
		
		return false;
	}

}
