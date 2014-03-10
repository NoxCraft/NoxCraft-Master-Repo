package com.noxpvp.mmo.abilities.entity;

import org.bukkit.entity.Entity;

import com.noxpvp.mmo.abilities.BaseEntityAbility;

public class ForcePullAbility extends BaseEntityAbility {

//TODO Finish
	
	public static final String ABILITY_NAME = "ForcePull";
	public static final String PERM_NODE = "force-pull";
	
	public ForcePullAbility(Entity ent)
	{
		super(ABILITY_NAME, ent);
	}
	
	public boolean execute() {
		return false;
	}

}
