package com.noxpvp.mmo.abilities.targeted;

import com.noxpvp.mmo.abilities.PVPAbility;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.abilities.BaseTargetedPlayerAbility;

/**
 * @author NoxPVP
 */
public class ShadowStepPlayerAbility extends BaseTargetedPlayerAbility implements PVPAbility {

	public static final String PERM_NODE = "shadow-step";
	public static final String ABILITY_NAME = "Shadow Step";

	/**
	 * @param player the Player type user for this ability instance
	 */
	public ShadowStepPlayerAbility(Player player, double range) {
		super(ABILITY_NAME, player, range, MMOPlayerManager.getInstance().getPlayer(player).getTarget());

	}

	public AbilityResult execute() {
		if (!mayExecute())
			return new AbilityResult(this, false);

		Location targetLoc = getTarget().getLocation();
		Vector facing = targetLoc.getDirection().setY(0).multiply(-1);
		Location loc = targetLoc.toVector().add(facing).toLocation(targetLoc.getWorld());
		loc.setPitch(0);
		loc.setYaw(targetLoc.getYaw());


		Block b = loc.getBlock();
		if (!(!b.getType().isSolid() || b.getRelative(BlockFace.UP).getType().isSolid())) {
			return new AbilityResult(this, false);
		}

		getPlayer().teleport(loc);

		return new AbilityResult(this, true);
	}

}
