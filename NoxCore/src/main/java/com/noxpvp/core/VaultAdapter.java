package com.noxpvp.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam.FriendlyFireType;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultAdapter {
	public static class GroupUtils {
		private static LinkedList<String> groupList = new LinkedList<String>(Arrays.asList(
				"hadmin", "admin", "mod", "jmod", "helper", "imperial", "king", "sponsor", "vip", "peasant"));
		
		public static List<String> getGroupList() {
			return Collections.unmodifiableList(groupList);
		}
		
		private static void loadGroupName(Player p) {
			if (!p.isOnline() || p == null) return;
			
			String[] groups = VaultAdapter.permission.getPlayerGroups(p);
			
			if (groups.length < 0) return;
			
			int ind = 100;
			String finalGroup = null;
			
			for (String group : groups) {
				if (groupList.indexOf(group) < ind) {
					ind = groupList.indexOf(group);
					finalGroup = group;
				}
			}
			
			if (finalGroup != null) {
					
				String teamName = finalGroup + "Team";
				World w = p.getWorld();
				
				if (CommonScoreboard.getTeam(teamName) != null) {
					CommonScoreboard.loadTeam(teamName).addPlayer(p);
				} else if (VaultAdapter.isChatLoaded()){
					CommonTeam team = CommonScoreboard.newTeam(teamName);
					
					team.setSendToAll(true);
					team.setFriendlyFire(FriendlyFireType.ON);
					
					team.setPrefix(VaultAdapter.chat.getGroupPrefix(w, finalGroup));
					team.setSuffix(VaultAdapter.chat.getGroupSuffix(w, finalGroup));
					
					team.show();
						
				}
			}
		}
		
		public static void reloadGroupName(Player p) {
			if (!p.isOnline() || p == null) return;
			
			loadGroupName(p);
		}
		
		public static void reloadGroupNames() {
			if (isChatLoaded() && isPermissionsLoaded() && permission.hasGroupSupport())
				for (Player p : Bukkit.getOnlinePlayers()) {
					loadGroupName(p);
				}
		}
		
		public static void setGroupList(Collection<String> s) {
			groupList = new LinkedList<String>(s);
		}
	}
	
	public static Chat chat = null;
	public static Economy economy = null;
	
	public static Permission permission = null;
	
	public static boolean isChatLoaded() { return (chat != null); }
	
	public static boolean isEconomyLoaded() { return (economy != null); }
	
	public static boolean isPermissionsLoaded() { return (permission != null); }
	public static void load()
	{
		setupChat();
		setupEconomy();
		setupPermission();
	}
	public static boolean setupChat()
	{
		RegisteredServiceProvider<Chat> service = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (service != null)
			chat = service.getProvider();
		return (chat != null);
	}
	
	public static boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> service = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (service != null)
			economy = service.getProvider();
		return (economy != null);
	}
	
	public static boolean setupPermission()
	{
		RegisteredServiceProvider<Permission> service = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (service != null)
			permission = service.getProvider();
		return (permission != null);
	}
	
	public static void unload()
	{
		economy = null;
		permission = null;
		chat = null;
	}
}
