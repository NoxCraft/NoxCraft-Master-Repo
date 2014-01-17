package nr.com.noxpvp.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.collections.StringMap;
import com.noxpvp.core.NoxPlugin;

public abstract class BaseCommand implements Command {
	private final boolean isPlayerOnly;
	private boolean isRoot;
	private final String name;
	private BaseCommand parent;
	
	private StringMap<BaseCommand> subCommands = new StringMap<BaseCommand>();
	
	private BaseCommand(BaseCommand parent, String name, boolean isPlayerOnly)
	{
		if (parent != null)
			isRoot = false;
		else
			isRoot = true;
		
		this.parent = parent;
		this.name = name;
		this.isPlayerOnly = isPlayerOnly;
	}
	
	/**
	 * @param name
	 * @param isPlayerOnly
	 */
	public BaseCommand(String name, boolean isPlayerOnly) {
		this(null, name, isPlayerOnly);
	}

	public final boolean containsSubComman(String name)
	{
		return subCommands.containsKeyLower(name);
	}

	public final boolean containsSubCommand(BaseCommand command) {
		return containsSubComman(command.getName());
	}
	
	public final BaseCommand getSubCommand(String name)
	{
		if (containsSubComman(name))
			return subCommands.getLower(name);
		return null;
	}

	public abstract boolean execute(CommandSender sender, CommandContext context) throws NoPermissionException;
	
	public final boolean executeCommand(CommandSender sender, CommandContext context) throws NoPermissionException {
		if (!hasSubCommands() || context.getArgumentCount() == 0)
			return execute(sender, context);
		
		String[] args = context.getArguments();
		
		String nextArg = context.getArgument(0);
		CommandContext newContext = new CommandContext(sender, context.getFlags(), Arrays.copyOfRange(args, 1, args.length-1));
		
		BaseCommand subCMD = getSubCommand(nextArg);
		if (subCMD != null)
			return subCMD.executeCommand(sender, newContext);
		return execute(sender, context);
	}
	
	public final String getFullName() {
		StringBuilder sb = new StringBuilder();
		BaseCommand last, current = this;
		while (current != null)
		{
			last = current;
			if (!last.equals(current))
				sb.insert(0, " ");
			
			current = current.getParent();
			sb.insert(0, last.getName());
		}
		
		return sb.toString();
	}
	
	public final String getName() {
		return name;
	}
	
	public final BaseCommand getParent() {
		return parent;
	}
	
	public abstract NoxPlugin getPlugin();
	
	public final BaseCommand getRoot() {
		if (getParent() == null)
			return this;
		else
			return getParent().getRoot();
	}
	
	public boolean hasArgumentLimit() {
		return getMaxArguments() < 0;
	}
	
	public final boolean hasSubCommands() { return subCommands.size() > 0; }
	
	public final boolean isPlayerOnly() {
		return isPlayerOnly;
	}
	
	public final boolean isRoot() {
		return isRoot;
	}
	
	public final void registerSubCommand(BaseCommand command) {
		if (containsSubCommand(command))
		{
			getPlugin().log(Level.WARNING, "Sub Command: " + command.getName() + " is already registered to command " + getName());
			return;
		}
		
		command.setParent(this);
		subCommands.putLower(command.getName(), command);
	}
	
	private void setParent(BaseCommand command) { this.parent = command; this.isRoot = false; }
}

