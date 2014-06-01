package com.noxpvp.core.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class QuestionBox extends CoreBox {

	private static ItemStack confirmItem, denyItem;
	public static final String MENU_NAME = "Questioner";
	
	static {
		confirmItem = new ItemStack(Material.WOOL, 1, (short) 5);		
		ItemMeta meta = confirmItem.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Click to confirm");		
		confirmItem.setItemMeta(meta);
		
		denyItem = new ItemStack(Material.WOOL, 1, (short) 14);
		meta = denyItem.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Click to cancel");
		denyItem.setItemMeta(meta);
	}
	
	public QuestionBox(Player p, String question) {
		this(p, question, null);
	}
	
	public QuestionBox(Player p, String question, CoreBox backbutton) {
		super(p, ChatColor.AQUA + question, InventoryType.DISPENSER, backbutton);
		
		
		//Setup Items
		
		//Confirm
		final CoreBoxItem confirm = new CoreBoxItem(this, QuestionBox.confirmItem) {
			
			public boolean onClick(InventoryClickEvent click) {
				QuestionBox.this.onConfirm();
				return true;
			}
		};
		
		//Deny
		final CoreBoxItem deny = new CoreBoxItem(this, QuestionBox.denyItem) {
			
			public boolean onClick(InventoryClickEvent click) {
				QuestionBox.this.onDeny();
				return true;
			}
		};
		
		addMenuItem(3, confirm);
		addMenuItem(5, deny);
		
	}
	
	public abstract void onConfirm();
	public abstract void onDeny();

}
