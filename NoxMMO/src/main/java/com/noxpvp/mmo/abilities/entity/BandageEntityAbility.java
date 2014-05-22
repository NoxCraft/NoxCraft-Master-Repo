package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BaseEntityAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class BandageEntityAbility extends BaseEntityAbility{
	
	public final static String ABILITY_NAME = "Bandage";
	public final static String PERM_NODE = "bandage";
	private int delay;

	public int getDelay() {return delay;}

	public BandageEntityAbility setDelay(int delay) {this.delay = delay; return this;}

	public BandageEntityAbility(Entity entity) {
		super(ABILITY_NAME, entity);
		
		this.delay = 10 * 20;
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;		
		
		if (!(getEntity() instanceof LivingEntity))
			return false;
		
		LivingEntity e = (LivingEntity) getEntity();
		
		e.setHealth(e.getMaxHealth());
		
		DamageRunnable wearOff = new DamageRunnable(e, e, (e.getMaxHealth() / 10), 10);
		wearOff.runTaskTimer(NoxMMO.getInstance(), delay, 15);
		
		return false;
	}

}
