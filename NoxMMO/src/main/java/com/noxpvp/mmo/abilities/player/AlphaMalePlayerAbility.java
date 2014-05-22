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
import com.noxpvp.mmo.abilities.IPVPAbility;

public class AlphaMalePlayerAbility extends BasePlayerAbility implements IPVPAbility {

	public static final String ABILITY_NAME = "Alpha Male";
	public static final String PERM_NODE = "alpha-male";
	private CommonLivingEntity<Wolf> wolf;
	private WolfController wolfController = new WolfController();

	public AlphaMalePlayerAbility(Player player) {
		super(ABILITY_NAME, player);
	}

	public Creature getActiveTarget() {
		return wolfController.getTarget();
	}

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

		private static boolean isAnyNonAttackingSource(DamageSource ds) {
			return ds == DamageSource.ANVIL || ds == DamageSource.CACTUS
					|| ds == DamageSource.DROWN || ds == DamageSource.FALL
					|| ds == DamageSource.FALLING_BLOCK || ds == DamageSource.FIRE
					|| ds == DamageSource.LAVA || ds == DamageSource.OUT_OF_WORLD
					|| ds == DamageSource.STARVE || ds == DamageSource.STUCK
					|| ds == DamageSource.WITHER;
		}

		@Override
		public void onDamage(DamageSource damageSource, double damage) {
			if (!isAnyNonAttackingSource(damageSource) && damageSource.getEntity() != null)
				setTarget(damageSource.getEntity());

			super.onDamage(damageSource, damage);
		}

		public Wolf getWolf() {
			return getEntity().getEntity();
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

		public Creature getTarget() {
			return currentTarget;
		}

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
	}

}
