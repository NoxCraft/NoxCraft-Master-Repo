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
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.core.effect.StaticEffects;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseRangedPlayerAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.handlers.BaseMMOEventHandler;
import com.noxpvp.mmo.runnables.UnregisterMMOHandlerRunnable;

public class RagePlayerAbility extends BaseRangedPlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Rage";
	public static final String PERM_NODE = "Rage";
	private BaseMMOEventHandler<EntityDamageByEntityEvent> handler;

	/**
	 * @param player The user of the ability instance
	 */
	public RagePlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range);
		
		setCD(75);

		this.handler = new BaseMMOEventHandler<EntityDamageByEntityEvent>(
				new StringBuilder().append(player.getName()).append(ABILITY_NAME).append("EntityDamageByEntityEvent").toString(),
				EventPriority.MONITOR, 1) {

			public boolean ignoreCancelled() {
				return true;
			}

			public Class<EntityDamageByEntityEvent> getEventType() {
				return EntityDamageByEntityEvent.class;
			}

			public String getEventName() {
				return "EntityDamageByEntityEvent";
			}

			public void execute(EntityDamageByEntityEvent event) {
				if (!event.getDamager().equals(RagePlayerAbility.this.getPlayer()))
					return;

				double damage = event.getDamage();
				double range = getRange();
				
				Player attacker = (Player) event.getDamager();

				for (Entity it : getPlayer().getNearbyEntities(range, range, range)) {
					if (!(it instanceof Damageable) || it.equals(attacker)) continue;

					((Damageable) it).damage(damage - (damage / 4), attacker);
					StaticEffects.SkullBreak((LivingEntity) it);
				}
			}
		};

	}

	public RagePlayerAbility(Player player) {
		this(player, 5);
	}

	@Override
	public String getDescription() {
		return "Your axe becomes ingulfed with your own rage! Dealing 75% damage to all enemys surrounding anything you damage";
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		IPlayerClass pClass = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();

		int length = (20 * (pClass.getTotalLevel())) / 16;

		registerHandler(handler);
		new UnregisterMMOHandlerRunnable(handler).runTaskLater(NoxMMO.getInstance(), length);

		return new AbilityResult(this, true);
	}

}
