package com.noxpvp.noxchat;

public class TargetManager {
	public static Targetable getTarget(String type, String name) {
		if (type.equals("Player")) {
			return getPlayerManager().getPlayer(name);
		} else if (type.startsWith("Channel")) {
			//TODO: Add channel support.
		}

		return null;
	}

	public static ChannelManager getChannelManager() {
		return ChannelManager.getInstance();
	}

	public static PlayerManager getPlayerManager() {
		return PlayerManager.getInstance();
	}
}
