package nr.com.noxpvp.core.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommandContext {
	public CommandSender getSender();
	
	public boolean isPlayer();
	
	public Player getPlayer();
	
	public Map<String, Object> getFlags();
	
	public boolean hasArgument(int argNumber);
	
	public String getArgument(int arg);
	
	public String[] getArguments();
	
	public int getArgumentCount();
	
	public int getFlagCount();
	
	public boolean hasFlag(String flag);
	
	public <T> T getFlag(String flag, Class<T> clazz);
	
	public <T> T getFlag(String flag, T def);
	
	public Object getFlag(String flag);
}
