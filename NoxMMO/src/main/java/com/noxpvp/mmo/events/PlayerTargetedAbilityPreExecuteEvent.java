/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

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
