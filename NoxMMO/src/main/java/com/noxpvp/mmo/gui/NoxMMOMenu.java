package com.noxpvp.mmo.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.mmo.locale.MMOLocale;

public class NoxMMOMenu extends CoreBox{
	
	public final static String MENU_NAME = "NoxMMO Menu";
	public final static int size = 1;
	
	private List<CoreBoxItem> boxItems;
	
	public NoxMMOMenu(Player p) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size);
		
		this.boxItems = new ArrayList<CoreBoxItem>();
		int curInd = 2;
		
		
		
	}

	public void clickHandler(InventoryClickEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void closeHandler(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
		
	}

}
