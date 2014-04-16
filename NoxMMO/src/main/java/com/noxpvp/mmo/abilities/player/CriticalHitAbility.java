package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PVPAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.internal.PlayerClass;

public class CriticalHitAbility extends BasePlayerAbility implements PassiveAbility<EntityDamageByEntityEvent>, PVPAbility {

	public static final String PERM_NODE = "Critical Hit";
	public static final String ABILITY_NAME = "critical-hit";
	
	private PlayerManager pm;
	
	public CriticalHitAbility(Player p) {
		super(ABILITY_NAME, p);
		
		this.pm = PlayerManager.getInstance();
	}
	
	public boolean execute(EntityDamageByEntityEvent event) {
		if (!mayExecute())
			return false;
		
		Player playerAttacker = (Player) ((event.getDamager() instanceof Player)? event.getDamager(): null);
		String itemName = playerAttacker.getItemInHand().getType().name().toUpperCase();
		
		if (!itemName.contains("SWORD") && !itemName.contains("AXE"))
			return false;
		
		if (playerAttacker == null || !playerAttacker.equals(getPlayer()))
			return false;
		
		MMOPlayer player = pm.getPlayer(getPlayer());
		
		if (player == null)
			return false;
		
		PlayerClass clazz = player.getPrimaryClass();
		
		double damage = (clazz.getLevel() + clazz.getTotalLevel()) / 75;
		if ((Math.random() * 100) > (damage * 45)) return false;
		
		event.setDamage(damage);
		return true;
	}
	
	/**
	 * Always Returns True Due To Being Passive!
	 */
	public boolean execute() { 
		return true; //Passive
	}

}
