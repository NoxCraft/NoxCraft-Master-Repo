package com.noxpvp.core.listeners;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

import com.bergerkiller.bukkit.common.scoreboards.CommonScoreboard;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam;
import com.bergerkiller.bukkit.common.scoreboards.CommonTeam.FriendlyFireType;
import com.noxpvp.core.NoxCore;
import com.noxpvp.core.PlayerManager;
import com.noxpvp.core.VaultAdapter;
import com.noxpvp.core.data.NoxPlayer;

public class LoginGroupListener extends NoxListener {
		private PlayerManager pm;
		private static LinkedList<String> groupList = new LinkedList<String>(Arrays.asList(
						"hadmin", "admin", "mod", "jmod", "helper", "imperial", "king", "sponsor", "vip", "peasant"));
		
		public static void setGroupList(Collection<String> s) {
				groupList = new LinkedList<String>(s);
		}
		
		public static List<String> getGroupList() {
				return Collections.unmodifiableList(groupList);
		}
		
		public static void reloadGroupNames() {
				for (Player p : Bukkit.getOnlinePlayers()) {
						loadGroupName(p);
				}
		}
		
		public static void reloadGroupName(Player p) {
				if (!p.isOnline() || p == null) return;
				
				loadGroupName(p);
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
		
		public LoginGroupListener()
		{
				this(NoxCore.getInstance());
		}
		
		public LoginGroupListener(NoxCore core)
		{
				super(core);
				this.pm = core.getPlayerManager();
		}
		
		@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
		public void onLogin(PlayerLoginEvent e)
		{
				Player p = null;
				NoxPlayer player = pm.getPlayer(p = e.getPlayer());
				if (player == null) return;
				
				loadGroupName(p);
		}
}