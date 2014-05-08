package com.noxpvp.mmo.gui;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.noxpvp.core.gui.CoreBox;
import com.noxpvp.core.gui.CoreBoxItem;
import com.noxpvp.mmo.MMOPlayer;
import com.noxpvp.mmo.PlayerManager;
import com.noxpvp.mmo.classes.internal.PlayerClass;
import com.noxpvp.mmo.locale.MMOLocale;
import com.noxpvp.mmo.util.PlayerClassUtil;

public class ClassChooseMenu extends CoreBox {

	public final static String MENU_NAME = "Class Selection";
	public final static String MENU_MAIN_COLOR = MMOLocale.GUI_MENU_NAME_COLOR.get();
	
	private final static int size = 18;
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new ClassChooseMenu(getPlayer(), null);
		
	}
	
	public ClassChooseMenu(Player p, @Nullable CoreBox backButton) {
		super(p, MMOLocale.GUI_MENU_NAME_COLOR.get() + MENU_NAME, size, backButton);
		
		Inventory box = getBox();
		List<PlayerClass> availableClasses = PlayerClassUtil.getAvailableClasses(p);
		
		ItemStack primarySign = new ItemStack(Material.SIGN);
		ItemStack secondarySign = new ItemStack(Material.SIGN);
		
		ItemMeta pMeta = primarySign.getItemMeta();
		ItemMeta sMeta = secondarySign.getItemMeta();
		
		String lorePrefix = ChatColor.GREEN.toString() + ChatColor.ITALIC;
		
		pMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a primary class");
		sMeta.setDisplayName(MENU_MAIN_COLOR + "Pick a Secondary class");
		
		pMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row", lorePrefix + "to select a primary class"));
		sMeta.setLore(Arrays.asList(lorePrefix + "Click an item on this row", lorePrefix + "to select a secondary class"));
		
		primarySign.setItemMeta(pMeta);
		secondarySign.setItemMeta(sMeta);
		
		box.setItem(0, primarySign);
		box.setItem(9, secondarySign);
		
		for (PlayerClass clazz : availableClasses) {
			
			ClassChooseMenuItem boxItem = new ClassChooseMenuItem(this, clazz.getIdentifingItem(), clazz) {
				public void onClick(InventoryClickEvent click) {
					
					if (getPlayerClass().isPrimaryClass()) {
						hide();
						new ClassMenu(getPlayer(), getPlayerClass(), ClassChooseMenu.this).show();
						
						return;
					} else {
						MMOPlayer mmoPlayer;
						if ((mmoPlayer = PlayerManager.getInstance().getPlayer(getPlayer())) != null){
							mmoPlayer.setSecondaryClass(getPlayerClass());
							hide();
							
							return;	
						}
					}					
					
				}
			};
			
			if (clazz.isPrimaryClass()){
				 for (int i = 2; i < -1 + (9 * 2); i++) {
					 if (i > 8 && i < 11)
						 i = 11;
					
					 if (getBox().getItem(i) == null){
						 addMenuItem(boxItem);
						 box.setItem(i, clazz.getIdentifingItem());
						 
						 break;
					 }
				}
				
			} else {
				for (int i = 20; i < 17 + (9 * 2); i++) {
					if (i > 26 && i < 29)
						i = 29;
					
					if (getBox().getItem(i) == null){
						addMenuItem(boxItem);
						box.setItem(i, clazz.getIdentifingItem());
					}
				}
			}
		}
	}

	public abstract class ClassChooseMenuItem extends CoreBoxItem {

		private PlayerClass pClass;
		
		public ClassChooseMenuItem(ClassChooseMenu parent, ItemStack item, PlayerClass clazz) {
			super(parent, item);
			
			this.pClass = clazz;
		}
		
		public PlayerClass getPlayerClass(){
			return this.pClass;
		}
		
	}
}
