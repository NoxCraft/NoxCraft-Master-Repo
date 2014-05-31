/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.entity.Entity;

import com.noxpvp.core.utils.TownyUtil;

public abstract class BaseEntityAbility extends BaseAbility implements EntityAbility {
	private Reference<Entity> entityRef;
	
	public BaseEntityAbility(final String name, Entity ref)
	{
		super(name);
		entityRef = new SoftReference<Entity>(ref);
	}
	
	public Entity getEntity() {
		return entityRef.get();
	}
	
	public boolean isValid() { return getEntity() != null; }
	
	/**
	 * Returns is the Entity of this ability is null, thus if the execute method will start
	 * 
	 * @return boolean If the execute() method is normally able to start
	 */
	public boolean mayExecute() {
		Entity entity = getEntity();
		
		return entity != null && (((this instanceof PVPAbility) && TownyUtil.isPVP(entity)) || !(this instanceof PVPAbility));
	}
	
}
