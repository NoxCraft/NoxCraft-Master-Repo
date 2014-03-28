package com.noxpvp.noxchat;

public class ChannelManager {
	private static ChannelManager instance;

	public static ChannelManager getInstance() {
		if (instance == null)
			instance = new ChannelManager();
		return instance;
	}
	
	protected ChannelManager() {
		
	}
}
