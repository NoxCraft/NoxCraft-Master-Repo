package com.noxpvp.mmo.abilities.player;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.EntityEffectRunnable;
import com.noxpvp.mmo.runnables.HealRunnable;

public class RejuvenationAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Rejuvenation";
	private int healthPerHeal = 1;
	private int heals = 6;
	private int delayBetweenHeals = 15;
	
	public int getHealthPerHeal() {return healthPerHeal;}
	public RejuvenationAbility setHealthPerHeal(int healthPerHeal) {this.healthPerHeal = healthPerHeal; return this;}
	
	public int getHeals() {return heals;}
	public RejuvenationAbility setHeals(int heals) {this.heals = heals; return this;}
	
	public int getDelayBetweenHeals() {return delayBetweenHeals;}
	public RejuvenationAbility setDelayBetweenHeals(int delayBetweenHeals) {this.delayBetweenHeals = delayBetweenHeals; return this;}
	
	public RejuvenationAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		NoxMMO instance = NoxMMO.getInstance();
		
		HealRunnable rejuvenation = new HealRunnable(getPlayer(), healthPerHeal, heals);
		EntityEffectRunnable hearts = new EntityEffectRunnable(getPlayer(), EntityEffect.WOLF_HEARTS, heals);
		
		rejuvenation.runTaskTimer(instance, 0, delayBetweenHeals);
		hearts.runTaskTimer(instance, 0, delayBetweenHeals);
		
		return true;
	}
	
	public boolean mayExecute() {
		return getPlayer() != null;
	}
	
	
}