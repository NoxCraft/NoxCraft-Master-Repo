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
