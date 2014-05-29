package com.noxpvp.mmo.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.core.gui.CoreBoxRegion;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.MMOPlayerManager;
import com.noxpvp.mmo.locale.MMOLocale;

public class NoxMMOMenu extends CoreBox {

	public static final int size = 9;
	public static final String MENU_NAME = "NoxMMO";
	
	public NoxMMOMenu(Player p) {
		this(p, null);
	}
	
	public NoxMMOMenu(Player p, CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);	
		
		Inventory box = getBox();
		MMOPlayer mp = MMOPlayerManager.getInstance().getPlayer(p);
		
		//Set up items
		CoreBoxRegion links = new CoreBoxRegion(this, new Vector(0, 0, 3), 0, 3);

		box.setItem(0, mp.getIdentifiableItem());
		box.setItem(1, mp.getPrimaryClass().getIdentifiableItem());
		
		final ClassChooseMenu ccm = new ClassChooseMenu(p, NoxMMOMenu.this);
		links.add(new CoreBoxItem(this, ccm.getIdentifiableItem()) {
			
			public boolean onClick(InventoryClickEvent click) {
				ccm.show();
				return true;
			}
		});
		
		//TODO More
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new NoxMMOMenu(getPlayer(), null);
	}
}
