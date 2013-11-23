package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;

/**
 * @author NoxPVP
 *
 */
public class ParryAbility extends BasePlayerAbility implements PassiveAbility{
	
	private static final String ABILITY_NAME = "Parry";
	public static final String PERM_NODE = "parry";
	
	public List<Material> parriedWeapons= new ArrayList<Material>();
	private Player e;
	private float percentChance;
	private boolean mustBlock = true;
	
	/**
	 * 
	 * @return Entity The currently set target for this ability instance
	 */
	public Player getE() {return e;}
	
	/**
	 * 
	 * @param e the target to set for this ability instance
	 * @return ParryAbility This instance, used for chaining
	 */
	public ParryAbility setE(Player e) {this.e = e; return this;}

	/**
	 * 
	 * @return double Get the currently set percentage for this ability's chance of success
	 */
	public float getPercentChance() {return percentChance; }

	/**
	 * 
	 * @param percentChance The chance to set for this ability's success
	 * @return ParryAbility This instance, used for chaining
	 */
	public ParryAbility setPercentChance(float percentChance) {this.percentChance = percentChance; return this;}

	/**
	 * 
	 * @return boolean If this ability is set to only succeed if the user is blocking with a sword
	 */
	public boolean isMustBlock() {return mustBlock;}

	/**
	 * 
	 * @param mustBlock boolean if the ability should only succeed if the player is blocking with a sword
	 * @return ParryAbility This instance, used for chaining
	 */
	public ParryAbility setMustBlock(boolean mustBlock) {this.mustBlock = mustBlock; return this;}

	/**
	 * 
	 * @param player The Player type user for this ability instance
	 */
	public ParryAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	/**
	 * 
	 * @return boolean If the ability executed successfully
	 */
	public boolean execute(){
		if (!mayExecute()) return false;
		
		Player p = getPlayer();
		Material i = getE().getItemInHand().getType();
		
		if (!parriedWeapons.contains(i)) return false;
		if (mustBlock && !p.isBlocking()) return false;
		if (RandomUtils.nextFloat() > percentChance) return false;
		
		return true;
	}
	
	/**
	 * 
	 * @return boolean If the execute() method will normally be able to execute
	 */
	public boolean mayExecute(){
		return getPlayer() != null;
	}
	
}
