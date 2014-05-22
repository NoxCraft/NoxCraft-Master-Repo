package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PlayerAbilityExecutedEvent extends PlayerAbilityEvent {

	public PlayerAbilityExecutedEvent(Player who, BasePlayerAbility ability) {
		super(who, ability);

	}

}
