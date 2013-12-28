package com.noxpvp.mmo.filters;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.NoxMMO;
import com.noxpvp.mmo.classes.PlayerClass;

public class PlayerClassFilter implements Filter<Player> {
	
	private List<Integer> classIds;
	
	private boolean inverse = false;
	
	public PlayerClassFilter(Integer... ids)
	{
		classIds = Arrays.asList(ids);
	}
	
	public boolean isFiltered(Player player) {
		MMOPlayer mPlayer = getMMOPlayer(player);
		
		PlayerClass mainClass = mPlayer.getMainPlayerClass();
		PlayerClass subClass = mPlayer.getSubPlayerClass();
		
		if (classIds.contains(mainClass.getId()))
			return inverse? false : true;
		if (classIds.contains(subClass.getId()))
			return inverse? false : true;
		
		return inverse;
	}
	
	private static MMOPlayer getMMOPlayer(Player player)
	{
		return NoxMMO.getInstance().getPlayerManager().getMMOPlayer(player);
	}
}
