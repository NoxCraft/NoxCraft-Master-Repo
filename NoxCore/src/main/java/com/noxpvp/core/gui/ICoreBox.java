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
