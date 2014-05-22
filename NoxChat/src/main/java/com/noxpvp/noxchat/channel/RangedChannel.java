package com.noxpvp.noxchat.channel;

import com.noxpvp.noxchat.Targetable;

public class RangedChannel extends BaseChannel {
	private String name;
	private double range;

	public RangedChannel(String name, double range) {
		this.name = name;
		this.range = range;
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
