/*
 * Copyright (c) 2014. NoxPVP.com
 *
 * All rights are reserved.
 *
 * You are not permitted to
 * 	Modify
 * 	Redistribute nor distribute
 * 	Sublicense
 *
 * You are required to keep this license header intact
 *
 * You are allowed to use this for non commercial purpose only. This does not allow any ad.fly type links.
 *
 * When using this you are required to
 * 	Display a visible link to noxpvp.com
 * 	For crediting purpose.
 *
 * For more information please refer to the license.md file in the root directory of repo.
 *
 * To use this software with any different license terms you must get prior explicit written permission from the copyright holders.
 */

package com.noxpvp.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.bergerkiller.bukkit.common.ModuleLogger;
import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
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
		

		public static void reloadAllGroupTags() {
			if (isPermissionsLoaded() && permission.hasGroupSupport())
				for (Player p : Bukkit.getOnlinePlayers()) {
					loadGroupTag(p);
				}
		}
		
		public static void reloadGroupTag(Player p) {
			if (p == null || !isPermissionsLoaded()) return;
			
			loadGroupTag(p);
		}
		
		private static void loadGroupTag(Player p) {
			String finalGroup;
			
			if ((finalGroup = getPlayerGroup(p)) == null) return;
			
			CommonScoreboard pBoard = CommonScoreboard.get(p);
			CommonTeam team = CommonScoreboard.getTeam(finalGroup + "Team");
			
			if (team == null) { 
				team = CommonScoreboard.dummyTeam;
				NoxCore.getInstance().log(Level.WARNING, "The team was not found, creating a team for: " + finalGroup + '.');
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
