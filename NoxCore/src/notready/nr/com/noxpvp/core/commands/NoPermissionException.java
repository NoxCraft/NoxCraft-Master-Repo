package nr.com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

public class NoPermissionException extends RuntimeException {
	private CommandSender sender;
	private String permission;
	private static final long serialVersionUID = 8910717124994314558L;
	
	public NoPermissionException(CommandSender sender, String permission, String message) {
		super(message);
		this.sender = sender;
		this.permission = permission;
	}

	/**
	 * @return the sender
	 */
	public synchronized final CommandSender getSender() {
		return sender;
	}

	/**
	 * @return the permission
	 */
	public synchronized final String getPermission() {
		return permission;
	}
}
