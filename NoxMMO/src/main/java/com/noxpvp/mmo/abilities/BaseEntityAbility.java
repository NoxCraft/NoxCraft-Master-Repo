package com.noxpvp.mmo.abilities;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.noxpvp.core.utils.TownyUtil;
import com.noxpvp.mmo.MasterListener;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;

public abstract class BaseEntityAbility extends BaseAbility implements EntityAbility {
	private Reference<Entity> entityRef;
	private MasterListener masterListener;
	private double damage;
	
	public BaseEntityAbility(final String name, Entity ref)
	{
		super(name);
		entityRef = new SoftReference<Entity>(ref);
		
		this.masterListener = NoxMMO.getInstance().getMasterListener();
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
	
	public MasterListener getMasterListener() {
		return masterListener;
	}
	
	public void registerHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.registerHandler(handler);
	}
	
	public void unregisterHandler(BaseMMOEventHandler<? extends Event> handler) {
		masterListener.unregisterHandler(handler);
		
	}
	
	public void setDamage(double damage) {
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}
	
}
