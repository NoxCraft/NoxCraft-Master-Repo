package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Entity;

public interface IEntityAbility extends Ability {
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
}
