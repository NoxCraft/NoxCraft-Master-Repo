package com.noxpvp.mmo.abilities;

import java.util.List;

import org.bukkit.entity.Entity;

import com.noxpvp.core.internal.IHeated;

public interface IEntityAbility extends Ability, IHeated {
	
	/**
	 * Retrieves the entity responsible for this ability.
	 *
	 * @return Entity object from bukkit.
	 */
	public Entity getEntity();

	/**
	 * Gets the damage variable stored in this ability, 0 if it has not been set
	 *
	 * @return {@link Double} damage amount
	 */
	public double getDamage();

	/**
	 * Sets a damage variable to be used in this ability
	 *
	 * @param damage
	 */
	public void setDamage(double damage);
	
	public void addEffectedEntity(Entity e);
	
	public List<Entity> getEffectedEntities();
	
	public void clearEffected();
	
	/**
	 * Gets the cooldown seconds variable to be used in this ability
	 * 
	 * @return seconds
	 */
	public int getCD();
	
	/**
	 * Sets the cooldown seconds variable to be used in this ability
	 * 
	 * @param cd
	 */
	public void setCD(int cd);
	
}
