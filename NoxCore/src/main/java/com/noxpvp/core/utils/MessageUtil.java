package com.noxpvp.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.bergerkiller.bukkit.common.filtering.Filter;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.noxpvp.core.NoxPlugin;
import com.noxpvp.core.VaultAdapter;

public class MessageUtil {
	
	public static void broadcast(final String permission, String message)
	{
		sendMessage(Bukkit.getOnlinePlayers(), permission , message);
	}
	
	public static void broadcast(final String permission, String... messages)
	{
		sendMessage(Bukkit.getOnlinePlayers(), permission , messages);
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
	
	public static void broadcast(World world,final String permission, String message)
	{
		sendMessage(world.getPlayers().toArray(new Player[0]), permission, message);
	}
	
	public static void broadcast(World world,final String permission, String... messages)
	{
		sendMessage(world.getPlayers().toArray(new Player[0]), permission, messages);	
	}
	
	public static String parseColor(String message)
	{
		return StringUtil.ampToColor(message);
	}
	
	public static void sendGlobalLocale(NoxPlugin plugin, CommandSender sender, String locale, String... params) 
	{
		sender.sendMessage(parseColor(plugin.getGlobalLocale(locale, params)));
	}
	
	public static void sendLocale(NoxPlugin plugin, CommandSender sender, String locale, String... params)
	{
		sender.sendMessage(parseColor(plugin.getLocale(locale, params)));
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
	
	public static void sendMessage(CommandSender[] senders, Filter<CommandSender> filter, String message)
	{
		for (CommandSender sender : senders)
			if (filter.isFiltered(sender))
				sendMessage(sender, message);
	}
	
	public static void sendMessage(CommandSender[] senders, Filter<CommandSender> filter, String... messages)
	{
		for (CommandSender sender : senders)
			if (filter.isFiltered(sender))
				sendMessage(sender, messages);
	}

	public static void sendMessage(CommandSender[] senders, String message)
	{
		for (CommandSender sender : senders)
			sender.sendMessage(message);
	}
	
	public static void sendMessage(CommandSender[] senders, String... messages)
	{
		for (CommandSender sender : senders)
			sendMessage(sender, messages);
	}
	
	public static void sendMessage(CommandSender[] senders,final String permission, String message)
	{
		sendMessage(senders, new Filter<CommandSender>() {
			public boolean isFiltered(CommandSender sender) {
				if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.has(sender, permission))
					return true;
				else if (sender.hasPermission(permission))
					return true;
				return false;
			}
		}, message);
	}
	
	public static void sendMessage(CommandSender[] senders, final String permission, String... messages)
	{
		sendMessage(senders, new Filter<CommandSender>() {
			public boolean isFiltered(CommandSender sender) {
				if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.has(sender, permission))
					return true;
				else if (sender.hasPermission(permission))
					return true;
				return false;
			}
		}, messages);
	}
	
	public static void sendMessage(Player[] players, Filter<Player> filter, String message)
	{
		for (Player player : players)
			if (filter.isFiltered(player))
				sendMessage(player, message);
	}
	
	public static void sendMessage(Player[] players, Filter<Player> filter, String... messages)
	{
		for (Player player : players)
			if (filter.isFiltered(player))
				sendMessage(player, messages);
	}
	
	public static void sendMessageNearby(Entity entity, double radX, double radY, double radZ, String message)
	{
		for (Entity e : WorldUtil.getNearbyEntities(entity, radX, radY, radZ))
			if (e instanceof CommandSender)
					((CommandSender)e).sendMessage(message);
	}
	
	public static void sendMessageNearby(Entity entity, double radius, String message)
	{
		sendMessageNearby(entity, radius, radius, radius, message);
	}
	
	public static void sendMessageNearby(Location location, double radX, double radY, double radZ, String message)
	{
		for (Entity e : WorldUtil.getNearbyEntities(location, radX, radY, radZ))
			if (e instanceof CommandSender)
					((CommandSender)e).sendMessage(message);
	}
	
	public static void sendMessageNearby(Location location, double radius, String message)
	{
		sendMessageNearby(location, radius, radius, radius, message);
	}
	
	public static void sendMessageToGroup(final String groupName, String message)
	{
		sendMessage(Bukkit.getOnlinePlayers(), new Filter<Player>() {
			public boolean isFiltered(Player player) {
				if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.hasGroupSupport() && VaultAdapter.permission.playerInGroup(player.getWorld(), player.getName(), groupName))
					return true;
				else if (VaultAdapter.isPermissionsLoaded() && !VaultAdapter.permission.hasGroupSupport())
					return VaultAdapter.permission.has(player, "group." + groupName);
				return false;
			}
		}, message);
	}
	
	public static void sendMessageToGroup(final String groupName, String... messages)
	{
		sendMessage(Bukkit.getOnlinePlayers(), new Filter<Player>() {
			public boolean isFiltered(Player player) {
				if (VaultAdapter.isPermissionsLoaded() && VaultAdapter.permission.hasGroupSupport() && VaultAdapter.permission.playerInGroup(player.getWorld(), player.getName(), groupName))
					return true;
				else if (VaultAdapter.isPermissionsLoaded() && !VaultAdapter.permission.hasGroupSupport())
					return VaultAdapter.permission.has(player, "group." + groupName);
				return false;
			}
		}, messages);
	}
	
	public static String parseDeathMessage(Player target) {
		
		ChatColor attackerColor = ChatColor.GREEN,
				targetColor = ChatColor.RED,
				actionColor = ChatColor.AQUA,
				itemColor = ChatColor.GOLD;
		
		StringBuilder sb = new StringBuilder();
		
		Entity attacker = null;
		ItemStack attackerItem = null;
		
		EntityDamageEvent e = target.getLastDamageCause();
		DamageCause cause = e.getCause();
		
		sb.append(targetColor + target.getName());
		
		switch (cause) {
			case ENTITY_ATTACK:
			case FALL:
	            if (e instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e;
					Entity nEntity = nEvent.getDamager();
					attacker = nEntity;
					
					switch (nEntity.getType()) {
						case PLAYER:
							attackerItem = ((Player) attacker).getItemInHand();
							
							if (cause == DamageCause.ENTITY_ATTACK)
								sb.append(actionColor + " was slain by " + ((Player) attacker).getName());
							else if (cause == DamageCause.FALL)
								sb.append(actionColor + " was doomed to fall by " + ((Player) attacker).getName());
		
							if (attackerItem.getType() != Material.AIR) {
								sb.append("'s " + attackerItem.getType().name());
							}
							break;
						case ARROW:
							sb.append(actionColor + " was shot");
							
							if (((Arrow) attacker).getShooter() instanceof Player){
								
								Player attacker2 = (Player) ((Arrow) attacker).getShooter();
								
								sb.append(" by " + attackerColor + attacker2.getName());
								
								attackerItem = ((Player) attacker).getItemInHand();
								if (attackerItem.getType() != Material.AIR) {
									sb.append(actionColor + " using " + itemColor + attackerItem.getType().name());
								}
								
							} else if (((Arrow) attacker).getShooter() instanceof Skeleton) {
								
								Skeleton attacker2 = (Skeleton) ((Arrow) attacker).getShooter();
								
								sb.append(" by " + attackerColor + attacker2.getType().name());
								
								attackerItem = ((Skeleton) attacker).getEquipment().getItemInHand();
								if (attackerItem.getType() != Material.AIR) {
									sb.append(actionColor + " using " + itemColor + attackerItem.getType().name());
								}
								
							}
							break;
						case BLAZE:
							sb.append(actionColor + " was FireBalled by a " + attacker.getType().name());
							break;
						case CAVE_SPIDER:
							sb.append(actionColor + " was Poisoned by a " + attacker.getType().name());
							break;
						case CREEPER:
							sb.append(actionColor + " was Blown up by a " + attacker.getType().name());
							break;
						case ENDERMAN:
							sb.append(actionColor + " was slendered by a " + attacker.getType().name());
							break;
						case ENDER_DRAGON:
							sb.append(actionColor + " was eaten by a " + attacker.getType().name());
							break;
						case ENDER_PEARL:
							sb.append(actionColor + " was killed by his own " + attacker.getType().name());
							break;
						case FALLING_BLOCK:
							sb.append(actionColor + " was suffecated by a " + attacker.getType().name());
							break;
						case FIREBALL:
						case SMALL_FIREBALL:
							sb.append(actionColor + " was FireBalled to death");
							break;
						case FISHING_HOOK:
							sb.append(actionColor + " was hooked to death");
							if (((Projectile) attacker).getShooter().getType() == EntityType.PLAYER) {
								sb.append(" by " + ((Player) ((Projectile) attacker).getShooter()).getDisplayName());
							}
							break;
						case GHAST:
							sb.append(actionColor + " was FireBalled by a " + attacker.getType().name());
							break;
						case GIANT:
							sb.append(actionColor + " was stomped to death by a " + attacker.getType().name());
							break;
						case IRON_GOLEM:
							sb.append(actionColor + " was crushed by a " + attacker.getType().name());
							break;
						case LIGHTNING:
							sb.append(actionColor + " was Fried to death");
							break;
						case MAGMA_CUBE:
							sb.append(actionColor + " was squished by a " + attacker.getType().name());
							break;
						case PIG_ZOMBIE:
							sb.append(actionColor + " was chewed up by a " + attacker.getType().name());
							break;
						case PRIMED_TNT:
							sb.append(actionColor + " was blown up");
							break;
						case SILVERFISH:
							sb.append(actionColor + " was eaten alive by a " + attacker.getType().name());
							break;
						case SKELETON:
							sb.append(actionColor + " was slain by a rogue " + attacker.getType().name());
							break;
						case SLIME:
							sb.append(actionColor + " was slimed to death");
							break;
						case SNOWBALL:
							sb.append(actionColor + " was shot by a snow ball");
							break;
						case SPIDER:
							sb.append(actionColor + " got caught in a web");
							break;
						case SPLASH_POTION:
							sb.append(actionColor + " was killed by a bottle of magic");
							break;
						case WITCH:
							sb.append(actionColor + " was off to see the Wizard, the wonderful Wizard of " + ChatColor.RED + "Nox");
							break;
						case WITHER:
						case WITHER_SKULL:
							sb.append(actionColor + " was withered to death");
							break;
						case WOLF:
							sb.append(actionColor + " became a chew toy");
							break;
						case ZOMBIE:
							sb.append(actionColor + " was eaten alive by a " + attacker.getType().name());
							break;
						default:
							break;
							
					}
	            }
			case DROWNING:
				sb.append(actionColor + " forgot to come up for air");
				break;
			case BLOCK_EXPLOSION:
				sb.append(actionColor + " was blown up");
				break;
			case CONTACT:
				sb.append(actionColor + " was pricked to death");
				break;
			case FIRE:
			case FIRE_TICK:
				sb.append(actionColor + " was burned to death");
				break;
			case LAVA:
				sb.append(actionColor + " took a lava bath");
				break;
			case LIGHTNING:
				sb.append(actionColor + " was Fried to death");
				break;
			case POISON:
				sb.append(actionColor + " was poisoned to death");
				break;
			case STARVATION:
				sb.append(actionColor + " starved to death");
				break;
			case SUFFOCATION:
				sb.append(actionColor + " tried to breath earth...");
				break;
			case SUICIDE:
				sb.append(actionColor + " managed to kill themself...");
				break;
			case THORNS:
				break;
			case VOID:
				sb.append(actionColor + " was fell out of Minecraftia");
				break;
			default:
				sb.append(actionColor + " died");
				break;
		}
		return sb.toString();
	}
}
	
