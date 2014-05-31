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
