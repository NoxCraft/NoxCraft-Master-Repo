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

package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

/**
 * @author NoxPVP
 *
 */
public class FireBallAbility extends BaseEntityAbility{
	
	public static final String PERM_NODE = "fireball";
	public static final String ABILITY_NAME = "FireBall";
	
	private double power;
	
	/**
	 * 
	 * 
	 * @param power The double power to shoot the fireball at
	 * (THIS MUST BE 1.0 OR ABOVE)
	 * @return FireBallAbility This instance, used for chaining
	 */
	public FireBallAbility setPower(double power) {this.power = power; return this;}
	
	/**
	 * 
	 * 
	 * @return double current power set for fireball launch
	 */
	public double getPower() {return this.power;}
	
	/**
	 * 
	 * 
	 * @param e The Entity type user of this ability instance
	 */
	public FireBallAbility(Entity e){
		super(ABILITY_NAME, e);
		
		this.power = 3;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		Entity e = getEntity();
		Projectile fireBall = (Projectile) e.getWorld().spawnEntity(e.getLocation().add(0, 1, 0), EntityType.SMALL_FIREBALL);
		
		fireBall.setVelocity(e.getVelocity().multiply(power));
		fireBall.setShooter((LivingEntity) e);
		
		return true;
	}

}
