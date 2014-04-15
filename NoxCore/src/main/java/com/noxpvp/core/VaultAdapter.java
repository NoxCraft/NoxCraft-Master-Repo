package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam.FriendlyFireType;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.locales.CoreLocale;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultAdapter {
	public static class GroupUtils {
		static ModuleLogger log;
		
		public static List<String> getGroupList(){
			if (isPermissionsLoaded() && permission.hasGroupSupport()){
				List<String> list = Arrays.asList(VaultAdapter.permission.getGroups());
				
				return list;
			} else {
				if (log != null)
					log.warning("Could not get group list... " + (!isPermissionsLoaded()?"Permissions not loaded.":permission.hasGroupSupport()?"":"No Group Support") );
			}
			
			return new ArrayList<String>();
		}
		
		public static String getPlayerGroup(Player p) {
			if (isPermissionsLoaded())
				return VaultAdapter.permission.getPrimaryGroup(p);
			return null;
		}
		
		public static String getFormatedPlayerName(Player p) {
			String group = getPlayerGroup(p);
			
			if (group != null)
				return CoreLocale.GROUP_TAG_PREFIX.get(group) + p.getName() + CoreLocale.GROUP_TAG_SUFFIX.get(group);
			
			return p.getName();
		}

		private static void setupTeams() {
			for (String group : getGroupList()) {
				
				String name = group + "Team";
				if (CommonScoreboard.getTeam(name) == null) {

					CommonTeam team = CommonScoreboard.newTeam(name);

					team.setFriendlyFire(FriendlyFireType.ON);
					team.setPrefix(CoreLocale.GROUP_TAG_PREFIX.get(group));
					team.setSuffix(CoreLocale.GROUP_TAG_SUFFIX.get(group));
					team.setSendToAll(true);

					team.show();
				} else {
					CommonTeam team = CommonScoreboard.getTeam(name);

					team.setPrefix(CoreLocale.GROUP_TAG_PREFIX.get(group));
					team.setSuffix(CoreLocale.GROUP_TAG_SUFFIX.get(group));
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
			String finalGroup = getPlayerGroup(p);
			
			Bukkit.broadcastMessage("final group for " + p.getName() + "- " + finalGroup);

			CommonScoreboard pBoard = CommonScoreboard.get(p);
			CommonTeam team = CommonScoreboard.getTeam(finalGroup + "Team");

			if (team == null) { 
				NoxCore.getInstance().log(Level.WARNING, "The team was not found.");
				team = CommonScoreboard.dummyTeam;
			}

			for (CommonTeam t2 :CommonScoreboard.getTeams()) {
				if (getGroupList().contains(t2.getName().replace("Team", "")))
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
			
			return p.hasPermission(perm);
		}

		public static boolean hasPermission(NoxPlayer p, String string) {
			if (p.isOnline())
				return hasPermission(p.getLastWorld(), p.getPlayer(), string);
			return hasPermission(p.getLastWorldName(), p.getPlayerName(), string);
		}

		public static boolean hasPermission(String world, String playerName, String perm) {
			if (isPermissionsLoaded())
				return permission.has(world, playerName, perm);
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
		GroupUtils.log = NoxCore.getInstance().getModuleLogger("VaultAdapter", "GroupUtils");
		setupGroupUtils();
		setupChat();
		setupEconomy();
		setupPermission();
	}
	
	public static boolean setupGroupUtils() {
		if (isPermissionsLoaded()) {
			GroupUtils.setupTeams();
			GroupUtils.reloadAllGroupTags();
			
			return true;
		} else {
			return false;
		}
		
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
