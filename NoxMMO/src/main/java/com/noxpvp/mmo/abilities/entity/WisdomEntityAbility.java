package com.noxpvp.mmo.abilities.entity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.packet.ParticleRunner;
import com.noxpvp.core.packet.ParticleType;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

public class WisdomEntityAbility extends BaseEntityAbility implements IPVPAbility {

	public static final String ABILITY_NAME = "Wisdom";
	public static final String PERM_NODE = "wisdom";

	@Override
	public String getDescription() {
		return "You gain a burst of sudden wisdom, increasing your attack strength for " + getDuration() + " seconds";
	}
	
	private NoxMMO mmo;
	private int duration;
	private int amplifier;

	/**
	 * Gets the duration is seconds
	 * 
	 * @return Integer The duration
	 */
	public int getDuration() {return duration;}

	/**
	 * Sets the duration
	 * 
	 * @param duration in seconds
	 * @return WisdomAbility This instance
	 */
	public WisdomEntityAbility setDuration(int duration) {this.duration = duration; return this;}

	/**
	 * Gets the amplifier 
	 * 
	 * @return Integer The amplifier
	 */
	public int getAmplifier() {return amplifier;}

	/**
	 * Sets the amplifier
	 * 
	 * @param amplifier
	 * @return WisdomAbility This instance
	 */
	public WisdomEntityAbility setAmplifier(int amplifier) {this.amplifier = amplifier; return this;}

	public WisdomEntityAbility(Entity e) {
		super(ABILITY_NAME, e);
		
		this.mmo = NoxMMO.getInstance();
		this.duration = 10;
		this.amplifier = 3;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		final LivingEntity e = (LivingEntity) (getEntity() instanceof LivingEntity? getEntity() : null);
		
		if (e == null) return false;
		
		final Location loc = e.getLocation().clone();
		e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 45, -100), true);
		e.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 45, -100), true);
		
		new ParticleRunner(ParticleType.enchantmenttable, e, false, 2F, 150, 1).start(10);
		new ParticleRunner(ParticleType.fireworksSpark, e, false, 0.3F, 50, 1).start(45);
		new ParticleRunner(ParticleType.flame, e, false, 0.05F, 50, 1).start(45);

		
		Bukkit.getScheduler().runTaskLater(mmo, new Runnable() {
			
			public void run() {
				e.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getDuration(), getAmplifier()), true);
				e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * getDuration(), getAmplifier()), true);
				e.teleport(loc);
			}
		}, 45);
		
		return true;
	}

}
