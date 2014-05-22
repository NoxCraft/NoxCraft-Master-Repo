package com.noxpvp.mmo.abilities.targeted;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;
import com.noxpvp.mmo.abilities.IPVPAbility;

public class TracerArrowPlayerAbility extends BaseTargetedPlayerAbility implements IPVPAbility {

	public static final String PERM_NODE = "tracer-arrow";
	private static final String ABILITY_NAME = "Tracer Arrow";
	static Map<String, TracerArrowPlayerAbility> abilityCue = new HashMap<String, TracerArrowPlayerAbility>();

	public TracerArrowPlayerAbility(Player player) {
		super(ABILITY_NAME, player, MMOPlayerManager.getInstance().getPlayer(player).getTarget());
	}

	/**
	 * Runs the event-side execution of this ability
	 *
	 * @param player Player, normally arrow shooter from a projectile hit event
	 * @return boolean If the execution ran successfully
	 */
	public static boolean eventExecute(Player player, final Arrow arrow) {
		MMOPlayer mmoP = MMOPlayerManager.getInstance().getPlayer(player);
		String name = player.getName();

		if (abilityCue.containsKey(name))
			return false;

		final LivingEntity target = mmoP.getTarget();

		Bukkit.getScheduler().runTaskTimer(NoxMMO.getInstance(), new BukkitRunnable() {

			private Vector arrowLoc = arrow.getLocation().toVector();
			private Vector los;

			public void safeCancel() {
				try {
					cancel();
				} catch (IllegalStateException e) {
				}
			}

			public void run() {
				if (arrow == null || !arrow.isValid() || target == null) {
					safeCancel();
					return;
				}

				los = target.getLocation().toVector().subtract(arrowLoc);
				arrow.setVelocity(los);
			}
		}, 0, 1);
		return true;
	}

	/**
	 * @return boolean - If the ability executed successfully
	 */
	public boolean execute() {
		if (!mayExecute())
			return false;

		final String pName = getPlayer().getName();

		TracerArrowPlayerAbility.abilityCue.put(pName, this);
		Bukkit.getScheduler().runTaskLater(NoxMMO.getInstance(), new Runnable() {

			public void run() {

				if (TracerArrowPlayerAbility.abilityCue.containsKey(pName))
					TracerArrowPlayerAbility.abilityCue.remove(pName);

			}
		}, 120);

		return true;
	}

}
