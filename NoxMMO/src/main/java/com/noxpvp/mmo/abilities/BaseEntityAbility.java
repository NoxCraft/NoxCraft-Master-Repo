package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.entity.Entity;

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
		return getEntity() != null;
	}
	
}
