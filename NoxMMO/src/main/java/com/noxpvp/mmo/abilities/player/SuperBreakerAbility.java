package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.classes.PlayerClass;

/**
 * @author NoxPVP
 *
 */
public class SuperBreakerAbility extends BasePlayerAbility {
	
	public static final String PERM_NODE = "super-breaker";
	public static final String ABILITY_NAME = "Super Breaker";

	public SuperBreakerAbility(Player player){
		super(ABILITY_NAME, player);
	}
	
	public SuperBreakerAbility(NoxPlayerAdapter adapt){
		this(adapt.getNoxPlayer().getPlayer());
	}
	
	public boolean execute() {
		if (!mayExecute())
			return false;
		
		Player p = getPlayer();
		PlayerClass pClass = PlayerManager.getInstance().getPlayer(p).getPrimaryClass();
		
		int length = (20 * pClass.getTotalLevel()) / 16;
		return p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, length, 50));
	}

}
