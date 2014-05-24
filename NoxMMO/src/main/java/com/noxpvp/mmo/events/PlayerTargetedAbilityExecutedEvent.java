package com.noxpvp.mmo.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BaseAbility.AbilityResult;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.events.internal.ITargetedPlayerAbilityEvent;

public class PlayerTargetedAbilityExecutedEvent extends PlayerAbilityExecutedEvent implements ITargetedPlayerAbilityEvent {

	public PlayerTargetedAbilityExecutedEvent(Player who, AbilityResult abilityResult) {
		super(who, abilityResult);
	}

	@Override
	public BaseTargetedPlayerAbility getAbility() {
		return (BaseTargetedPlayerAbility) super.getAbility();
	}

	public LivingEntity getTarget() {
		return getAbility().getTarget();
	}

}
