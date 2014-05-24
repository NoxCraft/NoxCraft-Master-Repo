package com.noxpvp.mmo.abilities.ranged;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;
import com.noxpvp.mmo.abilities.IPassiveAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class SeveringStrikesPlayerAbility extends BasePlayerAbility implements IPassiveAbility<EntityDamageByEntityEvent>, IPVPAbility {

	public static final String ABILITY_NAME = "Severing Strikes";
	public static final String PERM_NODE = "severing-strikes";

	private int bleed;

	/**
	 * @param player The user of the ability instance
	 */
	public SeveringStrikesPlayerAbility(Player player) {
		super(ABILITY_NAME, player);

	}

	public AbilityResult execute() {
		return new AbilityResult(this, true);
	}

	public AbilityResult execute(EntityDamageByEntityEvent event) {
		if (!mayExecute() || event.getDamager() != getPlayer())
			return new AbilityResult(this, false);

		Entity damaged = event.getEntity();
		Player p = getPlayer();

		if (!(damaged instanceof Damageable))
			return new AbilityResult(this, false);

		int levels = MMOPlayerManager.getInstance().getPlayer(p).getPrimaryClass().getTotalLevel();
		this.bleed = (20 * levels) / 16;

		new DamageRunnable((Damageable) damaged, p, 1 * (1 + ((bleed / 20) / 6)), (bleed / 20) / 3).runTaskTimer(NoxMMO.getInstance(), 30, 30);

		return new AbilityResult(this, true);
	}

}
