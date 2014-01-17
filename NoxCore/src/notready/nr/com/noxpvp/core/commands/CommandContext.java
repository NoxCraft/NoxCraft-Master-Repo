package nr.com.noxpvp.core.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.conversion.Conversion;

public class CommandContext implements ICommandContext {
	private Map<String, Object> flags;
	private String[] args;
	private CommandSender sender;
	
	public CommandContext(CommandSender sender, Map<String, Object> flags, String... args)
	{
		this.flags = new HashMap<String, Object>(flags);
		this.sender = sender;
		this.args = args;
	}
	
	public boolean hasArgument(int argNumber) {
		return getArgumentCount() > argNumber;
	}
	
	public Map<String, Object> getFlags() {
		return Collections.unmodifiableMap(flags);
	}

	public String getArgument(int arg) {
		if (arg >= getArgumentCount())
			throw new IndexOutOfBoundsException("Argument does not exist! Maximum Index of "+ (getArgumentCount() - 1) +"!");
		return args[arg];
	}

	public String[] getArguments() {
		return args;
	}

	public int getArgumentCount() {
		return args.length;
	}

	public int getFlagCount() {
		return flags.size();
	}

	public boolean hasFlag(String flag) {
		return flags.containsKey(flag);
	}

	public <T> T getFlag(String flag, T def) {
		if (hasFlag(flag))
			return Conversion.convert(flags.get(flag), def);
		return def;
	}
	
	public <T> T getFlag(String flag, Class<T> type) {
		if (hasFlag(flag))
			return Conversion.convert(flags.get(flag), type);
		return null;
	}

	public CommandSender getSender() {
		return sender;
	}

	public boolean isPlayer() {
		return getSender() instanceof Player;
	}

	public Player getPlayer() {
		if (isPlayer())
			return (Player) getSender();
		return null;
	}

	public Object getFlag(String flag) {
		if (hasFlag(flag))
			return flags.get(flag);
		else
			return null;
	}
	
	
}
