package com.noxpvp.mmo.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.events.internal.ITargetedPlayerAbilityEvent;

public class PlayerTargetedAbilityPreExecuteEvent extends PlayerAbilityPreExecuteEvent implements ITargetedPlayerAbilityEvent {

	public PlayerTargetedAbilityPreExecuteEvent(Player who, BaseTargetedPlayerAbility ability) {
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
