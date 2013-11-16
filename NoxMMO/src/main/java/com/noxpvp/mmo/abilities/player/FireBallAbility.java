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
	
	public final static String PERM_NODE = "fireball";
	private final static String ABILITY_NAME = "FireBall";
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
	}

	/**
	 * 
	 * 
	 * @return Boolean If this ability has executed successfully
	 */
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

	/**
	 * 
	 * 
	 * @return Boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		return getEntity() != null;
	}

}
