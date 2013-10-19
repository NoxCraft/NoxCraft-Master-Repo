package com.noxpvp.mmo;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.Persistant;
import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.abilities.Ability;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class MMOPlayer extends BaseNoxPlayerAdapter implements Persistant {

	private PlayerClass currentClass;
	
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
}
