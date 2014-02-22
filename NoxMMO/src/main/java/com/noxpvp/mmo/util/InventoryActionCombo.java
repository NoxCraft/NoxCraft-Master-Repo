package com.noxpvp.mmo.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.inventory.InventoryAction;

/**
 * Specially made enum to figure out Inventory Actions without extra code in EVERY method.
 */
public enum InventoryActionCombo {//you cant pick up everything...
	ANY_PLACE(InventoryAction.PLACE_ALL, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME),
	ANY_PICKUP(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME);
	
	private List<InventoryAction> actions;
	
	private InventoryActionCombo(InventoryAction... actions) {
		this(Arrays.asList(actions));
	}
	
	private InventoryActionCombo(List<InventoryAction> actions) {
		this.actions = actions;
	}
	
	public boolean contains(InventoryAction action) {
		return actions.contains(action);
	}
}
