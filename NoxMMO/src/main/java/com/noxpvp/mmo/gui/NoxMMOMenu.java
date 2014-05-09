package com.noxpvp.mmo.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.mmo.locale.MMOLocale;

public class NoxMMOMenu extends CoreBox {
	
	public final static String MENU_NAME = "NoxMMO Menu";
	public final static int size = 1;
	
	public NoxMMOMenu(Player p) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size);
		
		int curInd = 2;
		
		
		
	}

}
