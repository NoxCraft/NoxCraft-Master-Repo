package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class ForcePullAbility extends BaseEntityAbility {

	public ForcePullAbility(Entity ent)
	{
		super("ForcePull", ent);
	}
	
	public boolean execute() {
		return false;
	}

	public boolean mayExecute() {
		// TODO Auto-generated method stub
		return false;
	}

}
