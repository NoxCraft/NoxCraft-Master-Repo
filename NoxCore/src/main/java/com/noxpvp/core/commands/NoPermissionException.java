package com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public class NoPermissionException extends RuntimeException {
	private static final long serialVersionUID = 8910717124994314558L;
	private String permission;
	private CommandSender sender;

	public NoPermissionException(CommandSender sender, String permission, String message) {
		super(message);
		this.sender = sender;
		this.permission = permission;
	}

	/**
	 * @return the permission
	 */
	public synchronized final String getPermission() {
		return permission;
	}

	/**
	 * @return the sender
	 */
	public synchronized final CommandSender getSender() {
		return sender;
	}
}
