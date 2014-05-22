package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class RazerClawsPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, IPVPAbility{

	public static final String ABILITY_NAME = "Razer Claws";
	public static final String PERM_NODE = "razer-claws";
	
	private Damageable target;
	private NoxMMO mmo;
	
	public RazerClawsPlayerAbility(Player p, Damageable target) {
		super(ABILITY_NAME, p);
		
		this.target = target;
		this.mmo = NoxMMO.getInstance();
	}

	public boolean execute() { return true; }

	public boolean execute(EntityDamageByEntityEvent event) {

		if (event.getEntity() != getPlayer() || !mayExecute())
			return false;
		
		IPlayerClass clazz = MMOPlayerManager.getInstance().getPlayer(getPlayer()).getPrimaryClass();
		
		int levels = clazz.getTotalLevel();
		
		float chance = (levels / 20) <= 20? levels / 20 : 20;
		if (Math.random() > (chance)) return false;
		
		new DamageRunnable(target, getPlayer(), ((levels / 90) <= 5? levels / 90 : 90), (int) levels / 70).runTaskTimer(mmo, 0, 30);
		
		return true;
	}

}
