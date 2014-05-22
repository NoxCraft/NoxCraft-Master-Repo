package com.noxpvp.mmo.abilities.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.effect.shaped.BaseHelix;
import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

/**
 * @author NoxPVP
 */
public class FireBallPlayerAbility extends BaseEntityAbility implements IPVPAbility {

	public static final String ABILITY_NAME = "Fire Ball";
	public static final String PERM_NODE = "fire-ball";

	private double power;

	/**
	 * @param e The Entity type user of this ability instance
	 */
	public FireBallPlayerAbility(Entity e) {
		super(ABILITY_NAME, e);

		this.power = 3;
	}

	/**
	 * @return double current power set for fireball launch
	 */
	public double getPower() {
		return this.power;
	}

	/**
	 * @param power The double power to shoot the fireball at
	 *              (THIS MUST BE 1.0 OR ABOVE)
	 * @return FireBallAbility This instance, used for chaining
	 */
	public FireBallPlayerAbility setPower(double power) {
		this.power = power;
		return this;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;

		Entity e = getEntity();
		Location loc = (e instanceof LivingEntity) ? ((LivingEntity) e).getEyeLocation() : e.getLocation();
		Projectile fireBall = (Projectile) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);

		fireBall.setVelocity(loc.getDirection().normalize().multiply(getPower()));
		fireBall.setShooter((ProjectileSource) e);

		new FireBallHelix(e, 60).render(250);

		return true;
	}

	private class FireBallHelix extends BaseHelix {

		public FireBallHelix(Entity user, int time) {
			super((user instanceof LivingEntity) ?
					((LivingEntity) user).getEyeLocation().add(user.getLocation().getDirection().normalize()) :
					user.getLocation().add(user.getLocation().getDirection().normalize()), time);

			setForwardGain(0.07);
			setRadiusGain(0.5);
			setSpeed(1);

		}

		public NoxPlugin getPlugin() {
			return NoxMMO.getInstance();
		}

		public void onStart() {
			return;
		}

		public void onRun() {
			new ParticleRunner(ParticleType.flame, getLocation(), false, 0, 1, 1).start(0);

		}

		public void onStop() {
			return;
		}
	}

}
