package com.noxpvp.mmo;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IAbilityCycler {
	
	public Player getUser();
	
	public ItemStack getBaseItem();
	
	public void displayMessage(String message, int ticks);
	
	public void setDisplay(String display);
	
	//TODO Plan out thoroughly before fixing up ability cycler for our current specifications
}
