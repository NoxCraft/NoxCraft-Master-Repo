package com.noxpvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.VaultAdapter;

public class MessageUtil {
	
	public static String parseColor(String message)
	{
		return StringUtil.ampToColor(message);
	}
	
	public static void broadcast(World world, String message)
	{
		for (Player player : world.getPlayers())
			sendMessage(player, message);
	}
	
	public static void broadcast(World world, String... messages)
	{
		for (Player player : world.getPlayers())
			sendMessage(player, messages);
	}
	
	public static void broadcast(World world, String permission, String message)
	{
		sendMessage(world.getPlayers().toArray(new Player[0]), permission, message);
	}
	
	public static void broadcast(World world, String permission, String... messages)
	{
		sendMessage(world.getPlayers().toArray(new Player[0]), permission, messages);	
	}
	
	
	
	public static void broadcast(String permission, String message)
	{
		sendMessage(Bukkit.getOnlinePlayers(), permission , message);
	}
	
	public static void broadcast(String permission, String... messages)
	{
		sendMessage(Bukkit.getOnlinePlayers(), permission , messages);
	}
	
	public static void sendMessageToGroup(String groupName, String message)
	{
		for (Player player : Bukkit.getOnlinePlayers())
			if (VaultAdapter.permission.playerInGroup(player.getWorld(), player.getName(), groupName))
				sendMessage(player, message);
	}
	
	public static void sendMessageToGroup(String groupName, String... messages)
	{
		Player[] players = Bukkit.getOnlinePlayers();
		if (players.length > messages.length) {
			for (String message : messages)
				for (Player player : players)
					if (VaultAdapter.permission.playerInGroup(player.getWorld(), player.getName(), groupName))
						sendMessage(player, message);
		} else
			for (Player player : players)
				if (VaultAdapter.permission.playerInGroup(player.getWorld(), player.getName(), groupName))
					for (String message : messages)
						sendMessage(player, message);
	}

	public static void sendMessageNearby(Entity entity, double radius, String message)
	{
		sendMessageNearby(entity, radius, radius, radius, message);
	}
	
	public static void sendMessageNearby(Entity entity, double radX, double radY, double radZ, String message)
	{
		for (Entity e : WorldUtil.getNearbyEntities(entity, radX, radY, radZ))
			if (e instanceof CommandSender)
					((CommandSender)e).sendMessage(message);
	}
	
	public static void sendMessageNearby(Location location, double radius, String message)
	{
		sendMessageNearby(location, radius, radius, radius, message);
	}
	
	public static void sendMessageNearby(Location location, double radX, double radY, double radZ, String message)
	{
		for (Entity e : WorldUtil.getNearbyEntities(location, radX, radY, radZ))
			if (e instanceof CommandSender)
					((CommandSender)e).sendMessage(message);
	}
	
	public static void sendMessage(CommandSender[] senders, String message)
	{
		for (CommandSender sender : senders)
			sender.sendMessage(message);
	}
	
	public static void sendMessage(CommandSender[] senders, String permission, String message)
	{
		for (CommandSender sender : senders)
			if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.has(sender, permission))
				sendMessage(sender, message);
			else if (sender.hasPermission(permission))
				sendMessage(sender, message);
	}
	
	public static void sendMessage(CommandSender[] senders, String... messages)
	{
		for (CommandSender sender : senders)
			sendMessage(sender, messages);
	}
	
	public static void sendMessage(CommandSender[] senders, String permission, String... messages)
	{
		for (CommandSender sender : senders)
			if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.has(sender, permission))
				sendMessage(sender, messages);
			else if (sender.hasPermission(permission))
				sendMessage(sender, messages);
	}
	
	public static void sendMessage(CommandSender sender, String message)
	{
		sender.sendMessage(message);
	}
	
	public static void sendMessage(CommandSender sender, String...messages)
	{
		for (String message : messages)
			sender.sendMessage(message);
	}
	
	public static void sendLocale(NoxPlugin plugin, CommandSender sender, String locale, String... params)
	{
		sender.sendMessage(parseColor(plugin.getLocale(locale, params)));
	}
	
	public static void sendGlobalLocale(NoxPlugin plugin, CommandSender sender, String locale, String... params) 
	{
		sender.sendMessage(parseColor(plugin.getGlobalLocale(locale, params)));
	}
}
	
