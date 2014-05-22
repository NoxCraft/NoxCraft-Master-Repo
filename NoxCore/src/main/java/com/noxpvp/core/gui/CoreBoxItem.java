package com.noxpvp.core.gui;

import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.gui.ICoreBox.ICoreBoxItem;

public abstract class CoreBoxItem implements ICoreBoxItem {

	private CoreBox box;
	private ItemStack item;

	public CoreBoxItem(CoreBox parent, ItemStack item) {
		this.item = item;
		this.box = parent;
	}

	public CoreBox getParentBox() {
		return box;
	}

	public ItemStack getItem() {
		return item;
	}

}
