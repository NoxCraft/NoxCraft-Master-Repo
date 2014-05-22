package com.noxpvp.core.commands;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommandContext {
	public String getArgument(int arg);

	public int getArgumentCount();

	public String[] getArguments();

	public Object getFlag(String flag);

	public <T> T getFlag(String flag, Class<T> clazz);

	public <T> T getFlag(String flag, T def);

	public int getFlagCount();

	public Map<String, Object> getFlags();

	public Player getPlayer();

	public CommandSender getSender();

	public boolean hasArgument(int argNumber);

	public boolean hasFlag(String flag);

	public boolean isPlayer();
}
