package com.noxpvp.mmo;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;

import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {

	private PlayerClass currentClass;
	private Reference<LivingEntity> target_ref;
	
	public MMOPlayer(OfflinePlayer player)
	{
		super(player);
	}
	
	public MMOPlayer(String name)
	{
		super(name);
	}

	public MMOPlayer(NoxPlayerAdapter player)
	{
		super(player);
	}
	
	public Ability[] getAbilities()
	{
		//TODO: Implement.
		return null;
	}
	
	public PassiveAbility[] getPassiveAbilities()
	{
		//TODO: Implement
		return null;
	}
	
	public void save() {
		if (getNoxPlayer() == null)
			return;
		
		//TODO: Implement Saving
	}

	public void load() {
		if (getNoxPlayer() == null)
			return;
		
		//TODO: Implement Loading.
	}

	public Ability getAbility(String abilityName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * @param target - The LivingEntity to set as the current target
	 * @return MMOPlayer - This instance
	 * 
	 * @throws IllegalStateException - If the target is dead
	 */
	public MMOPlayer setTarget(LivingEntity target){
		if (target != null && target.isDead())
		{
			throw new IllegalStateException("Target cannot be dead");
		}
		
		this.target_ref = new SoftReference<LivingEntity>(target);
		
		return this;
	}
	
	/**
	 * 
	 * @return LivingEntity - This players currently set target
	 */
	public LivingEntity getTarget(){return this.target_ref.get();}
	
	/**
	 * 
	 * @return boolean - If this player has a target set
	 */
	public boolean hasTarget(){
		if ( target_ref.get() == null || target_ref.get().isDead())
			return false;
		
		return true;
	}
}
