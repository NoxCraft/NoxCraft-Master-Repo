package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

/**
 * @author NoxPVP
 *
 */
public class DisarmAbility extends BasePlayerAbility{
	
	public static List<Player> disarmedPlayers = new ArrayList<Player>();
	
	private static final String ABILITY_NAME = "Disarm";

	public static final String PERM_NODE = "disarm";
	private Player target;
	private HashSet<Material> weapons;
	
	/**
	 * 
	 * 
	 * @return Player The currently set target for this ability instance
	 */
	public Player getTarget() {return target;}
	
	/**
	 * 
	 * 
	 * @param target The Player type target that the disarm should apply to
	 * @return disarmAbility This instance, used for chaining
	 */
	public DisarmAbility setTarget(Player target) {this.target = target; return this;}
	
	/**
	 * 
	 * 
	 * @return HashSet The current list of weapons that the target must be holding
	 */
	public HashSet<Material> getWeapons() {return this.weapons;}
	
	/**
	 * 
	 * 
	 * @param weapon Material Type of weapon to remove from the list of weapons the target must for holding
	 * @return DisarmAbility This instance, used for chaining
	 */
	public DisarmAbility removeWeapon(Material weapon) {this.weapons.remove(weapon); return this;}
	
	/**
	 * 
	 * 
	 * @param weapon A Material type weapon to add to the list of weapons the target must be holding
	 * @return DisarmAbility This instance, used for chaining
	 */
	public DisarmAbility addWeapon(Material weapon) {this.weapons.add(weapon); return this;}
	
	/**
	 * 
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public DisarmAbility(Player player){
		super(ABILITY_NAME, player);
		weapons = new HashSet<Material>();
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the ability has successfully executed
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player t = getTarget();
		Material held = t.getItemInHand().getType();
		
		if (getWeapons().contains(held)){
			DisarmAbility.disarmedPlayers.add(getTarget());
		}
		
		return true;
	}
	
	/**
	 * 
	 * 
	 * @return Boolean If the execute() method will normally be able to start
	 */
	public boolean mayExecute() {
		if (getPlayer() == null || getTarget() == null)
			return false;
		
		return true;
	}

}
