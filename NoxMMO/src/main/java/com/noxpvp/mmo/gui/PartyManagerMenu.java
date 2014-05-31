package com.noxpvp.mmo.gui;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.gui.CoreBox;

public class PartyManagerMenu extends CoreBox {
	
	public final static String MENU_NAME = "Party Manager";
	public final static int size = 2;
	
	public PartyManagerMenu(Player p) {
		this(p, null);
	}
	
	public PartyManagerMenu(Player p, CoreBox backbutton) {
		super(p, MENU_NAME, size, backbutton);
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new PartyManagerMenu(getPlayer(), this.getBackButton());
	}
	
	@Override
	public ItemStack getIdentifiableItem() {
		ItemStack item = super.getIdentifiableItem();
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(Arrays.asList(ChatColor.AQUA + "Under Development"));
		item.setItemMeta(meta);
		
		return item;
	}

}
