package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam.FriendlyFireType;
import com.noxpvp.core.locales.CoreLocale;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultAdapter {
	public static class GroupUtils {
		private static LinkedList<String> groupList = new LinkedList<String>(Arrays.asList(
				"hadmin", "admin", "mod", "jmod", "helper", "imperial", "king", "sponsor", "vip", "peasant"));
		
		private static List<String> teamNames = new ArrayList<String>();
		
		public static List<String> getGroupList() {
			return Collections.unmodifiableList(groupList);
		}
		
		public static void setGroupList(Collection<String> s) {
			groupList = new LinkedList<String>(s);
			
			teamNames.clear();
			for (String group : groupList)
				teamNames.add(group + "Team");
			
			setupTeams();
			reloadAllGroupTags();
		}
		
		private static void setupTeams() {
			for (String name : teamNames) {
				if (CommonScoreboard.getTeam(name) == null) {
					
					CommonTeam team = CommonScoreboard.newTeam(name);
					
					team.setFriendlyFire(FriendlyFireType.ON);
					team.setPrefix(CoreLocale.GROUP_TAG_PREFIX.get(name.replace("Team", "")));
					team.setSuffix(CoreLocale.GROUP_TAG_SUFFIX.get(name.replace("Team", "")));
					team.setSendToAll(true);
					
					team.show();
				} else {
					CommonTeam team = CommonScoreboard.getTeam(name);
					
					team.setPrefix(CoreLocale.GROUP_TAG_PREFIX.get(name.replace("Team", "")));
					team.setSuffix(CoreLocale.GROUP_TAG_SUFFIX.get(name.replace("Team", "")));
				}
			}
		}

		public static void reloadAllGroupTags() {
			if (isChatLoaded() && isPermissionsLoaded() && permission.hasGroupSupport())
				for (Player p : Bukkit.getOnlinePlayers()) {
					loadGroupTag(p);
				}
		}
		
		public static void reloadGroupTag(Player p) {
			if (p == null) return;
			
			loadGroupTag(p);
		}
		
		private static void loadGroupTag(Player p) {
			String[] groups = VaultAdapter.permission.getPlayerGroups(p);
			
			if (groups.length < 0) return;
			
			int ind = 100;
			String finalGroup = null;
			
			for (String group : groups) {
				if (groupList.indexOf(group) >= 0 && groupList.indexOf(group) < ind) {
					ind = groupList.indexOf(group);
					finalGroup = group;
					
				}
			}
			Bukkit.broadcastMessage("final group for " + p.getName() + "- " + finalGroup);
			
			CommonScoreboard pBoard = CommonScoreboard.get(p);
			CommonTeam team = CommonScoreboard.getTeam(finalGroup + "Team");
			
			if (team == null) { 
				NoxCore.getInstance().log(Level.WARNING, "The team was not found.");
				team = CommonScoreboard.dummyTeam;
			}
			
			for (CommonTeam t2 :CommonScoreboard.getTeams()) {
				if (teamNames.contains(t2.getName()))
					if (t2.getPlayers().contains(p.getName()))
						t2.removePlayer(p);
			}
			
			pBoard.setTeam(team);
			team.addPlayer(p);
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
