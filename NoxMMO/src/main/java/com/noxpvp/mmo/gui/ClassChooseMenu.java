package com.noxpvp.mmo.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.util.InventoryActionCombo;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassChooseMenu extends CoreBox{

	public final static String MENU_NAME = "Class Selection";
	public final static String MENU_MAIN_COLOR = MMOLocale.GUI_MENU_NAME_COLOR.get();
	
	private final static int size = 18;

	private Map<Integer, ClassChooseMenuItem> menuItems;
	
	public ClassChooseMenu(Player p, @Nullable CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);
		
		Inventory box = getBox();
		menuItems = new HashMap<Integer, ClassChooseMenuItem>();
		List<PlayerClass> availableClasses = PlayerClassUtil.getAvailableClasses(p);
		
		ItemStack primarySign = new ItemStack(Material.SIGN);
		ItemStack secondarySign = new ItemStack(Material.SIGN);
		
		ItemMeta pMeta = primarySign.getItemMeta();
		ItemMeta sMeta = secondarySign.getItemMeta();
		
		String lorePrefix = ChatColor.GREEN + "" + ChatColor.ITALIC;
		
		pMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a primary class");
		sMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a Secondary class");
		
		pMeta.setLore(Arrays.asList(lorePrefix + "Click a item on this row", lorePrefix + "to select the teir you want to use"));
		sMeta.setLore(Arrays.asList(lorePrefix + "Click a item on this row", lorePrefix + "to select a secondary class"));
		
		primarySign.setItemMeta(pMeta);
		secondarySign.setItemMeta(sMeta);
		
		menuItems.put(0, null);
		box.setItem(0, primarySign);
		
		menuItems.put(9, null);
		box.setItem(9, secondarySign);
		
		for (PlayerClass clazz : availableClasses) {
			
			ItemStack item = clazz.getIdentifingItem();
			ItemMeta meta = item.getItemMeta();
			
			meta.setLore(null);//Remove any vanilla item lore
			meta.setLore(clazz.getLore());
			
			item.setItemMeta(meta);
			
			ClassChooseMenuItem boxItem = new ClassChooseMenuItem(this, item, clazz) {
				public void onClick(InventoryClickEvent click) {
					if (!InventoryActionCombo.ANY_PICKUP.contains(click.getAction()) && !InventoryActionCombo.ANY_PLACE.contains(click.getAction()))
						return;
					
					ClassChooseMenuItem item;
					if ((item = menuItems.get(click.getSlot())) != null && item.getItem() == click.getCurrentItem()){
						
						if (getPlayerClass().isPrimaryClass())
							new ClassMenu(getPlayer(), getPlayerClass(), ClassChooseMenu.this).show();
						else {
							MMOPlayer mmoPlayer;
							if ((mmoPlayer = PlayerManager.getInstance().getPlayer(getPlayer())) != null){
								mmoPlayer.setSecondaryClass(getPlayerClass());
								hide();
							}
						}
					}
				}
			};
			
			if (clazz.isPrimaryClass()){
				 for (int i = 2; i < -1 + (9 * 2); i++) {
					 if (i > 8 && i < 11)
						 i = 11;
					
					 if (!menuItems.containsKey(i)){
						 menuItems.put(i, boxItem);
						 box.setItem(i, item);
					 }
				}
				
			} else {
				for (int i = 20; i < 17 + (9 * 2); i++) {
					if (i > 26 && i < 29)
						i = 29;
					
					if (!menuItems.containsKey(i)){
						menuItems.put(i, boxItem);
						box.setItem(i, item);
					}
				}
			}
		}
	}

	public void clickHandler(InventoryClickEvent event) {
		InventoryAction action = event.getAction();
		
		if (action != InventoryAction.PICKUP_ALL &&
			action != InventoryAction.SWAP_WITH_CURSOR)
			return;
		
		ItemStack clickItem = event.getCursor() == null? event.getCurrentItem() : event.getCursor();
		if (clickItem == null)
			return;
		
		ClassChooseMenuItem finalItem = null;
		for (ClassChooseMenuItem item : menuItems.values()) {
			if (item == null || !item.equals(clickItem)) continue;
			
			finalItem = item;
		}
		
		if (finalItem != null)
			finalItem.onClick(event);
	}
	
	public void closeHandler(InventoryCloseEvent event) {this.unregister();}
	
	public abstract class ClassChooseMenuItem extends CoreBoxItem {

		private PlayerClass pClass;
		
		public ClassChooseMenuItem(ClassChooseMenu parent, ItemStack item, PlayerClass clazz) {
			super(parent, item);
			
			this.pClass = clazz;
		}
		
		@Override
		public ClassChooseMenu getParentBox(){
			return getParentBox();
		}
		
		public PlayerClass getPlayerClass(){
			return this.pClass;
		}
		
	}
}
