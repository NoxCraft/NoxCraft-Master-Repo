package com.noxpvp.mmo.abilities.player;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.abilities.BasePlayerAbility;

//FIXME not made yet

public class JoustPlayerAbility extends BasePlayerAbility implements PVPAbility {

	public static final String ABILITY_NAME = "Joust";
	public static final String PERM_NODE = "joust";

	private EntityDamageByEntityEvent event;

	public JoustPlayerAbility(Player p, EntityDamageByEntityEvent event) {
		super(ABILITY_NAME, p);

		this.event = event;
	}

	public AbilityResult execute() {

		throw new NotImplementedException();

//		MMOPlayer mmoP;
//		PlayerClass clazz = (mmoP = PlayerManager.getInstance().getPlayer(getPlayer())) != null? mmoP.getPrimaryClass() : null;
//		
//		if (clazz == null) return false;
//		
//		event.setDamage(event.getDamage() + (((clazz.getTotalLevel() * 100) + clazz.getLevel()) / 75));
//		
//		return true;
	}

}
