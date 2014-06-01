package com.noxpvp.core.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class QuestionBox extends CoreBox {

	private static ItemStack confirmItem, denyItem;
	public static final String MENU_NAME = "Questioner";
	
	static {
		confirmItem = new ItemStack(Material.WOOL, 1, (short) 5);
		denyItem = new ItemStack(Material.WOOL, 1, (short) 14);
	}
	
	public QuestionBox(Player p, String question, ItemStack qstnItem) {
		super(p, question, 1);
		
		
		//Setup Items
		
		//Question
		final CoreBoxItem qstn = new CoreBoxItem(this, qstnItem) {
			
			public boolean onClick(InventoryClickEvent click) {
				return false;
			}
		};
		addMenuItem(1, qstn);
		
		//Confirm / deny
		final CoreBoxItem confirm = new CoreBoxItem(this, QuestionBox.confirmItem) {
			
			public boolean onClick(InventoryClickEvent click) {
				QuestionBox.this.onConfirm();
				return true;
			}
		}, deny = new CoreBoxItem(this, QuestionBox.denyItem) {
			
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
