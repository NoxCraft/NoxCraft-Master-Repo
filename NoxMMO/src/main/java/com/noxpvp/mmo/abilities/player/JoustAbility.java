package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;

public class JoustAbility extends BasePlayerAbility implements PassiveAbility{

	public final static String ABILITY_NAME = "Joust";
	public final static String PERM_NODE = "joust";
	
	private EntityDamageByEntityEvent event;
	
	public JoustAbility(Player p, EntityDamageByEntityEvent event) {
		super(ABILITY_NAME, p);
		
		this.event = event;
	}

	public boolean execute() {
		
		MMOPlayer mmoP;
		PlayerClass clazz = (mmoP = NoxMMO.getInstance().getPlayerManager().getMMOPlayer(getPlayer())) != null? mmoP.getMainPlayerClass() : null;
		
		if (clazz == null) return false;
		
		event.setDamage(event.getDamage() + (((clazz.getTierLevel() * 100) + clazz.getLevel()) / 75));
		
		return true;
	}

}
