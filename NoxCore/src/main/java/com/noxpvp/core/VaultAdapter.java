package com.noxpvp.core;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.noxpvp.core.locales.CoreLocale;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultAdapter {
	public static class GroupUtils {
		
		public static List<String> getGroupList(){
			if (isChatLoaded() && isPermissionsLoaded() && permission.hasGroupSupport()){
				List<String> list = Arrays.asList(VaultAdapter.permission.getGroups());
				
				return list;
			}
			
			return null;
		}
		
		public static String getPlayerGroup(Player p) {
			return VaultAdapter.permission.getPrimaryGroup(p);
		}
		
		public static String getFormatedPlayerName(Player p) {
			String group = getPlayerGroup(p);
			
			if (group != null)
				return CoreLocale.GROUP_TAG_PREFIX.get(group) + p.getName() + CoreLocale.GROUP_TAG_SUFFIX.get(group);
			
			return p.getName();
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
			String finalGroup;
			
			if ((finalGroup = getPlayerGroup(p)) == null) return;
			
			CommonScoreboard pBoard = CommonScoreboard.get(p);
			CommonTeam team = CommonScoreboard.getTeam(finalGroup + "Team");
			
			if (team == null) { 
				NoxCore.getInstance().log(Level.WARNING, "The team was not found.");
				team = CommonScoreboard.dummyTeam;
			}
			
			for (CommonTeam t2 :CommonScoreboard.getTeams()) {
				if (t2.equals(team))
					continue;
				if (t2.getPlayers().contains(p.getName()))
					t2.removePlayer(p);
			}
			
			pBoard.setTeam(team);
			team.addPlayer(p);
		}
		
	}
	
	public static class PermUtils {
		
		public static boolean hasPermission(World w, Player p, String perm){
			if (isPermissionsLoaded())
				return permission.has(w.getName(), p.getName(), perm);
			
			return false;
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
}
