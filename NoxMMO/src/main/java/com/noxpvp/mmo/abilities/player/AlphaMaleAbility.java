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

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Sound;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import com.bergerkiller.bukkit.common.controller.EntityController;
import com.bergerkiller.bukkit.common.entity.type.CommonLivingEntity;
import com.bergerkiller.bukkit.common.wrappers.DamageSource;
import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class AlphaMaleAbility extends BasePlayerAbility {
	public static final String PERM_NODE = "alpha-male";
	public static final String ABILITY_NAME = "Alpha Male";
	
	public AlphaMaleAbility(Player player) {
		super(ABILITY_NAME, player);
	}


	private CommonLivingEntity<Wolf> wolf;
	
	public Creature getActiveTarget() {
		return wolfController.getTarget();
	}
	
	private WolfController wolfController = new WolfController();
	
	public void setWolf(Wolf wolf) {
		this.wolf = new CommonLivingEntity<Wolf>(wolf);
		this.wolf.setController(wolfController);
	}
	
	public boolean execute() {
		return false;
	}
	
	public WolfController getWolfController() {
		return wolfController;
	}
	
	
	static class WolfController extends EntityController<CommonLivingEntity<Wolf>> {
		private Creature currentTarget;
		private AnimalTamer tamer;
		
		private short growlTicker = 0;
		private short nextGrowlTick = (short) RandomUtils.nextInt(100);
		
		@Override
		public void onDamage(DamageSource damageSource, double damage) {
			if (!isAnyNonAttackingSource(damageSource) && damageSource.getEntity() != null)
				setTarget(damageSource.getEntity());
			
			super.onDamage(damageSource, damage);
		}
		
		public Wolf getWolf() { return getEntity().getEntity(); }
		
		public void setTarget(Entity entity) {
			Wolf w = getWolf();
			if (entity instanceof Creature && entity != tamer) {
				Creature creature = (Creature) entity;
				currentTarget = creature;
			
				if (w != null) {
					w.setAngry(true);
					w.setTarget(creature);
				}
			} else if (entity == null) {
				w.setTarget(null);
				w.setAngry(false);
				growlTicker = 0; //Reset tick logic.
			}
		}
		
		private boolean isAngry() {
			return getWolf().isAngry();
		}
		
		public void growl() {
			getEntity().makeRandomSound(Sound.WOLF_GROWL, 100, 1);
		}
		
		@Override
		public void onTick() {
			super.onTick();
			
			if (isAngry()) { //Growling logic.
				growlTicker++;
				if (growlTicker >= nextGrowlTick) {
					nextGrowlTick = (short) RandomUtils.nextInt(100);
					growlTicker = 0;
					growl();
				}
			}
		}
		
		public Creature getTarget() { return currentTarget; }
		
		private static boolean isAnyNonAttackingSource(DamageSource ds) {
			if (ds == DamageSource.ANVIL || ds == DamageSource.CACTUS
					|| ds == DamageSource.DROWN || ds == DamageSource.FALL 
					|| ds == DamageSource.FALLING_BLOCK || ds == DamageSource.FIRE
					|| ds == DamageSource.LAVA || ds == DamageSource.OUT_OF_WORLD
					|| ds == DamageSource.STARVE || ds == DamageSource.STUCK
					|| ds == DamageSource.WITHER)
				return true;
			return false;
		}
	}

}
