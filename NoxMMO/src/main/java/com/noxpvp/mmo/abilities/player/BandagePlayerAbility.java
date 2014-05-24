package com.noxpvp.mmo.abilities.player;

import org.bukkit.entity.Player;

import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.abilities.BasePlayerAbility;
import com.noxpvp.mmo.runnables.DamageRunnable;

public class BandagePlayerAbility extends BasePlayerAbility {

	public static final String ABILITY_NAME = "Bandage";
	public static final String PERM_NODE = "bandage";
	private int delay;

	public BandagePlayerAbility(Player p) {
		super(ABILITY_NAME, p);

		this.delay = 10 * 20;
	}

	public int getDelay() {
		return delay;
	}

	public BandagePlayerAbility setDelay(int delay) {
		this.delay = delay;
		return this;
	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Player p = getPlayer();

		p.setHealth(p.getMaxHealth());

		DamageRunnable wearOff = new DamageRunnable(p, p, (p.getMaxHealth() / 10), 10);
		wearOff.runTaskTimer(NoxMMO.getInstance(), delay, 15);

		return new AbilityResult(this, true);
	}

}
