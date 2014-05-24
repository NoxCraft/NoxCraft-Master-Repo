package com.noxpvp.mmo.abilities;

import org.bukkit.event.Event;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;

/**
 * Currently this is no different from Ability. It only marks abilities that activate by themselves.
 *
 * @author Chris
 */
public interface IPassiveAbility<T extends Event> extends Ability {
	public AbilityResult execute(T event);
}
