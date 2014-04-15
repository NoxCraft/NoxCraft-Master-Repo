package com.noxpvp.mmo.abilities.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;

/**
 * @author NoxPVP
 *
 */
public class ParryAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent>, PVPAbility{
	
	public static final String ABILITY_NAME = "Parry";
	public static final String PERM_NODE = "parry";
	
	public List<Material> parriedWeapons= new ArrayList<Material>();
	
	private float percentChance;
	private boolean mustBlock;

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
		
		this.mustBlock = false;
	}
	
	public boolean execute() { return true; }

	public boolean execute(EntityDamageByEntityEvent event){
		if (event.getEntity() != getPlayer() || !mayExecute()) return false;
		
		Player p = getPlayer();
		
		MMOPlayer mmoPlayer;
		
		if ((mmoPlayer = PlayerManager.getInstance().getPlayer(p)) == null || (mustBlock && (!parriedWeapons.contains(p.getItemInHand().getType()))))
				return false;
		
		float chance = mmoPlayer.getPrimaryClass().getTotalLevel() / 6;
		percentChance = (chance <= 75)? chance : 75;
		if (RandomUtils.nextFloat() > percentChance) return false;
		
		
		Entity damager = event.getDamager();
		if (damager instanceof Damageable){
			((Damageable) damager).damage(event.getDamage() / .7, p);
		}
		
		return true;
	}
	
}
