package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
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
	private LivingEntity target;
	private WolfController wolfController = new WolfController() {
		
		@Override
		public void onDamage(DamageSource damageSource, double damage) {
			super.onDamage(damageSource, damage);
		}
		
	};
	
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
		
		@Override
		public void onDamage(DamageSource damageSource, double damage) {
			// TODO Auto-generated method stub
			super.onDamage(damageSource, damage);
			if (!isAnyNonAttackingSource(damageSource) && damageSource.getEntity() != null)
				trySetAggro(damageSource.getEntity());
			
		}
		
		public Wolf getWolf() { return getEntity().getEntity(); }
		
		private void trySetAggro(Entity entity) {
			if (entity instanceof Creature && entity != tamer) {
				Creature creature = (Creature) entity;
				currentTarget = creature;
			
				Wolf w = getWolf();
				if (w != null) {
					w.setAngry(true);
					w.setTarget(creature);
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
