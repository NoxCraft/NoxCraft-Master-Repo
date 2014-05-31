/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.mmo.filters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class PlayerClassFilter implements Filter<Player> {

	private List<String> classIds;

	private boolean inverse = false;

	public PlayerClassFilter(String... ids) {
		classIds = new ArrayList<String>();
		for (String id : ids) {
			if (PlayerClassUtil.hasClassId(id))
				classIds.add(id);
			else if (PlayerClassUtil.hasClassName(id))
				classIds.add(PlayerClassUtil.getIdByClassName(id));
		}
	}

	public boolean isFiltered(Player player) {
		MMOPlayer mPlayer = getMMOPlayer(player);

		IPlayerClass mainClass = mPlayer.getPrimaryClass();
		IPlayerClass subClass = mPlayer.getSecondaryClass();

		if (classIds.contains(mainClass.getUniqueID()))
			return !inverse;
		if (classIds.contains(subClass.getUniqueID()))
			return !inverse;

		return inverse;
	}

	private static MMOPlayer getMMOPlayer(Player player) {
		return MMOPlayerManager.getInstance().getPlayer(player);
	}
}
