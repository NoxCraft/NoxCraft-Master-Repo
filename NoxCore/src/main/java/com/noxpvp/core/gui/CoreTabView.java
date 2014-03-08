package com.noxpvp.core.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.tab.TabView;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.noxpvp.core.data.NoxPlayer;
import com.noxpvp.core.locales.CoreLocale;
import com.noxpvp.core.manager.PlayerManager;
import com.noxpvp.core.utils.chat.MessageUtil;

public class CoreTabView {
	
	private static TabView baseTab;
	private static boolean setup = false;
	
	private TabView tab;
	private NoxPlayer p;

	public static void setup() {
		if (setup)
			return;
		
		//May only be called on plugin enable
		baseTab = TabView.createTab(TabView.MAX_WIDTH, TabView.MAX_HEIGHT);
		
		baseTab.setRowText(0, 0, 2, MessageUtil.parseColor(CoreLocale.TAB_VIEW_HEADER.get()).split("\n", 3));
		baseTab.setRowText(1, 0, 2, MessageUtil.parseColor(CoreLocale.TAB_VIEW_UNDERLINE.get()).split("\n", 3));
		
		setup = true;
	}
	
	
	public CoreTabView(NoxPlayer p){
		this.tab = CraftNewTab();
		
		this.p = p;
		setupPlayerView(p);
	}

	public CoreTabView(CoreTabView tab){
		this.tab = tab.tab;
		
	}
	
	public static TabView CraftNewTab(){
		return baseTab.clone();
	}
	
	public CoreTabView clone(){
		return new CoreTabView(this);
	}
	
	public void setupPlayerView(NoxPlayer p) {
		
		UUID player = p.getPlayer().getUniqueId();
		int i = 6;//Refresh other players
		for (Player it : Bukkit.getOnlinePlayers()){
			if (it.getUniqueId().equals(player))
				continue;
			
			tab.set((int) Math.floor(i / 3), (int) Math.floor(i % (3 * (i / 3))), p.getFullName(), PlayerUtil.getPing(it));
			
			CoreTabView itTab = PlayerManager.getInstance().getPlayer(it.getName()).getTabView();
			if (itTab.contains(p))
				return;
			
			itTab.add(p);
		}
		
		tab.displayTo(p.getPlayer());
		
	}
	
	public void delete(){
		int i = 6;//Refresh other players
		for (Player it : Bukkit.getOnlinePlayers()){
			if (it == p)
				continue;
			
			tab.set((int) Math.floor(i / 3), (int) Math.floor(i % (3 * (i / 3))), p.getFullName(), PlayerUtil.getPing(it));
			
			CoreTabView itTab = PlayerManager.getInstance().getPlayer(it.getName()).getTabView();
			if (!itTab.contains(p))
				return;
			
			itTab.remove(p);
		}
		
	}
	
	public void remove(NoxPlayer p){
		remove(p.getFullName());
	}
	
	public void remove(String text){
		for (int i = 0; i < (tab.getHeight() * tab.getWidth()); i++){
			int x = (int) Math.floor(i / 3), z = (int) Math.floor(i % (3 * (i / 3)));
			
			if (tab.getText(x, z).contains(text))
				tab.set(x, z, "", TabView.PING_NONE);
			
		}
	}
	
	public void add(NoxPlayer p){
		for (int i = 0; i < (baseTab.getHeight() * tab.getWidth()); i++){
			int x = (int) Math.floor(i / 3), z = (int) Math.floor(i % (3 * (i / 3)));
			
			if (tab.getText(x, z).isEmpty())
				tab.set(x, z, p.getFullName(), PlayerUtil.getPing(p.getPlayer()));
			
		}
	}
	
	public boolean contains(NoxPlayer p){
		return contains(p.getFullName());
	}
	
	public boolean contains(String text){
		for (int i = 0; i < (baseTab.getHeight() * tab.getWidth()); i++){
			int x = (int) Math.floor(i / 3), z = (int) Math.floor(i % (3 * (i / 3)));
			
			if (tab.getText(x, z) == text)
				return true;
			
			continue;
			
		}
		return false;
	}

}
