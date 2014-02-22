package com.noxpvp.noxchat;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.utils.chat.MessageUtil;

public class ChatPlayer extends BaseNoxPlayerAdapter implements Targetable {

	/**
	 * @param player
	 */
	public ChatPlayer(NoxPlayerAdapter player) {
		super(player);
	}

	/**
	 * @param player
	 */
	public ChatPlayer(OfflinePlayer player) {
		super(player);
	}

	/**
	 * @param name
	 */
	public ChatPlayer(String name) {
		super(name);
	}

	public void load() {
		// TODO Auto-generated method stub

	}

	public void save() {
		// TODO Auto-generated method stub

	}

	public Targetable getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setTarget(Targetable targets) {
		
	}

	public void sendMessage(String message) {
		if (isOnline())
			MessageUtil.sendMessage(getPlayer(), message);
	}

	public void sendMessage(String... messages) {
		if (isOnline())
			MessageUtil.sendMessage(getPlayer(), messages);
	}
	
	public void sendTargetMessage(String message) {
		getTarget().sendMessage(message);
	}
	
	public void sendTargetMessage(String... messages) {
		getTarget().sendMessage(messages);
	}
}
