package com.noxpvp.mmo.abilities;

import org.bukkit.entity.Entity;

public interface EntityAbility extends Ability {
	/**
	 * Retrieves the entity responsible for this ability.
	 * @return Entity object from bukkit.
	 */
	public Entity getEntity();
}
