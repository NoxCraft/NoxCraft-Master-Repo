package com.noxpvp.noxchat.channel;

import org.bukkit.entity.Player;

import com.noxpvp.noxchat.ChatPlayer;
import com.noxpvp.noxchat.Targetable;

public class RangedChannel extends BaseChannel {
	private String name;
	private double range;
	
	public RangedChannel(String name, double range) {
		this.name = name;
		this.range = range;
	}
	
	@Override
	public boolean canHear(Targetable from, Targetable target) {
		final boolean superHear = super.canHear(from, target);
		if (superHear == false)
			return false;
		
		if (target instanceof ChatPlayer) {
			ChatPlayer player = (ChatPlayer) target;
			Player p = player.getPlayer();
			if (p == null)
				return false;
			
		}
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	protected String getChannelType() {
		return "Ranged";
	}

	public boolean isMuted(Targetable target) {
		// TODO Auto-generated method stub
		return false;
	}
}
