package com.noxpvp.noxchat;

import java.util.List;

import org.bukkit.OfflinePlayer;

import com.noxpvp.core.data.BaseNoxPlayerAdapter;
import com.noxpvp.core.data.NoxPlayerAdapter;
import com.noxpvp.core.utils.gui.MessageUtil;

public class ChatPlayer extends BaseNoxPlayerAdapter implements Targetable {

	public ChatPlayer(NoxPlayerAdapter player) {
		super(player);
	}

	public ChatPlayer(OfflinePlayer player) {
		super(player);
	}

	public ChatPlayer(String name) {
		super(name);
	}

	private Targetable target;
	private List<String> mutes;
	
	public Targetable getTarget() {
		return this.target;
	}
	
	public void setTarget(Targetable target) {
		this.target = target;
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
		if (getTarget() != null)
			getTarget().sendMessage(message);
	}
	
	public void sendTargetMessage(String... messages) {
		if (getTarget() != null)
			getTarget().sendMessage(messages);
	}

	public final String getName() {
		return getPlayerName();
	}

	public final String getType() {
		return "Player";
	}

	public boolean isMuted(Targetable target) {
		if (!(target instanceof ChatPlayer))
			return false;

		return mutes.contains(target.getName());
	}

	public void sendTargetMessage(Targetable from, String message) {
		if (getTarget() != null)
			getTarget().sendMessage(from, message);
	}

	public void sendTargetMessage(Targetable from, String... messages) {
		if (getTarget() != null)
			getTarget().sendMessage(from, messages);
	}

	public void sendMessage(Targetable from, String message) {
		if (!isMuted(from))
			sendMessage(message);
	}

	public void sendMessage(Targetable from, String... messages) {
		if (!isMuted(from))
			sendMessage(messages);
	}

	@Override
	public void save() {
		Targetable target = getTarget();
		if (target == null)
			getPersistantData().remove("chat.target");
		else {
			getPersistantData().set("chat.target.type", target.getType());
			getPersistantData().set("chat.target.name", target.getName());
		}
	}

	@Override
	public void load() {
		
	}
}
