package com.noxpvp.mmo.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.events.internal.ITargetedPlayerAbilityEvent;

public class PlayerTargetedAbilityExecutedEvent extends PlayerAbilityEvent implements ITargetedPlayerAbilityEvent {

	public PlayerTargetedAbilityExecutedEvent(Player who, BaseTargetedPlayerAbility ability) {
		super(who, ability);
	}
	
	@Override
	public BaseTargetedPlayerAbility getAbility() {
		return (BaseTargetedPlayerAbility) super.getAbility();
	}

	public LivingEntity getTarget() {
		return ((BaseTargetedPlayerAbility) ability).getTarget();
	}

}
