package com.noxpvp.core.gui;

import org.bukkit.inventory.ItemStack;

public interface MenuItemRepresentable {

	/**
	 * Returns and {@link ItemStack} to represent this object in a player viewable menu
	 *
	 * @return {@link ItemStack} identifier
	 */
	public ItemStack getIdentifiableItem();
	
}
