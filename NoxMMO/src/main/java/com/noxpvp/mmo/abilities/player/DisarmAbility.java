package com.noxpvp.mmo.abilities.player;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

public class DisarmAbility extends BasePlayerAbility{
	
	private final static String ABILITY_NAME = "Disarm";
	private Player target;
	private HashSet<Material> weapons;
	
	public Player getTarget() {return target;}
	public DisarmAbility setTarget(Player target) {this.target = target; return this;}
	
	public HashSet<Material> getWeapons() {return this.weapons;}
	public DisarmAbility removeWeapon(Material weapon) {this.weapons.remove(weapon); return this;}
	public DisarmAbility addWeapon(Material weapon) {this.weapons.add(weapon); return this;}
	
	public DisarmAbility(Player player){
		super(ABILITY_NAME, player);
		weapons = new HashSet<Material>();
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player t = getTarget();
		
		if (getWeapons().contains(t.getItemInHand().getType())){
			//TODO t.add to a hashset for the other event
		}
		
		return true;
	}
	
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}

}
