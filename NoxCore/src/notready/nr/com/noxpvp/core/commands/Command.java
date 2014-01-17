package nr.com.noxpvp.core.commands;

import org.bukkit.command.CommandSender;

import com.noxpvp.core.commands.CommandContext;
import com.noxpvp.core.commands.NoPermissionException;

public interface Command {
	public String getName();
	
	public boolean isRoot();
	
	public String[] getFlags();
	
	public boolean hasArgumentLimit();
	
	public int getMaxArguments();
	
	public String[] getHelp();
	
	public boolean isPlayerOnly();
	
	public Command getParent();
	
	public Command getRoot();
	
	public boolean executeCommand(CommandSender sender, CommandContext context) throws NoPermissionException;
}
