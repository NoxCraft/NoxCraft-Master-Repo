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

package com.noxpvp.mmo.abilities.player;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class RazerClawsPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String ABILITY_NAME = "Razer Claws";
	public static final String PERM_NODE = "razer-claws";

	private Damageable target;

	public RazerClawsPlayerAbility(Player p, Damageable target) {
		super(ABILITY_NAME, p);

		this.target = target;
	}

	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {

		if (event.getEntity() != getPlayer() || !mayExecute())
			return new AbilityResult(this, false);

		IPlayerClass clazz = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();

		int levels = clazz.getTotalLevel();

		float chance = (levels / 20) <= 20 ? levels / 20 : 20;
		if (Math.random() > (chance))
			return new AbilityResult(this, false);

		new DamageRunnable(target, getPlayer(), ((levels / 90) <= 5 ? levels / 90 : 90), (int) levels / 70).runTaskTimer(NoxMMO.getInstance(), 0, 30);

		return new AbilityResult(this, true);
	}

}
