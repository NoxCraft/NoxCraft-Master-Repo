package com.noxpvp.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.collections.StringMap;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.locales.GlobalLocale;
import com.noxpvp.core.utils.MessageUtil;

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

	public abstract boolean execute(CommandContext context) throws NoPermissionException;
	
	public final boolean executeCommand(CommandContext context) throws NoPermissionException {
		if (!hasSubCommands() || context.getArgumentCount() == 0)
			return execute(context);
		
		String[] args = context.getArguments();
		
		String nextArg = context.getArgument(0);
		CommandContext newContext = new CommandContext(context.getSender(), context.getFlags(), Arrays.copyOfRange(args, 1, args.length-1));
		
		BaseCommand subCMD = getSubCommand(nextArg);
		if (subCMD != null)
			return subCMD.executeCommand(newContext);
		return execute(context);
	}
	
	protected StringMap<BaseCommand> getSubCommandMap() { return subCommands; }
	
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
	
	public final void registerSubCommands(BaseCommand... commands)
	{
		for (BaseCommand c : commands)
			registerSubCommand(c);
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

	public static final String COMMAND_NAME = "reloader";

	public final void displayHelp(CommandSender sender) {
		MessageBuilder mb = new MessageBuilder();
		
		mb.setSeparator("\n");
		mb.newLine();
		for (String line : GlobalLocale.HELP_HEADER.get(getPlugin().getName(), COMMAND_NAME).split("\n"))
			mb.append(line);
		for (String line : getHelp())
			mb.append(line);
		
		MessageUtil.sendMessage(sender, mb.lines());
	}
}

