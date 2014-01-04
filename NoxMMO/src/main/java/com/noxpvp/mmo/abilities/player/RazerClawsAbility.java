package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.PassiveAbility;
import com.noxpvp.mmo.classes.PlayerClass;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class RazerClawsAbility extends BasePlayerAbility implements PassiveAbility{

	public final static String ABILITY_NAME = "Razer Claws";
	public final static String PERM_NODE = "razer-claws";
	
	private Damageable target;
	private NoxMMO mmo;
	
	public RazerClawsAbility(Player p, Damageable target) {
		super(ABILITY_NAME, p);
		
		this.target = target;
		this.mmo = NoxMMO.getInstance();
	}

	public boolean execute() {
		if (!mayExecute())
			return false;
		
		PlayerClass clazz = mmo.getPlayerManager().getMMOPlayer(getPlayer()).getMainPlayerClass();
		
		int levels = clazz.getTotalLevels();
		
		if (Math.random() > (levels / 20)) return false;
		
		new DamageRunnable(target, getPlayer(), levels / 80, (int) levels / 70).runTaskTimer(mmo, 0, 30);
		
		return true;
	}

}
