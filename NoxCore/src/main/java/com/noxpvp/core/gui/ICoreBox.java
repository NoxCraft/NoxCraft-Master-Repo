package com.noxpvp.core.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ICoreBox {
	
	/**
	 * Gets the inventory of this box
	 * 
	 * @return Inventory
	 */
	public Inventory getBox();
	
	public Player getPlayer();
	
	/**
	 * Shows the player the non-editable box
	 * 
	 */
	public void show();
	
	/**
	 * Closes the inventory of the player has it open
	 * 
	 */
	public void hide();
	
	/**
	 * Handles the code of what should go on if a player clicks on an item in the box
	 * 
	 * @param event
	 */
	public void clickHandler(InventoryClickEvent event);
	
	/**
	 * Handles the code of what should go on if a player closes the box
	 * 
	 * @param event
	 */
	public void closeHandler(InventoryCloseEvent event);
	
	public interface ICoreBoxItem {
		
		/**
		 * Gets the CoreBox that this item is contained in
		 * 
		 * @return CoreBox
		 */
		public CoreBox getParentBox();
		
		/**
		 * Gets the contained itemstack of this box item
		 * 
		 * @return ItemStack
		 */
		public ItemStack getItem();
		
		/**
		 * Handles the code of what should go on if a player clicks this item, not necessary for most implementations
		 * 
		 * @param event
		 */
		public void onClick(InventoryClickEvent click);
		
	}
	
}
