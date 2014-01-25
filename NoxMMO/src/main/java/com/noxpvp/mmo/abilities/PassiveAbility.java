package com.noxpvp.mmo.abilities;

import org.bukkit.event.Event;

/**
 * Currently this is no different from Ability. It only marks abilities that activate by themselves.
 * 
 * @author Chris
 */
public interface PassiveAbility <T extends Event> extends Ability {
	public boolean execute(T event);
}
