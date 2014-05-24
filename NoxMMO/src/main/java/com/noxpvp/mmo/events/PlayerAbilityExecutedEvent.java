package com.noxpvp.mmo.events;

import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class PlayerAbilityExecutedEvent extends PlayerAbilityEvent {
	private AbilityResult result;

	public PlayerAbilityExecutedEvent(Player who, AbilityResult result) {
		super(who, (BasePlayerAbility) result.getExecuter());
		
		this.result = result;
	}

	/**
	 * Gets the result of the ability that caused this event
	 * 
	 * @return {@link AbilityResult} the result
	 */
	public AbilityResult getResult() {
		return result;
	}

}
