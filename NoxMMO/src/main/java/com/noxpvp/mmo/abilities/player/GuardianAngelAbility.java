package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.NumberConversions;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import com.noxpvp.mmo.runnables.EffectRunnable;
import com.noxpvp.mmo.runnables.EntityEffectRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;

public class GuardianAngelAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Guardian Angel";
	private int range = 5;
	private int healAmount = 6;
	private Effect leaveEffect;
	
	public int getRange() {return range;}
	public GuardianAngelAbility setRange(int range) {this.range = range; return this;}
	
	public int getHealAmount() {return healAmount;}
	public GuardianAngelAbility setHealAmount(int healAmount) {this.healAmount = healAmount; return this;}
	
	public Effect getLeaveEffect() {return this.leaveEffect;}
	public GuardianAngelAbility setLeaveEffect(Effect leaveEffect) {this.leaveEffect = leaveEffect; return this;}
	
	public GuardianAngelAbility(Player player){
		super(ABILITY_NAME, player);
	}
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
		
		NoxMMO instance = NoxMMO.getInstance();
		
		for (Entity it : v.getNearbyEntities(range, range, range)){
			if (!(it instanceof Player)) continue;
			
			Entity e = it;
			
			Runnable heal = new HealRunnable(e, getHealAmount(), 1);
			Runnable effect = new EntityEffectRunnable(e, EntityEffect.WOLF_HEARTS, NumberConversions.toInt((getHealAmount() / 2)));
			Bukkit.getScheduler().runTaskLater(instance, heal, 40);
			Bukkit.getScheduler().runTaskTimer(instance, effect, 25, 5);
		}
		Runnable leaveEffect = new EffectRunnable(v, getLeaveEffect(), 1, 5);
		Runnable despawn = new DespawnRunnable(v);
		Bukkit.getScheduler().runTaskTimer(instance, leaveEffect, 90, 2);
		Bukkit.getScheduler().runTaskLater(instance, despawn, 100);
		
		return true;
	}
	
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
}
