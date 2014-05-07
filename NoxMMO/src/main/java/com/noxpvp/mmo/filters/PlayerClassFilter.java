package com.noxpvp.mmo.filters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.internal.IPlayerClass;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class PlayerClassFilter implements Filter<Player> {
	
	private List<String> classIds;
	
	private boolean inverse = false;
	
	public PlayerClassFilter(String... ids)
	{
		classIds = new ArrayList<String>();
		for (String id: ids)
		{
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
			return inverse? false : true;
		if (classIds.contains(subClass.getUniqueID()))
			return inverse? false : true;
		
		return inverse;
	}
	
	private static MMOPlayer getMMOPlayer(Player player)
	{
		return PlayerManager.getInstance().getPlayer(player);
	}
}
