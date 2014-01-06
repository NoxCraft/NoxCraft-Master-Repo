package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class CriticalHitAbility extends BasePlayerAbility implements PassiveAbility{

	public static final String PERM_NODE = "Critical Hit";
	public static final String ABILITY_NAME = "critical-hit";
	private EntityDamageEvent event;
	
	public CriticalHitAbility(Player p, EntityDamageEvent event) {
		super(ABILITY_NAME, p);
		
		this.event = event;
	}

	@Override
	public boolean execute() { 
		if (!mayExecute())
			return false;
		
		MMOPlayer player = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer());
		
		if (player == null)
			return false;
		
		PlayerClass clazz = player.getMainPlayerClass();
		
		double damage = (clazz.getLevel() + clazz.getTotalLevels()) / 75;
		if ((Math.random() * 100) > (damage * 45)) return false;
		
		event.setDamage(damage);
		
		return true;
	}

}
