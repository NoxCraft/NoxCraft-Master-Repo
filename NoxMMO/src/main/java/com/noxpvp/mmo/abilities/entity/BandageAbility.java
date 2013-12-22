package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

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
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
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
	
	/**
	 * 
	 * 
	 * @return Boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		return getEntity() != null;
	}

}