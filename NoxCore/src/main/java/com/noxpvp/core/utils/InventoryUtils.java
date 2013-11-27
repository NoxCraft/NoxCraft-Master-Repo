package com.noxpvp.core.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public static boolean hasItems(Inventory inv, ItemStack item){
		Material type = item.getType();
		int totals = 0;
		
		for(ItemStack s : inv.getContents()){
			if (s.getType() != type) continue;
			
			totals += s.getAmount();
			
		}
		
		return totals >= item.getAmount();
		
	}

}
