package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class BackStabAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "BackStab";
	private Entity target;
	private double accuracy = 20;
	private int chancePercent = 50;
	
	public double getAccuracy() {return accuracy;}
	public BackStabAbility setAccuracy(double accuracy) {this.accuracy = accuracy; return this;}
	
	public int getChancePercent() {return chancePercent;}
	public BackStabAbility setChancePercent(int chancePercent) {this.chancePercent = chancePercent; return this;}
	
	public Entity getTarget() {return target;}
	public BackStabAbility setTarget(Entity target) {this.target = target; return this;}
	
	public BackStabAbility(Player player){
		super(ABILITY_NAME, player);
	}
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		double tYaw = getTarget().getLocation().getYaw();
		double pYaw = getPlayer().getLocation().getYaw();
		
		if ((pYaw <= (tYaw + accuracy)) && (pYaw >= (tYaw - accuracy)))
			return true;
					
		return false;
	}
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}	
}
