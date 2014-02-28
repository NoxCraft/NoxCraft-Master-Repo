package com.noxpvp.mmo.abilities.player;

import java.util.Arrays;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DespawnRunnable;
import com.noxpvp.mmo.runnables.EffectsRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;

/**
 * @author NoxPVP
 *
 */
public class GuardianAngelAbility extends BasePlayerAbility{
	
	public static final String PERM_NODE = "guardian-angel";
	public static final String ABILITY_NAME = "Guardian Angel";
	
	private int range;
	private double healAmount;
	
	/**
	 * 
	 * 
	 * @return Integer Currently set range for the angel heal range
	 */
	public int getRange() {return range;}
	
	/**
	 * 
	 * 
	 * @param range The Integer range the angel should look for player to heal
	 * @return GuardianAngelAbility This instance, used for chaining
	 */
	public GuardianAngelAbility setRange(int range) {this.range = range; return this;}
	
	/**
	 * 
	 * 
	 * @return Integer The currently set amount to heal players
	 */
	public double getHealAmount() {return healAmount;}
	
	/**
	 * 
	 * 
	 * @param healAmount double amount that the angel should heal players
	 * @return GuardianAngelAbility This instance, used for chaining
	 */
	public GuardianAngelAbility setHealAmount(double healAmount) {this.healAmount = healAmount; return this;}
	
	/**
	 * 
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public GuardianAngelAbility(Player player){
		super(ABILITY_NAME, player);
		
		this.healAmount = 6;
		this.range = 6;
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		Villager v = (Villager) p.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
		
		NoxMMO instance = NoxMMO.getInstance();
		
		for (Entity it : v.getNearbyEntities(range, range, range)){
			if (!(it instanceof Player)) continue;
			
			LivingEntity e = (LivingEntity) it;
			
			new HealRunnable(e, getHealAmount(), 1).runTaskLater(instance, 40);
			new EffectsRunnable(Arrays.asList("heart"), false, e.getLocation(), 0, (int) getHealAmount()/2, 2, null).runTaskTimer(instance, 25, 5);
		}
		
		new EffectsRunnable(Arrays.asList("angryVillager"), false, null, 0f, 1, 1, v).runTaskLater(instance, 60);
		new DespawnRunnable(v).runTaskLater(instance, 60);
		
		return true;
	}
	
}
