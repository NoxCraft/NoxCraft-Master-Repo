package com.noxpvp.mmo;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {

	private PlayerClass curMainClass, curSubClass;
	private Reference<LivingEntity> target_ref;
	private Map<String, Ability> abilities = new HashMap<String, Ability>();
	
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
		return abilities.values().toArray(new Ability[abilities.size()]);
	}
	
	public PassiveAbility[] getPassiveAbilities()
	{
		List<PassiveAbility> passive = new ArrayList<PassiveAbility>();
		for (Ability a : getAbilities())
			if (a instanceof PassiveAbility)
				passive.add((PassiveAbility)a);
		
		return passive.toArray(new PassiveAbility[passive.size()]);
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
	
	public PlayerClass getSubPlayerClass() {
		return this.curSubClass;
	}
	
	public PlayerClass getMainPlayerClass() {
		return this.curMainClass;
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
