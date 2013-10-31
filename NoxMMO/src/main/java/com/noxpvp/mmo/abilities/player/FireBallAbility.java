package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class FireBallAbility extends BaseEntityAbility{
	
	public final static String PERM_NODE = "fireball";
	private final static String ABILITY_NAME = "FireBall";
	private int power;
	
	public FireBallAbility setPower(int power) {this.power = power; return this;}
	public int getPower() {return this.power;}
	
	public FireBallAbility(Entity e){
		super(ABILITY_NAME, e);
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		Entity e = getEntity();
		Projectile fireBall = (Projectile) e.getWorld().spawnEntity(e.getLocation(), EntityType.SMALL_FIREBALL);
		
		fireBall.setVelocity(e.getVelocity().multiply(power));
		fireBall.setShooter((LivingEntity) e);
		
		return true;
	}

	public boolean mayExecute() {
		return getEntity() != null;
	}

}
