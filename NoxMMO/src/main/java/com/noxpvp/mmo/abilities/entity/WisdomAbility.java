package com.noxpvp.mmo.abilities.entity;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.utils.EffectsRunnable;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class WisdomAbility extends BaseEntityAbility{

	public final static String ABILITY_NAME = "Wisdom";

	private NoxMMO mmo;
	private int duration;
	private int amplifier;

	/**
	 * Gets the duration
	 * 
	 * @return Integer The duration
	 */
	public int getDuration() {return duration;}

	/**
	 * Sets the duration
	 * 
	 * @param duration
	 * @return WisdomAbility This instance
	 */
	public WisdomAbility setDuration(int duration) {this.duration = duration; return this;}

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
	public WisdomAbility setAmplifier(int amplifier) {this.amplifier = amplifier; return this;}

	public WisdomAbility(Entity e) {
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
		
		e.addPotionEffects(Arrays.asList(new PotionEffect(PotionEffectType.SLOW, 45, 50), new PotionEffect(PotionEffectType.JUMP, 45, -50)));
		Bukkit.getScheduler().runTaskLater(mmo, new Runnable() {
			
			public void run() {
				new EffectsRunnable(Arrays.asList("enchantmenttable"), false, null, 1.5F, 250, 1, e).runTask(mmo);
				e.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * getDuration(), getAmplifier()));
				
			}
		}, 12);
		
		return true;
	}

}
